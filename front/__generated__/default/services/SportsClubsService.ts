/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListSportsClubListResponse } from '../models/ApiResponseListSportsClubListResponse';
import type { ApiResponseSportsClubResponse } from '../models/ApiResponseSportsClubResponse';
import type { ApiResponseVoid } from '../models/ApiResponseVoid';
import type { SportsClubCreateRequest } from '../models/SportsClubCreateRequest';
import type { SportsClubUpdateRequest } from '../models/SportsClubUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SportsClubsService {
    /**
     * Get sports club
     * Returns a sports club by ID.
     * @returns ApiResponseSportsClubResponse OK
     * @throws ApiError
     */
    public static get({
        id,
    }: {
        /**
         * Sports club ID
         */
        id: number,
    }): CancelablePromise<ApiResponseSportsClubResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/sports-clubs/{id}',
            path: {
                'id': id,
            },
        });
    }
    /**
     * Update sports club
     * Updates a sports club and its categories.
     * @returns ApiResponseSportsClubResponse OK
     * @throws ApiError
     */
    public static update1({
        id,
        requestBody,
    }: {
        /**
         * Sports club ID
         */
        id: number,
        requestBody: SportsClubUpdateRequest,
    }): CancelablePromise<ApiResponseSportsClubResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/sports-clubs/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Delete sports club
     * Soft deletes a sports club.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static delete({
        id,
    }: {
        /**
         * Sports club ID
         */
        id: number,
    }): CancelablePromise<ApiResponseVoid> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/sports-clubs/{id}',
            path: {
                'id': id,
            },
        });
    }
    /**
     * List sports clubs
     * Returns all sports clubs.
     * @returns ApiResponseListSportsClubListResponse OK
     * @throws ApiError
     */
    public static list(): CancelablePromise<ApiResponseListSportsClubListResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/sports-clubs',
        });
    }
    /**
     * Create sports club
     * Creates a sports club with optional categories.
     * @returns ApiResponseSportsClubResponse OK
     * @throws ApiError
     */
    public static create({
        requestBody,
    }: {
        requestBody: SportsClubCreateRequest,
    }): CancelablePromise<ApiResponseSportsClubResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/sports-clubs',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
