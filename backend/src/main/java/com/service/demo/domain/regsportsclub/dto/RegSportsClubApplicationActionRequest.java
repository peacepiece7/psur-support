package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationActionRequest {
    @Schema(description = "Action key (approve/reject/review/etc)", example = "approve")
    @NotBlank
    private String action;

    @Schema(description = "Action payload (optional)")
    private Map<String, Object> payload;
}
