package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SportsClubSummary {
    @Schema(description = "Club ID", example = "10")
    private final Long id;
    @Schema(description = "Club name", example = "Seoul Sports Club")
    private final String name;
    @Schema(description = "Common code ID for club role", example = "10")
    private final Long clubRoleCodeId;

    public SportsClubSummary(Long id, String name, Long clubRoleCodeId) {
        this.id = id;
        this.name = name;
        this.clubRoleCodeId = clubRoleCodeId;
    }
}
