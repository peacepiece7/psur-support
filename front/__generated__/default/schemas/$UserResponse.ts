/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UserResponse = {
    description: `Response payload`,
    properties: {
        id: {
            type: 'number',
            description: `User ID`,
            format: 'int64',
        },
        loginId: {
            type: 'string',
            description: `User login ID`,
        },
        username: {
            type: 'string',
            description: `User name`,
        },
        email: {
            type: 'string',
            description: `Email address`,
        },
        telno: {
            type: 'string',
            description: `Phone number`,
        },
    },
} as const;
