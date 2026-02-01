package com.service.demo.domain.sportsclub.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.sportsclub.dto.SportsClubCreateRequest;
import com.service.demo.domain.sportsclub.dto.SportsClubListResponse;
import com.service.demo.domain.sportsclub.dto.SportsClubResponse;
import com.service.demo.domain.sportsclub.dto.SportsClubUpdateRequest;
import com.service.demo.domain.sportsclub.service.SportsClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Sports Clubs", description = "체육동호회 관리")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/sports-clubs")
public class SportsClubController {

    private final SportsClubService sportsClubService;

    public SportsClubController(SportsClubService sportsClubService) {
        this.sportsClubService = sportsClubService;
    }

    @Operation(summary = "체육동호회 생성", description = "체육동호회를 생성합니다.")
    @PostMapping
    public ApiResponse<SportsClubResponse> create(@Valid @RequestBody SportsClubCreateRequest req) {
        return ApiResponse.ok(sportsClubService.create(req));
    }

    @Operation(summary = "체육동호회 조회", description = "체육동호회 ID로 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<SportsClubResponse> get(
            @Parameter(description = "체육동호회 ID", example = "1") @PathVariable Long id) {
        return ApiResponse.ok(sportsClubService.getById(id));
    }

    @Operation(summary = "체육동호회 목록", description = "체육동호회 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<SportsClubListResponse>> list() {
        return ApiResponse.ok(sportsClubService.list());
    }

    @Operation(summary = "체육동호회 수정", description = "체육동호회 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ApiResponse<SportsClubResponse> update(
            @Parameter(description = "체육동호회 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody SportsClubUpdateRequest req) {
        return ApiResponse.ok(sportsClubService.update(id, req));
    }

    @Operation(summary = "체육동호회 삭제", description = "체육동호회를 소프트 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@Parameter(description = "체육동호회 ID", example = "1") @PathVariable Long id) {
        sportsClubService.delete(id);
        return ApiResponse.ok(null);
    }
}
