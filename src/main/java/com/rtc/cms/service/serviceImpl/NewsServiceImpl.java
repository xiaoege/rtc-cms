package com.rtc.cms.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rtc.cms.dao.news.RtcNewsDetailMapper;
import com.rtc.cms.dao.news.RtcNewsMapper;
import com.rtc.cms.dto.NewsOperation;
import com.rtc.cms.dto.RtcNewsDTO;
import com.rtc.cms.entity.news.RtcNews;
import com.rtc.cms.service.NewsService;
import com.rtc.cms.vo.NewsDetailVO;
import com.rtc.cms.vo.ResultData;
import com.rtc.cms.vo.RtcNewsDetailVO;
import com.rtc.cms.vo.RtcNewsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenHang
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private RtcNewsMapper rtcNewsMapper;

    @Autowired
    private RtcNewsDetailMapper rtcNewsDetailMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rtc.url}")
    private String url;

    /**
     * 查询新闻列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultData listNews(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RtcNews> rtcNewsList = rtcNewsMapper.selectNewsList();
        PageInfo<RtcNews> of = PageInfo.of(rtcNewsList);
        of.getTotal();
        PageHelper.clearPage();

        List<RtcNewsVO> voList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(rtcNewsList)) {
            for (int i = 0; i < rtcNewsList.size(); i++) {
                RtcNewsVO vo = new RtcNewsVO();
                BeanUtils.copyProperties(rtcNewsList.get(i), vo);
                voList.add(vo);
            }
        }

        return ResultData.success(voList, of.getTotal());
    }

    /**
     * 审核新闻
     *
     * @param body
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultData examinateNews(String body) {
        try {
            NewsOperation newsOperation = objectMapper.readValue(body, NewsOperation.class);
            if (rtcNewsMapper.examinateNews(newsOperation) > 0) {
                return ResultData.success();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResultData.fail();
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultData removeNews(int id) {
        int i = rtcNewsMapper.deleteNews(id);
        if (i > 0) {
            return ResultData.success();
        }
        return ResultData.fail();
    }

    /**
     * 查询单个新闻
     *
     * @param id
     * @return
     */
    @Override
    public ResultData getNews(int id) {
        NewsDetailVO vo = rtcNewsMapper.selectNews(id);
        List<RtcNewsDetailVO> detailList = vo.getNewsDetailList();
        List resultList = new ArrayList();
        vo.setResultList(resultList);
        String description = vo.getDescription();
        if (description != null) {
            description = description.replaceAll("<(img|/img|figcaption|/figcaption|strong|/strong|em|/em|p|/p).*?>", "");
            vo.setDescription(description);
        }
        String previewUrl = vo.getPreview();
        if (previewUrl != null) {
            previewUrl = this.url + previewUrl.substring("/work/images".length());
            vo.setPreview(previewUrl);
        }
        try {
            if (!ObjectUtils.isEmpty(detailList)) {
                for (int i = 0; i < detailList.size(); i++) {
                    RtcNewsDetailVO rtcNewsDetailVO = detailList.get(i);
                    String content = rtcNewsDetailVO.getContent();
                    content = content.replaceAll("<(?!img|figcaption|/figcaption|strong|/strong|em|/em|p|/p).*?>", "");
                    String[] split = content.split("</p>");
                    int contentLength = split.length;
                    if (split != null && contentLength != 1) {
                        contentLength -= 1;
                    }
                    for (int j = 0; j < contentLength; j++) {
                        String p = split[j];
                        if (p.contains("<p")) {
                            p = p.substring(p.indexOf("<p>") + 3).replace("\\n", "");
                        }
                        Map map = new HashMap();
                        String url = "";
                        if (p.contains("figcaption") || p.contains("<img")) {
                            url = p.substring(p.indexOf("'") + 1, p.indexOf("'", p.indexOf("'") + 1));
                            //                        "http://192.168.1.125/chinadaily/2020-07/14/1594717677-8073.jpeg"
                            if (null != url && url.length() > 12) {
                                url = this.url + url.substring(12);
                            }
//                            File picture = new File(url);
                            //                        BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                            BufferedImage sourceImg = ImageIO.read(new URL(url).openStream());
                            // 单位：像素
                            int width = sourceImg.getWidth();
                            int height = sourceImg.getHeight();
                            map.put("data", p.replaceAll("<[^>]*>", ""));
                            map.put("type", "img");
                            map.put("url", url);
                            map.put("width", width);
                            map.put("height", height);
                            resultList.add(map);
                        } else if (p.startsWith(this.url)) {
//                            File picture = new File(p);
                            //                        BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                            BufferedImage sourceImg = ImageIO.read(new URL(p).openStream());
                            // 单位：像素
                            int width = sourceImg.getWidth();
                            int height = sourceImg.getHeight();
                            map.put("data", p.replaceAll("<[^>]*>", ""));
                            map.put("type", "img");
                            map.put("url", p);
                            map.put("width", width);
                            map.put("height", height);
                            resultList.add(map);
                        } else if (p.contains("<strong>")) {
                            map.put("data", p.replaceAll("<[^>]*>", ""));
                            map.put("type", "title");
                            resultList.add(map);
                        } else if (p.contains("<em>")) {
                            map.put("data", p.replaceAll("<[^>]*>", ""));
                            map.put("type", "em");
                            resultList.add(map);
                        } else {
                            map.put("data", p);
                            map.put("type", "content");
                            resultList.add(map);
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultData.success(vo);
    }

    /**
     * 修改新闻
     *
     * @param body
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData modifyNews(String body) {
        try {
            RtcNewsDTO dto = objectMapper.readValue(body, RtcNewsDTO.class);
            rtcNewsMapper.updateByPrimaryKeySelective(dto);
            String uuid = rtcNewsMapper.selectUuid(dto.getId());
            rtcNewsMapper.deleteNewsDetail(uuid);
            ArrayList<String> contentList = dto.getContent();
            if (!ObjectUtils.isEmpty(contentList)) {
                for (int i = 0; i < contentList.size(); i++) {
                    String content = contentList.get(i);
                    if (!content.startsWith("<p>")) {
                        content = "<p>" + content + "</P>";
                        contentList.set(i, content);
                    }
                }
                rtcNewsDetailMapper.insertNewsContent(uuid, contentList);

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResultData.fail();
        }
        return null;
    }
}
