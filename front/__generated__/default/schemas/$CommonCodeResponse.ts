/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeResponse = {
    description: `Codes under this group`,
    properties: {
        code: {
            type: 'string',
            description: `Code value`,
        },
        codeName: {
            type: 'string',
            description: `Code display name`,
        },
        description: {
            type: 'string',
            description: `Code description`,
        },
        sortOrder: {
            type: 'number',
            description: `Sort order`,
            format: 'int32',
        },
        groupCode: {
            type: 'string',
            description: `Child group code mapped to this code`,
        },
    },
} as const;
