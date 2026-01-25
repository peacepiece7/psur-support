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

@Tag(name = "Common Codes", description = "Common code group and code lookup")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    public CommonCodeController(CommonCodeService commonCodeService) {
        this.commonCodeService = commonCodeService;
    }

    @Operation(summary = "Get common code tree", description = "Returns a group tree (up to 3 levels) with codes.")
    @GetMapping("/{groupCode}/tree")
    public ApiResponse<CommonCodeGroupResponse> getTree(
            @Parameter(description = "Root group code", example = "REGION") @PathVariable String groupCode,
            @Parameter(description = "Depth (1-3)") Integer depth,
            @Parameter(description = "Include codes in response") Boolean includeCodes,
            @Parameter(description = "Include inactive codes and groups") Boolean includeInactive) {
        return ApiResponse.ok(commonCodeService.getGroupTree(
                groupCode,
                depth,
                includeCodes == null || includeCodes,
                Boolean.TRUE.equals(includeInactive)
        ));
    }

    @Operation(summary = "List common code groups", description = "Returns all active common code groups for lookup.")
    @GetMapping("/groups")
    public ApiResponse<CommonCodeGroupListResponse> listGroups(
            @Parameter(description = "Search by group name or code") String name,
            @Parameter(description = "Include inactive groups") Boolean includeInactive,
            @Parameter(description = "Sort by (groupCode, groupName, level, sortOrder, id, parentGroupId)")
            String sortBy,
            @Parameter(description = "Sort direction (asc, desc)") String sortDir,
            @Parameter(description = "Offset for pagination") Integer offset,
            @Parameter(description = "Limit for pagination (max 200)") Integer limit) {
        return ApiResponse.ok(commonCodeService.listGroups(
                name,
                Boolean.TRUE.equals(includeInactive),
                sortBy,
                sortDir,
                offset == null ? 0 : offset,
                limit == null ? 20 : limit
        ));
    }

    @Operation(summary = "List root common code groups", description = "Returns top-level common code groups.")
    @GetMapping("/groups/root")
    public ApiResponse<List<CommonCodeGroupSummaryResponse>> listRootGroups() {
        return ApiResponse.ok(commonCodeService.listRootGroups());
    }

    @Operation(summary = "Create common code", description = "Creates a common code under a group.")
    @PostMapping
    public ApiResponse<CommonCodeResponse> createCode(@RequestBody CommonCodeCreateRequest request) {
        return ApiResponse.ok(commonCodeService.createCode(request));
    }

    @Operation(summary = "Delete common code", description = "Soft deletes a common code.")
    @DeleteMapping("/{groupCode}/codes/{code}")
    public ApiResponse<Void> deleteCode(@PathVariable String groupCode, @PathVariable String code) {
        commonCodeService.deleteCode(groupCode, code);
        return ApiResponse.ok(null);
    }
}
