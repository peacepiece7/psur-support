/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $ApiResponseCommonCodeResponse = {
    description: `Standard API wrapper`,
    properties: {
        resultCode: {
            type: 'number',
            description: `Application-level result code`,
            format: 'int32',
        },
        resultMessage: {
            type: 'string',
            description: `Application-level result message`,
        },
        data: {
            type: 'CommonCodeResponse',
        },
    },
} as const;
