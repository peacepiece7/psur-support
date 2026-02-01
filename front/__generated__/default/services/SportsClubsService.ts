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
     * 체육동호회 조회
     * 체육동호회 ID로 정보를 조회합니다.
     * @returns ApiResponseSportsClubResponse OK
     * @throws ApiError
     */
    public static get({
        id,
    }: {
        /**
         * 체육동호회 ID
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
     * 체육동호회 수정
     * 체육동호회 정보를 수정합니다.
     * @returns ApiResponseSportsClubResponse OK
     * @throws ApiError
     */
    public static update1({
        id,
        requestBody,
    }: {
        /**
         * 체육동호회 ID
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
     * 체육동호회 삭제
     * 체육동호회를 소프트 삭제합니다.
     * @returns ApiResponseVoid OK
     * @throws ApiError
     */
    public static delete({
        id,
    }: {
        /**
         * 체육동호회 ID
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
     * 체육동호회 목록
     * 체육동호회 목록을 조회합니다.
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
     * 체육동호회 생성
     * 체육동호회를 생성합니다.
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
