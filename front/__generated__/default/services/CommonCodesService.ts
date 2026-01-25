/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseCommonCodeGroupListResponse } from '../models/ApiResponseCommonCodeGroupListResponse';
import type { ApiResponseCommonCodeGroupResponse } from '../models/ApiResponseCommonCodeGroupResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CommonCodesService {
    /**
     * Get common code tree
     * Returns a group tree (up to 3 levels) with codes.
     * @returns ApiResponseCommonCodeGroupResponse OK
     * @throws ApiError
     */
    public static getTree({
        groupCode,
        depth,
        includeCodes,
    }: {
        /**
         * Root group code
         */
        groupCode: string,
        /**
         * Depth (1-3)
         */
        depth: number,
        /**
         * Include codes in response
         */
        includeCodes: boolean,
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
            },
        });
    }
    /**
     * List common code groups
     * Returns all active common code groups for lookup.
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
         * Search by group name or code
         */
        name: string,
        /**
         * Include inactive groups
         */
        includeInactive: boolean,
        /**
         * Sort by (groupCode, groupName, level, sortOrder, id, parentGroupId)
         */
        sortBy: string,
        /**
         * Sort direction (asc, desc)
         */
        sortDir: string,
        /**
         * Offset for pagination
         */
        offset: number,
        /**
         * Limit for pagination (max 200)
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
}
