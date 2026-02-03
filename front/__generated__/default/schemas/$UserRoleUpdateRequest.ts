/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserRoleUpdateRequest = {
    properties: {
        userId: {
            type: 'number',
            description: `User ID`,
            isRequired: true,
            format: 'int64',
        },
        oldRoleId: {
            type: 'number',
            description: `Old role ID`,
            isRequired: true,
            format: 'int64',
        },
        newRoleId: {
            type: 'number',
            description: `New role ID`,
            isRequired: true,
            format: 'int64',
        },
    },
} as const;
