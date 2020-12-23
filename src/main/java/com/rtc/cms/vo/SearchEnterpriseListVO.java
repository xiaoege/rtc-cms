package com.rtc.cms.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ChenHang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchEnterpriseListVO {

    @JsonAlias("e_type")
    private String eType;

    @JsonAlias("e_name")
    private String eName;

    private String nation;
    /**
     * mysql表的id
     */
    private Integer pid;

    /**
     * es-document的id
     */
    private String esId;

    @JsonAlias("enterprise_id")
    private String enterpriseId;

    /**
     * 法人代表
     */
    @JsonAlias("legal_representative")
    private String legalRepresentative;
    /**
     * 成立日期
     */
    @JsonAlias({"establishment_date", "date_of_incorporation"})
    private String establishmentDate;
    /**
     * 公司地址
     */
    @JsonAlias({"registered_address", "registrated_address", "principal_address"})
    private String address;

    @JsonAlias("@timestamp")
    private LocalDateTime createTime;

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getEsId() {
        return esId;
    }

    public void setEsId(String esId) {
        this.esId = esId;
    }

    @Override
    public String toString() {
        return "SearchEnterpriseListVO{" +
                "eType='" + eType + '\'' +
                ", eName='" + eName + '\'' +
                ", nation='" + nation + '\'' +
                ", pid=" + pid +
                ", esId='" + esId + '\'' +
                ", enterpriseId='" + enterpriseId + '\'' +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", establishmentDate='" + establishmentDate + '\'' +
                ", address='" + address + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
