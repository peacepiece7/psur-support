package com.service.demo.domain.commoncode.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeCreateRequest;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupListResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupSummaryResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeResponse;
import com.service.demo.domain.commoncode.service.CommonCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Common Codes", description = "공통 코드/그룹 조회")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    public CommonCodeController(CommonCodeService commonCodeService) {
        this.commonCodeService = commonCodeService;
    }

    @Operation(summary = "공통 코드 트리 조회", description = "공통 코드 그룹 트리(최대 3레벨)를 조회합니다.")
    @GetMapping("/{groupCode}/tree")
    public ApiResponse<CommonCodeGroupResponse> getTree(
            @Parameter(description = "루트 그룹 코드", example = "REGION") @PathVariable String groupCode,
            @Parameter(description = "조회 깊이(1-3)") Integer depth,
            @Parameter(description = "코드 포함 여부") Boolean includeCodes,
            @Parameter(description = "비활성 코드/그룹 포함 여부") Boolean includeInactive) {
        return ApiResponse.ok(commonCodeService.getGroupTree(
                groupCode,
                depth,
                includeCodes == null || includeCodes,
                Boolean.TRUE.equals(includeInactive)
        ));
    }

    @Operation(summary = "공통 코드 그룹 목록", description = "공통 코드 그룹 목록을 조회합니다.")
    @GetMapping("/groups")
    public ApiResponse<CommonCodeGroupListResponse> listGroups(
            @Parameter(description = "그룹명 또는 코드로 검색") String name,
            @Parameter(description = "비활성 그룹 포함 여부") Boolean includeInactive,
            @Parameter(description = "정렬 기준 (groupCode, groupName, level, sortOrder, id, parentGroupId)")
            String sortBy,
            @Parameter(description = "정렬 방향 (asc, desc)") String sortDir,
            @Parameter(description = "페이지 오프셋") Integer offset,
            @Parameter(description = "페이지 크기 (최대 200)") Integer limit) {
        return ApiResponse.ok(commonCodeService.listGroups(
                name,
                Boolean.TRUE.equals(includeInactive),
                sortBy,
                sortDir,
                offset == null ? 0 : offset,
                limit == null ? 20 : limit
        ));
    }

    @Operation(summary = "루트 공통 코드 그룹 목록", description = "최상위 공통 코드 그룹 목록을 조회합니다.")
    @GetMapping("/groups/root")
    public ApiResponse<List<CommonCodeGroupSummaryResponse>> listRootGroups() {
        return ApiResponse.ok(commonCodeService.listRootGroups());
    }

    @Operation(summary = "공통 코드 생성", description = "공통 코드 그룹 하위에 코드를 생성합니다.")
    @PostMapping
    public ApiResponse<CommonCodeResponse> createCode(@RequestBody CommonCodeCreateRequest request) {
        return ApiResponse.ok(commonCodeService.createCode(request));
    }

    @Operation(summary = "공통 코드 삭제", description = "공통 코드를 소프트 삭제합니다.")
    @DeleteMapping("/{groupCode}/codes/{code}")
    public ApiResponse<Void> deleteCode(@PathVariable String groupCode, @PathVariable String code) {
        commonCodeService.deleteCode(groupCode, code);
        return ApiResponse.ok(null);
    }

}
