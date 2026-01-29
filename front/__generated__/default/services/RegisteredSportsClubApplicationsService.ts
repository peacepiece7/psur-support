/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListRegSportsClubApplicationResponse } from '../models/ApiResponseListRegSportsClubApplicationResponse';
import type { ApiResponseRegSportsClubApplicationResponse } from '../models/ApiResponseRegSportsClubApplicationResponse';
import type { RegSportsClubApplicationCreateRequest } from '../models/RegSportsClubApplicationCreateRequest';
import type { RegSportsClubApplicationStatusUpdateRequest } from '../models/RegSportsClubApplicationStatusUpdateRequest';
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
     * Update application status
     * Updates the status of an application.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static updateStatus({
        applyId,
        requestBody,
    }: {
        /**
         * Application info ID
         */
        applyId: number,
        requestBody: RegSportsClubApplicationStatusUpdateRequest,
    }): CancelablePromise<ApiResponseRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'PATCH',
            url: '/reg-sports-club-applications/{applyId}/status',
            path: {
                'applyId': applyId,
            },
            body: requestBody,
            mediaType: 'application/json',
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
