/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListUserListResponse } from '../models/ApiResponseListUserListResponse';
import type { ApiResponseMeDetailResponse } from '../models/ApiResponseMeDetailResponse';
import type { ApiResponseUserResponse } from '../models/ApiResponseUserResponse';
import type { UpdateProfileRequest } from '../models/UpdateProfileRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UsersService {
    /**
     * Get my profile
     * Returns logged-in user's profile
     * @returns ApiResponseUserResponse OK
     * @throws ApiError
     */
    public static me(): CancelablePromise<ApiResponseUserResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/me',
        });
    }
    /**
     * Update my profile
     * Updates logged-in user's profile
     * @returns ApiResponseUserResponse OK
     * @throws ApiError
     */
    public static update({
        requestBody,
    }: {
        requestBody: UpdateProfileRequest,
    }): CancelablePromise<ApiResponseUserResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/users/me',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * List users
     * Returns users list
     * @returns ApiResponseListUserListResponse OK
     * @throws ApiError
     */
    public static list({
        includeInactive,
    }: {
        /**
         * Include inactive users
         */
        includeInactive?: boolean,
    }): CancelablePromise<ApiResponseListUserListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users',
            query: {
                'includeInactive': includeInactive,
            },
        });
    }
    /**
     * Get my profile detail
     * Returns user profile with roles and clubs
     * @returns ApiResponseMeDetailResponse OK
     * @throws ApiError
     */
    public static meDetail(): CancelablePromise<ApiResponseMeDetailResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/me/detail',
        });
    }
}
