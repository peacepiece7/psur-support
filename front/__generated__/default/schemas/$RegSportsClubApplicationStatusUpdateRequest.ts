/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RegSportsClubApplicationStatusUpdateRequest = {
    properties: {
        statusCodeId: {
            type: 'number',
            description: `Application status code ID`,
            isRequired: true,
            format: 'int64',
        },
        handlerName: {
            type: 'string',
            description: `Handler name`,
        },
        handlerTelno: {
            type: 'string',
            description: `Handler phone number`,
        },
        handlerEmail: {
            type: 'string',
            description: `Handler email`,
        },
        memo: {
            type: 'string',
            description: `Handling memo`,
        },
    },
} as const;
