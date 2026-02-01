/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListRegSportsClubApplicationResponse } from '../models/ApiResponseListRegSportsClubApplicationResponse';
import type { ApiResponseRegSportsClubApplicationResponse } from '../models/ApiResponseRegSportsClubApplicationResponse';
import type { RegSportsClubApplicationUpsertRequest } from '../models/RegSportsClubApplicationUpsertRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class RegisteredSportsClubApplicationsService {
    /**
     * 저장
     * 상태 전이 없이 신청 데이터를 저장합니다.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static save({
        requestBody,
    }: {
        requestBody: RegSportsClubApplicationUpsertRequest,
    }): CancelablePromise<ApiResponseRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/reg-sports-club-applications/save',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 신청
     * 신청 데이터를 반영하고 BPM 상태 전이를 수행합니다.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static apply({
        requestBody,
    }: {
        requestBody: RegSportsClubApplicationUpsertRequest,
    }): CancelablePromise<ApiResponseRegSportsClubApplicationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/reg-sports-club-applications/apply',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 신청 목록
     * 등록 체육동호회 신청 목록을 조회합니다.
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
     * 신청 조회
     * 신청 ID로 등록 체육동호회 신청 정보를 조회합니다.
     * @returns ApiResponseRegSportsClubApplicationResponse OK
     * @throws ApiError
     */
    public static get1({
        applyId,
    }: {
        /**
         * 신청 ID
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
