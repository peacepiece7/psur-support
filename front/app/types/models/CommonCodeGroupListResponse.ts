/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommonCodeGroupSummaryResponse } from './CommonCodeGroupSummaryResponse';
/**
 * Paged common code group response
 */
export type CommonCodeGroupListResponse = {
    /**
     * Result items
     */
    items?: Array<CommonCodeGroupSummaryResponse>;
    /**
     * Next offset for infinite scroll
     */
    nextOffset?: number;
    /**
     * Whether more items are available
     */
    hasNext?: boolean;
    /**
     * Applied limit size
     */
    limit?: number;
};

