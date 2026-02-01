package com.service.demo.domain.regsportsclub.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationCreateRequest;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.service.RegSportsClubApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequiredArgsConstructor
public class RegSportsClubApplicationController {
    private final RegSportsClubApplicationService regSportsClubApplicationService;

    @Operation(summary = "Create application", description = "Creates a registered sports club application.")
    @PostMapping
    public ApiResponse<RegSportsClubApplicationResponse> create(
            @Valid @RequestBody RegSportsClubApplicationCreateRequest req) {
        return ApiResponse.ok(regSportsClubApplicationService.create(req));
    }

    @Operation(summary = "Save application", description = "Saves application data without state transition.")
    @PostMapping("/{applyId}/save")
    public ApiResponse<Void> save(
            @Parameter(description = "Application info ID", example = "1") @PathVariable Long applyId) {
        regSportsClubApplicationService.save(applyId);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "Apply application", description = "Applies application data and triggers BPM transition.")
    @PostMapping("/{applyId}/apply")
    public ApiResponse<Void> apply(
            @Parameter(description = "Application info ID", example = "1") @PathVariable Long applyId) {
        regSportsClubApplicationService.apply(applyId);
        return ApiResponse.ok(null);
    }

    // @TODO: {applyId}/receipt, {applyId}/reject, {applyId}/review, {applyId}/approve endpoints to be implemented

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
