/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $UpdateProfileRequest = {
    properties: {
        username: {
            type: 'string',
            description: `User name`,
            isRequired: true,
            maxLength: 100,
            minLength: 2,
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
