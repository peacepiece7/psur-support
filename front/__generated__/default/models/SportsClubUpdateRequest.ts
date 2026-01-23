/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type SportsClubUpdateRequest = {
    /**
     * Club name
     */
    name: string;
    /**
     * Club location
     */
    location?: string;
    /**
     * Representative name
     */
    representativeName?: string;
    /**
     * Representative phone number
     */
    representativeTelno?: string;
    /**
     * Club number
     */
    clubNo?: string;
    /**
     * Business registration number
     */
    businessNo?: string;
    /**
     * Common code ID for club role
     */
    clubRoleCodeId?: number;
    /**
     * Club active flag
     */
    isActive?: boolean;
    /**
     * Sports category IDs
     */
    categoryIds?: Array<number>;
};

