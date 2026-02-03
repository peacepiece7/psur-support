package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
public class MeDetailResponse {
    @Schema(description = "User info")
    private final UserResponse user;
    @Schema(description = "User roles")
    private final List<UserRoleSummary> userRoles;
    @Schema(description = "Sports clubs owned by user")
    private final List<SportsClubSummary> sportsClubList;

    public MeDetailResponse(UserResponse user,
                            List<UserRoleSummary> userRoles,
                            List<SportsClubSummary> sportsClubList) {
        this.user = user;
        this.userRoles = userRoles;
        this.sportsClubList = sportsClubList;
    }
}
