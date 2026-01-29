/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RegSportsClubApplicationCreateRequest = {
    properties: {
        statusCodeId: {
            type: 'number',
            description: `Application status code ID`,
            format: 'int64',
        },
        statusCode: {
            type: 'string',
            description: `Application status code`,
        },
        applicantName: {
            type: 'string',
            description: `Applicant name`,
            isRequired: true,
        },
        applicantTelno: {
            type: 'string',
            description: `Applicant phone number`,
            isRequired: true,
        },
        applicantEmail: {
            type: 'string',
            description: `Applicant email`,
            isRequired: true,
        },
        clubName: {
            type: 'string',
            description: `Club name`,
            isRequired: true,
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
    },
} as const;
