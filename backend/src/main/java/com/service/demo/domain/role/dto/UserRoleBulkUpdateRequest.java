package com.service.demo.domain.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleBulkUpdateRequest {
    @Schema(description = "User ID", example = "10")
    @NotNull
    private Long userId;

    @Schema(description = "Role IDs to keep/assign", example = "[2,3,4]")
    @NotNull
    private List<Long> roleIds;
}
