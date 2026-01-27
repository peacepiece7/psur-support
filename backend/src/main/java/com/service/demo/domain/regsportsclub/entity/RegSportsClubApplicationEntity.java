package com.service.demo.domain.regsportsclub.entity;

import java.time.LocalDateTime;

public class RegSportsClubApplicationEntity {
    private Long id;
    private Long applyId;
    private String name;
    private String location;
    private String representativeName;
    private String representativeTelno;
    private String businessNo;
    private Long clubRoleCodeId;
    private Long operatingSportParentCodeId;
    private Long operatingSportChildCodeId;
    private Long approvedClubId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getRepresentativeTelno() {
        return representativeTelno;
    }

    public void setRepresentativeTelno(String representativeTelno) {
        this.representativeTelno = representativeTelno;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getClubRoleCodeId() {
        return clubRoleCodeId;
    }

    public void setClubRoleCodeId(Long clubRoleCodeId) {
        this.clubRoleCodeId = clubRoleCodeId;
    }

    public Long getOperatingSportParentCodeId() {
        return operatingSportParentCodeId;
    }

    public void setOperatingSportParentCodeId(Long operatingSportParentCodeId) {
        this.operatingSportParentCodeId = operatingSportParentCodeId;
    }

    public Long getOperatingSportChildCodeId() {
        return operatingSportChildCodeId;
    }

    public void setOperatingSportChildCodeId(Long operatingSportChildCodeId) {
        this.operatingSportChildCodeId = operatingSportChildCodeId;
    }

    public Long getApprovedClubId() {
        return approvedClubId;
    }

    public void setApprovedClubId(Long approvedClubId) {
        this.approvedClubId = approvedClubId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
