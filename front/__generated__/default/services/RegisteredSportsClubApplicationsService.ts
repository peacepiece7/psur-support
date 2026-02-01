/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListRegSportsClubApplicationResponse } from '../models/ApiResponseListRegSportsClubApplicationResponse';
import type { ApiResponseRegSportsClubApplicationResponse } from '../models/ApiResponseRegSportsClubApplicationResponse';
import type { ApiResponseVoid } from '../models/ApiResponseVoid';
import type { RegSportsClubApplicationCreateRequest } from '../models/RegSportsClubApplicationCreateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class RegisteredSportsClubApplicationsService {
    /**
     * List applications
     * Returns all registered sports club applications.
     * @returns ApiResponseListRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static list1(): CancelablePromise<ApiResponseListRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/reg-sports-club-applications',
        });
    }
    /**
     * Create application
     * Creates a registered sports club application.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static create1({
        requestBody,
    }: {
        requestBody: RegSportsClubApplicationCreateRequest,
    }): CancelablePromise<ApiResponseRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/reg-sports-club-applications',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Save application
     * Saves application data without state transition.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static save({
        applyId,
    }: {
        /**
         * Application info ID
         */
        applyId: number,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/reg-sports-club-applications/{applyId}/save',
            path: {
                'applyId': applyId,
            },
        });
    }
    /**
     * Apply application
     * Applies application data and triggers BPM transition.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static apply({
        applyId,
    }: {
        /**
         * Application info ID
         */
        applyId: number,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/reg-sports-club-applications/{applyId}/apply',
            path: {
                'applyId': applyId,
            },
        });
    }
    /**
     * Get application
     * Returns a registered sports club application by ID.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static get1({
        applyId,
    }: {
        /**
         * Application info ID
         */
        applyId: number,
    }): CancelablePromise<ApiResponseRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/reg-sports-club-applications/{applyId}',
            path: {
                'applyId': applyId,
            },
        });
    }
}
