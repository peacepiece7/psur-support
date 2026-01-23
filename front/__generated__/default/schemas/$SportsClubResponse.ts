/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $SportsClubResponse = {
    description: `Response payload`,
    properties: {
        id: {
            type: 'number',
            description: `Club ID`,
            format: 'int64',
        },
        name: {
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
        isActive: {
            type: 'boolean',
            description: `Club active flag`,
        },
        categoryIds: {
            type: 'array',
            contains: {
                type: 'number',
                description: `Sports category IDs`,
                format: 'int64',
            },
        },
    },
} as const;
