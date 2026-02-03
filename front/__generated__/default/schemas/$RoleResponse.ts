/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RoleResponse = {
    description: `Response payload`,
    properties: {
        id: {
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
        description: {
            type: 'string',
            description: `Role description`,
        },
        isActive: {
            type: 'boolean',
            description: `Role active flag`,
        },
    },
} as const;
