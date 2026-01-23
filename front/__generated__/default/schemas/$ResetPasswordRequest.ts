/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $ResetPasswordRequest = {
    properties: {
        currentPassword: {
            type: 'string',
            description: `Current password`,
            isRequired: true,
        },
        newPassword: {
            type: 'string',
            description: `New password`,
            isRequired: true,
            maxLength: 100,
            minLength: 8,
        },
    },
} as const;
