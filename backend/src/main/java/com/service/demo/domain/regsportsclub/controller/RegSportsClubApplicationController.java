package com.service.demo.domain.regsportsclub.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationUpsertRequest;
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

@Tag(name = "Registered Sports Club Applications", description = "등록 체육동호회 신청 흐름")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/reg-sports-club-applications")
@RequiredArgsConstructor
public class RegSportsClubApplicationController {
    private final RegSportsClubApplicationService regSportsClubApplicationService;

    @Operation(summary = "저장", description = "상태 전이 없이 신청 데이터를 저장합니다.")
    @PostMapping("/save")
    public ApiResponse<RegSportsClubApplicationResponse> save(
            @Valid @RequestBody RegSportsClubApplicationUpsertRequest req) {
        return ApiResponse.ok(regSportsClubApplicationService.save(req));
    }

    @Operation(summary = "신청", description = "신청 데이터를 반영하고 BPM 상태 전이를 수행합니다.")
    @PostMapping("/apply")
    public ApiResponse<RegSportsClubApplicationResponse> apply(
            @Valid @RequestBody RegSportsClubApplicationUpsertRequest req) {
        return ApiResponse.ok(regSportsClubApplicationService.apply(req));
    }

    // @TODO: {applyId}/receipt, {applyId}/reject, {applyId}/review, {applyId}/approve endpoints to be implemented

    @Operation(summary = "신청 조회", description = "신청 ID로 등록 체육동호회 신청 정보를 조회합니다.")
    @GetMapping("/{applyId}")
    public ApiResponse<RegSportsClubApplicationResponse> get(
            @Parameter(description = "신청 ID", example = "1") @PathVariable Long applyId) {
        return ApiResponse.ok(regSportsClubApplicationService.getByApplyId(applyId));
    }

    @Operation(summary = "신청 목록", description = "등록 체육동호회 신청 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<RegSportsClubApplicationResponse>> list() {
        return ApiResponse.ok(regSportsClubApplicationService.list());
    }
}
