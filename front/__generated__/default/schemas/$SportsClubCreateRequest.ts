/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $SportsClubCreateRequest = {
    properties: {
        name: {
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
        clubNo: {
            type: 'string',
            description: `Club number`,
        },
        businessNo: {
            type: 'string',
            description: `Business registration number`,
        },
        clubRoleCodeId: {
            type: 'number',
            description: `Common code ID for club role`,
            format: 'int64',
        },
        categoryIds: {
            type: 'array',
            contains: {
                type: 'number',
                description: `Operating sport common code IDs`,
                format: 'int64',
            },
        },
    },
} as const;
