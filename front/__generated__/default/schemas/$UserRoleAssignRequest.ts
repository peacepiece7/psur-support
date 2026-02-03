/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserRoleAssignRequest = {
    properties: {
        userId: {
            type: 'number',
            description: `User ID`,
            isRequired: true,
            format: 'int64',
        },
        roleId: {
            type: 'number',
            description: `Role ID`,
            isRequired: true,
            format: 'int64',
        },
    },
} as const;
