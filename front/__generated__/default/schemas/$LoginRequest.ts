/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $LoginRequest = {
    properties: {
        loginId: {
            type: 'string',
            description: `User login ID`,
            isRequired: true,
        },
        password: {
            type: 'string',
            description: `User password`,
            isRequired: true,
        },
    },
} as const;
