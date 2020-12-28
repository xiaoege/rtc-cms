package com.rtc.cms.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rtc.cms.dao.rtc.RtcUserMapper;
import com.rtc.cms.dto.EnterpriseDTO;
import com.rtc.cms.entity.rtc.RtcRefCountry;
import com.rtc.cms.service.EnterpriseService;
import com.rtc.cms.util.CommonConst;
import com.rtc.cms.util.ElasticsearchUtils;
import com.rtc.cms.vo.ResultData;
import com.rtc.cms.vo.SearchEnterpriseListVO;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ChenHang
 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    Logger logger = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    private final RestHighLevelClient client = ElasticsearchUtils.getClient();

    @Value("${rtc.esIndices}")
    private String[] esIndices;

    @Autowired
    private RtcUserMapper rtcUserMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 企业列表查询
     *
     * @param name
     * @param nation
     * @param area
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultData listEnterprise(String name, String nation, String area, String startDate, String endDate, int pageNum, int pageSize) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // 解决jackson无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(pageSize);
        // es分页查询控制在10000条数据以内
        searchSourceBuilder.from(Math.min((pageNum - 1) * pageSize, 10000 - pageSize));
        searchSourceBuilder.sort("@timestamp", SortOrder.DESC);
        searchSourceBuilder.sort("pid", SortOrder.DESC);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must();
        boolQueryBuilder.filter();

        // 无条件搜索
        if ("".equals(name)) {
//            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        } else {
            MatchPhraseQueryBuilder matchPhraseQueryBuilder = new MatchPhraseQueryBuilder("e_name", name);
//            searchSourceBuilder.query(matchPhraseQueryBuilder);
            boolQueryBuilder.must(matchPhraseQueryBuilder);
        }

        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("@timestamp");
        if (!StringUtils.isEmpty(startDate)) {
            rangeQueryBuilder.gte(startDate);
//            searchSourceBuilder.query(rangeQueryBuilder);
        }
        if (!StringUtils.isEmpty(endDate)) {
            rangeQueryBuilder.lte(endDate);
//            searchSourceBuilder.query(rangeQueryBuilder);
//            boolQueryBuilder.filter(rangeQueryBuilder);
        }
            boolQueryBuilder.filter(rangeQueryBuilder);

        SearchRequest searchRequest = null;
        if ("".equals(area)) {
            searchRequest = new SearchRequest(esIndices);
        } else {
            searchRequest = new SearchRequest(area);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        logger.debug("searchRequest:searchType:{}, indices:{}, source:{}", searchRequest.searchType(), searchRequest.indices(), searchRequest.source());
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        List resultList = new ArrayList();
        long total = 0;
        if (searchResponse != null) {
            TotalHits totalHits = searchResponse.getHits().getTotalHits();
            total += totalHits.value;
            SearchHit[] hits = searchResponse.getHits().getHits();
            if (!ObjectUtils.isEmpty(hits)) {
                for (int i = 0; i < hits.length; i++) {
                    SearchHit hit = hits[i];
                    SearchEnterpriseListVO vo = objectMapper.readValue(hit.getSourceAsString(), SearchEnterpriseListVO.class);
                    vo.setEsId(hit.getId());
                    resultList.add(vo);
                }
            }
        }
//        logger.debug("searchRequest:data:{}", resultList);
        return ResultData.success(resultList, total);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData removeEnterprise(String id, String esId, String nation, String eType) {
        RtcRefCountry rtcRefCountry = rtcUserMapper.getTable(eType.toLowerCase());
        String index = rtcRefCountry.getIdx();
        // es删除
        GetRequest getRequest = new GetRequest(index, esId);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = false;
        try {
            exists = client.exists(getRequest, RequestOptions.DEFAULT);
            if (!exists) {
                return ResultData.fail();
            } else {
                DeleteRequest request = new DeleteRequest(index, esId);
                DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
                RefreshRequest refreshRequest = new RefreshRequest(index);
                client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
                DocWriteResponse.Result result = deleteResponse.getResult();
                if (!"DELETED".equals(deleteResponse.getResult().name())) {
                    return ResultData.fail();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // mysql 逻辑删除
        String table = getTable(eType);
        if (!"".equals(table)) {
            jdbcTemplate.update("update " + table + " set status = ? where id = ?", CommonConst.STATUS_NOT_USED, Integer.valueOf(id));
            return ResultData.success();
        }

        return ResultData.fail();
    }

    /**
     * 根据地区找到mysql里对应的表
     *
     * @param nation 国家
     * @param eType  地区
     * @return mysql里对应的表
     */
    @Override
    public String getTable(String eType) {
        String table = rtcUserMapper.getTable(eType).getTb();
        return rtcUserMapper.getTable(eType) == null ? "" : table;
    }

    /**
     * 批量删除公司
     *
     * @param ca
     * @return
     */
    @Override
    public ResultData removeEnterpriseList(String ca) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<EnterpriseDTO> dto = objectMapper.readValue(ca, new TypeReference<List<EnterpriseDTO>>() {
            });
            dto.parallelStream().forEach(i -> removeEnterprise(i.getId(), i.getEsId(), i.getNation(), i.geteType()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.debug("json反序列化错误:{}", ca);
            return ResultData.fail();
        }
        return ResultData.success();
    }

}
