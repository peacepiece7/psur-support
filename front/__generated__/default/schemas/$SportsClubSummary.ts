/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $SportsClubSummary = {
    description: `Sports clubs owned by user`,
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
        clubRoleCodeId: {
            type: 'number',
            description: `Common code ID for club role`,
            format: 'int64',
        },
    },
} as const;
