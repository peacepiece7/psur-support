/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseUserResponse } from '../models/ApiResponseUserResponse';
import type { UpdateProfileRequest } from '../models/UpdateProfileRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UsersService {
    /**
     * Get my profile
     * Returns the authenticated user's profile.
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
     * Updates the authenticated user's profile.
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
}
