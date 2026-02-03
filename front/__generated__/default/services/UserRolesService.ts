/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListUserRoleResponse } from '../models/ApiResponseListUserRoleResponse';
import type { ApiResponseUserRoleResponse } from '../models/ApiResponseUserRoleResponse';
import type { ApiResponseVoid } from '../models/ApiResponseVoid';
import type { UserRoleAssignRequest } from '../models/UserRoleAssignRequest';
import type { UserRoleBulkUpdateRequest } from '../models/UserRoleBulkUpdateRequest';
import type { UserRoleUpdateRequest } from '../models/UserRoleUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UserRolesService {
    /**
     * List user roles
     * Returns user role assignments
     * @returns ApiResponseListUserRoleResponse OK
     * @throws ApiError
     */
    public static list1({
        userId,
        includeInactive,
    }: {
        /**
         * User ID
         */
        userId?: number,
        /**
         * Include inactive user roles
         */
        includeInactive?: boolean,
    }): CancelablePromise<ApiResponseListUserRoleResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/user-roles',
            query: {
                'userId': userId,
                'includeInactive': includeInactive,
            },
        });
    }
    /**
     * Update user role
     * Soft deletes old role and assigns new role
     * @returns ApiResponseUserRoleResponse OK
     * @throws ApiError
     */
    public static update1({
        requestBody,
    }: {
        requestBody: UserRoleUpdateRequest,
    }): CancelablePromise<ApiResponseUserRoleResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/user-roles',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Assign role to user
     * Assigns a role to a user (upsert)
     * @returns ApiResponseUserRoleResponse OK
     * @throws ApiError
     */
    public static assign({
        requestBody,
    }: {
        requestBody: UserRoleAssignRequest,
    }): CancelablePromise<ApiResponseUserRoleResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/user-roles',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Delete user role
     * Soft deletes a user role assignment
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static delete({
        userId,
        roleId,
    }: {
        /**
         * User ID
         */
        userId: number,
        /**
         * Role ID
         */
        roleId: number,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/user-roles',
            query: {
                'userId': userId,
                'roleId': roleId,
            },
        });
    }
    /**
     * Bulk update user roles
     * Replaces user roles with given list
     * @returns ApiResponseListUserRoleResponse OK
     * @throws ApiError
     */
    public static bulkUpdate({
        requestBody,
    }: {
        requestBody: UserRoleBulkUpdateRequest,
    }): CancelablePromise<ApiResponseListUserRoleResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/user-roles/bulk',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
