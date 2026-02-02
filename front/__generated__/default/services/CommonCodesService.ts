/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseCommonCodeGroupListResponse } from '../models/ApiResponseCommonCodeGroupListResponse';
import type { ApiResponseCommonCodeGroupResponse } from '../models/ApiResponseCommonCodeGroupResponse';
import type { ApiResponseCommonCodeResponse } from '../models/ApiResponseCommonCodeResponse';
import type { ApiResponseListCommonCodeGroupSummaryResponse } from '../models/ApiResponseListCommonCodeGroupSummaryResponse';
import type { ApiResponseVoid } from '../models/ApiResponseVoid';
import type { CommonCodeCreateRequest } from '../models/CommonCodeCreateRequest';
import type { CommonCodeUpdateRequest } from '../models/CommonCodeUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CommonCodesService {
    /**
     * 공통 코드 수정
     * 공통 코드를 수정합니다.
     * @returns ApiResponseCommonCodeResponse OK
     * @throws ApiError
     */
    public static updateCode({
        requestBody,
    }: {
        requestBody: CommonCodeUpdateRequest,
    }): CancelablePromise<ApiResponseCommonCodeResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/common-codes',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 공통 코드 생성
     * 공통 코드 그룹 하위에 코드를 생성합니다.
     * @returns ApiResponseCommonCodeResponse OK
     * @throws ApiError
     */
    public static createCode({
        requestBody,
    }: {
        requestBody: CommonCodeCreateRequest,
    }): CancelablePromise<ApiResponseCommonCodeResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/common-codes',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 공통 코드 트리 조회
     * 공통 코드 그룹 트리(최대 3레벨)를 조회합니다.
     * @returns ApiResponseCommonCodeGroupResponse OK
     * @throws ApiError
     */
    public static getTree({
        groupCode,
        depth,
        includeCodes,
        includeInactive,
    }: {
        /**
         * 루트 그룹 코드
         */
        groupCode: string,
        /**
         * 조회 깊이(1-3)
         */
        depth: number,
        /**
         * 코드 포함 여부
         */
        includeCodes: boolean,
        /**
         * 비활성 코드/그룹 포함 여부
         */
        includeInactive: boolean,
    }): CancelablePromise<ApiResponseCommonCodeGroupResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/common-codes/{groupCode}/tree',
            path: {
                'groupCode': groupCode,
            },
            query: {
                'depth': depth,
                'includeCodes': includeCodes,
                'includeInactive': includeInactive,
            },
        });
    }
    /**
     * 공통 코드 그룹 목록
     * 공통 코드 그룹 목록을 조회합니다.
     * @returns ApiResponseCommonCodeGroupListResponse OK
     * @throws ApiError
     */
    public static listGroups({
        name,
        includeInactive,
        sortBy,
        sortDir,
        offset,
        limit,
    }: {
        /**
         * 그룹명 또는 코드로 검색
         */
        name: string,
        /**
         * 비활성 그룹 포함 여부
         */
        includeInactive: boolean,
        /**
         * 정렬 기준 (groupCode, groupName, level, sortOrder, id, parentGroupId)
         */
        sortBy: string,
        /**
         * 정렬 방향 (asc, desc)
         */
        sortDir: string,
        /**
         * 페이지 오프셋
         */
        offset: number,
        /**
         * 페이지 크기 (최대 200)
         */
        limit: number,
    }): CancelablePromise<ApiResponseCommonCodeGroupListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/common-codes/groups',
            query: {
                'name': name,
                'includeInactive': includeInactive,
                'sortBy': sortBy,
                'sortDir': sortDir,
                'offset': offset,
                'limit': limit,
            },
        });
    }
    /**
     * 루트 공통 코드 그룹 목록
     * 최상위 공통 코드 그룹 목록을 조회합니다.
     * @returns ApiResponseListCommonCodeGroupSummaryResponse OK
     * @throws ApiError
     */
    public static listRootGroups(): CancelablePromise<ApiResponseListCommonCodeGroupSummaryResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/common-codes/groups/root',
        });
    }
    /**
     * 공통 코드 삭제
     * 공통 코드를 소프트 삭제합니다.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static deleteCode({
        groupCode,
        code,
    }: {
        groupCode: string,
        code: string,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/common-codes/{groupCode}/codes/{code}',
            path: {
                'groupCode': groupCode,
                'code': code,
            },
        });
    }
}
