/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RegisterRequest = {
    properties: {
        loginId: {
            type: 'string',
            description: `User login ID`,
            isRequired: true,
            maxLength: 50,
            minLength: 4,
        },
        username: {
            type: 'string',
            description: `User name`,
            isRequired: true,
            maxLength: 100,
            minLength: 2,
        },
        password: {
            type: 'string',
            description: `User password`,
            isRequired: true,
            maxLength: 100,
            minLength: 8,
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
