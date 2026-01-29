/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RegSportsClubApplicationResponse = {
    description: `Response payload`,
    properties: {
        applyId: {
            type: 'number',
            description: `Application info ID`,
            format: 'int64',
        },
        applicationId: {
            type: 'number',
            description: `Application detail ID`,
            format: 'int64',
        },
        code: {
            type: 'string',
            description: `Application status code`,
        },
        codeName: {
            type: 'string',
            description: `Application status code name`,
        },
        appliedAt: {
            type: 'string',
            description: `Applied at`,
            format: 'date-time',
        },
        applicantName: {
            type: 'string',
            description: `Applicant name`,
        },
        applicantTelno: {
            type: 'string',
            description: `Applicant phone number`,
        },
        applicantEmail: {
            type: 'string',
            description: `Applicant email`,
        },
        clubName: {
            type: 'string',
            description: `Club name`,
        },
        location: {
            type: 'string',
            description: `Club location`,
        },
        representativeName: {
            type: 'string',
            description: `Representative name`,
        },
        representativeTelno: {
            type: 'string',
            description: `Representative phone number`,
        },
        businessNo: {
            type: 'string',
            description: `Business registration number`,
        },
        clubRoleCodeId: {
            type: 'number',
            description: `Club role code ID`,
            format: 'int64',
        },
        operatingSportParentCodeId: {
            type: 'number',
            description: `Operating sport parent code ID`,
            format: 'int64',
        },
        operatingSportChildCodeId: {
            type: 'number',
            description: `Operating sport child code ID`,
            format: 'int64',
        },
        operatingSportCodeIds: {
            type: 'array',
            contains: {
                type: 'number',
                description: `Operating sport common code IDs`,
                format: 'int64',
            },
        },
        approvedClubId: {
            type: 'number',
            description: `Approved sports club ID`,
            format: 'int64',
        },
    },
} as const;
