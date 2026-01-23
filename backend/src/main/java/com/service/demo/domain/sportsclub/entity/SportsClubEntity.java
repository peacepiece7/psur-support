package com.service.demo.domain.sportsclub.entity;

import java.time.LocalDateTime;

public class SportsClubEntity {
    private Long id;
    private String name;
    private String location;
    private String representativeName;
    private String representativeTelno;
    private String clubNo;
    private String businessNo;
    private Long clubRoleCodeId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getClubNo() {
        return clubNo;
    }

    public void setClubNo(String clubNo) {
        this.clubNo = clubNo;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
