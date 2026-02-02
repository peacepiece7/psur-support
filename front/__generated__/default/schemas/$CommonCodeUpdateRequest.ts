/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeUpdateRequest = {
    properties: {
        groupCode: {
            type: 'string',
            description: `Group code`,
        },
        code: {
            type: 'string',
            description: `Code value`,
        },
        codeName: {
            type: 'string',
            description: `Code display name`,
        },
        childGroupCode: {
            type: 'string',
            description: `Child group code mapped to this code`,
        },
        sortOrder: {
            type: 'number',
            description: `Sort order`,
            format: 'int32',
        },
        description: {
            type: 'string',
            description: `Code description`,
        },
        isActive: {
            type: 'boolean',
            description: `Is active`,
        },
    },
} as const;
