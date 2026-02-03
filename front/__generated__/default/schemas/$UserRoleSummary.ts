/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserRoleSummary = {
    description: `User roles`,
    properties: {
        roleId: {
            type: 'number',
            description: `Role ID`,
            format: 'int64',
        },
        roleCode: {
            type: 'string',
            description: `Role code`,
        },
        roleName: {
            type: 'string',
            description: `Role name`,
        },
        roleType: {
            type: 'string',
            description: `Role type`,
        },
    },
} as const;
