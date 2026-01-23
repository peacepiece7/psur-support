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

@Tag(name = "Sports Clubs", description = "Sports club management")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/sports-clubs")
public class SportsClubController {

    private final SportsClubService sportsClubService;

    public SportsClubController(SportsClubService sportsClubService) {
        this.sportsClubService = sportsClubService;
    }

    @Operation(summary = "Create sports club", description = "Creates a sports club with optional categories.")
    @PostMapping
    public ApiResponse<SportsClubResponse> create(@Valid @RequestBody SportsClubCreateRequest req) {
        return ApiResponse.ok(sportsClubService.create(req));
    }

    @Operation(summary = "Get sports club", description = "Returns a sports club by ID.")
    @GetMapping("/{id}")
    public ApiResponse<SportsClubResponse> get(
            @Parameter(description = "Sports club ID", example = "1") @PathVariable Long id) {
        return ApiResponse.ok(sportsClubService.getById(id));
    }

    @Operation(summary = "List sports clubs", description = "Returns all sports clubs.")
    @GetMapping
    public ApiResponse<List<SportsClubListResponse>> list() {
        return ApiResponse.ok(sportsClubService.list());
    }

    @Operation(summary = "Update sports club", description = "Updates a sports club and its categories.")
    @PutMapping("/{id}")
    public ApiResponse<SportsClubResponse> update(
            @Parameter(description = "Sports club ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody SportsClubUpdateRequest req) {
        return ApiResponse.ok(sportsClubService.update(id, req));
    }

    @Operation(summary = "Delete sports club", description = "Soft deletes a sports club.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@Parameter(description = "Sports club ID", example = "1") @PathVariable Long id) {
        sportsClubService.delete(id);
        return ApiResponse.ok(null);
    }
}
