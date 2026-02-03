/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserListResponse = {
    description: `Response payload`,
    properties: {
        id: {
            type: 'number',
            description: `User ID`,
            format: 'int64',
        },
        loginId: {
            type: 'string',
            description: `Login ID`,
        },
        username: {
            type: 'string',
            description: `User name`,
        },
        email: {
            type: 'string',
            description: `Email`,
        },
        telno: {
            type: 'string',
            description: `Phone number`,
        },
        status: {
            type: 'string',
            description: `Status`,
        },
        isActive: {
            type: 'boolean',
            description: `Active flag`,
        },
        lastLoginAt: {
            type: 'string',
            description: `Last login time`,
            format: 'date-time',
        },
        createdAt: {
            type: 'string',
            description: `Created time`,
            format: 'date-time',
        },
    },
} as const;
