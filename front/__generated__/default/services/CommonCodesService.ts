/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
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
    }: {
        /**
         * Root group code
         */
        groupCode: string,
    }): CancelablePromise<ApiResponseCommonCodeGroupResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/common-codes/{groupCode}/tree',
            path: {
                'groupCode': groupCode,
            },
        });
    }
}
