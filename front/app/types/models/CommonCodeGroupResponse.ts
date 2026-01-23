/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommonCodeResponse } from './CommonCodeResponse';
/**
 * Response payload
 */
export type CommonCodeGroupResponse = {
    /**
     * Group code
     */
    groupCode?: string;
    /**
     * Group name
     */
    groupName?: string;
    /**
     * Group description
     */
    description?: string;
    /**
     * Group level (1-3)
     */
    level?: number;
    /**
     * Sort order
     */
    sortOrder?: number;
    /**
     * Codes under this group
     */
    codes?: Array<CommonCodeResponse>;
    /**
     * Child groups
     */
    children?: Array<CommonCodeGroupResponse>;
};

