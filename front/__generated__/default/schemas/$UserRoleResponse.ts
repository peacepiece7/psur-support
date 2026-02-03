/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserRoleResponse = {
    description: `Response payload`,
    properties: {
        id: {
            type: 'number',
            description: `User role ID`,
            format: 'int64',
        },
        userId: {
            type: 'number',
            description: `User ID`,
            format: 'int64',
        },
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
        isActive: {
            type: 'boolean',
            description: `User role active flag`,
        },
        assignedAt: {
            type: 'string',
            description: `Assigned at`,
            format: 'date-time',
        },
        updatedAt: {
            type: 'string',
            description: `Updated at`,
            format: 'date-time',
        },
        deletedAt: {
            type: 'string',
            description: `Deleted at`,
            format: 'date-time',
        },
    },
} as const;
