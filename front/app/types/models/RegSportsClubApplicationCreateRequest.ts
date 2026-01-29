/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type RegSportsClubApplicationCreateRequest = {
    /**
     * Application status code ID
     */
    statusCodeId?: number;
    /**
     * Application status code
     */
    statusCode?: string;
    /**
     * Applicant name
     */
    applicantName: string;
    /**
     * Applicant phone number
     */
    applicantTelno: string;
    /**
     * Applicant email
     */
    applicantEmail: string;
    /**
     * Club name
     */
    clubName: string;
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
};

