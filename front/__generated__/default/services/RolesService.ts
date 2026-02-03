/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListRoleResponse } from '../models/ApiResponseListRoleResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class RolesService {
    /**
     * List roles
     * Returns all roles
     * @returns ApiResponseListRoleResponse OK
     * @throws ApiError
     */
    public static list3({
        includeInactive,
    }: {
        /**
         * Include inactive roles
         */
        includeInactive: boolean,
    }): CancelablePromise<ApiResponseListRoleResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/roles',
            query: {
                'includeInactive': includeInactive,
            },
        });
    }
}
