/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseLoginResponse } from '../models/ApiResponseLoginResponse';
import type { ApiResponseVoid } from '../models/ApiResponseVoid';
import type { LoginRequest } from '../models/LoginRequest';
import type { RegisterRequest } from '../models/RegisterRequest';
import type { ResetPasswordRequest } from '../models/ResetPasswordRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AuthService {
    /**
     * Register a new user
     * Creates a new user account and returns the profile.
     * @returns ApiResponseLoginResponse OK
     * @throws ApiError
     */
    public static register({
        requestBody,
    }: {
        requestBody: RegisterRequest,
    }): CancelablePromise<ApiResponseLoginResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/auth/register',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Reset password
     * Changes the password for the authenticated user.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static resetPassword({
        requestBody,
    }: {
        requestBody: ResetPasswordRequest,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/auth/password/reset',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Logout
     * Invalidates the current session.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static logout(): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/auth/logout',
        });
    }
    /**
     * Login
     * Validates credentials and creates a session.
     * @returns ApiResponseLoginResponse OK
     * @throws ApiError
     */
    public static login({
        requestBody,
    }: {
        requestBody: LoginRequest,
    }): CancelablePromise<ApiResponseLoginResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/auth/login',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
