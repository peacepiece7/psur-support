/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $MeDetailResponse = {
    description: `Response payload`,
    properties: {
        user: {
            type: 'UserResponse',
        },
        userRoles: {
            type: 'array',
            contains: {
                type: 'UserRoleSummary',
            },
        },
        sportsClubList: {
            type: 'array',
            contains: {
                type: 'SportsClubSummary',
            },
        },
    },
} as const;
