package com.service.demo.domain.regsportsclub.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationCreateRequest;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationStatusUpdateRequest;
import com.service.demo.domain.regsportsclub.service.RegSportsClubApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Registered Sports Club Applications", description = "Registered sports club application flow")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/reg-sports-club-applications")
public class RegSportsClubApplicationController {
    private final RegSportsClubApplicationService regSportsClubApplicationService;

    public RegSportsClubApplicationController(RegSportsClubApplicationService regSportsClubApplicationService) {
        this.regSportsClubApplicationService = regSportsClubApplicationService;
    }

    @Operation(summary = "Create application", description = "Creates a registered sports club application.")
    @PostMapping
    public ApiResponse<RegSportsClubApplicationResponse> create(
            @Valid @RequestBody RegSportsClubApplicationCreateRequest req) {
        return ApiResponse.ok(regSportsClubApplicationService.create(req));
    }

    @Operation(summary = "Update application status", description = "Updates the status of an application.")
    @PatchMapping("/{applyId}/status")
    public ApiResponse<RegSportsClubApplicationResponse> updateStatus(
            @Parameter(description = "Application info ID", example = "1") @PathVariable Long applyId,
            @Valid @RequestBody RegSportsClubApplicationStatusUpdateRequest req) {
        return ApiResponse.ok(regSportsClubApplicationService.updateStatus(applyId, req));
    }

    @Operation(summary = "Get application", description = "Returns a registered sports club application by ID.")
    @GetMapping("/{applyId}")
    public ApiResponse<RegSportsClubApplicationResponse> get(
            @Parameter(description = "Application info ID", example = "1") @PathVariable Long applyId) {
        return ApiResponse.ok(regSportsClubApplicationService.getByApplyId(applyId));
    }

    @Operation(summary = "List applications", description = "Returns all registered sports club applications.")
    @GetMapping
    public ApiResponse<List<RegSportsClubApplicationResponse>> list() {
        return ApiResponse.ok(regSportsClubApplicationService.list());
    }
}
