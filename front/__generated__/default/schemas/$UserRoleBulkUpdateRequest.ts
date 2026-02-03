/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserRoleBulkUpdateRequest = {
    properties: {
        userId: {
            type: 'number',
            description: `User ID`,
            isRequired: true,
            format: 'int64',
        },
        roleIds: {
            type: 'array',
            contains: {
                type: 'number',
                description: `Role IDs to keep/assign`,
                format: 'int64',
            },
            isRequired: true,
        },
    },
} as const;
