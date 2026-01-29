/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Response payload
 */
export type RegSportsClubApplicationResponse = {
    /**
     * Application info ID
     */
    applyId?: number;
    /**
     * Application detail ID
     */
    applicationId?: number;
    /**
     * Application status code
     */
    code?: string;
    /**
     * Application status code name
     */
    codeName?: string;
    /**
     * Applied at
     */
    appliedAt?: string;
    /**
     * Applicant name
     */
    applicantName?: string;
    /**
     * Applicant phone number
     */
    applicantTelno?: string;
    /**
     * Applicant email
     */
    applicantEmail?: string;
    /**
     * Club name
     */
    clubName?: string;
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
     * Business registration number
     */
    businessNo?: string;
    /**
     * Club role code ID
     */
    clubRoleCodeId?: number;
    /**
     * Operating sport parent code ID
     */
    operatingSportParentCodeId?: number;
    /**
     * Operating sport child code ID
     */
    operatingSportChildCodeId?: number;
    /**
     * Operating sport common code IDs
     */
    operatingSportCodeIds?: Array<number>;
    /**
     * Approved sports club ID
     */
    approvedClubId?: number;
};

