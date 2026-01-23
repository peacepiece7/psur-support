/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeGroupResponse = {
    description: `Response payload`,
    properties: {
        groupCode: {
            type: 'string',
            description: `Group code`,
        },
        groupName: {
            type: 'string',
            description: `Group name`,
        },
        description: {
            type: 'string',
            description: `Group description`,
        },
        level: {
            type: 'number',
            description: `Group level (1-3)`,
            format: 'int32',
        },
        sortOrder: {
            type: 'number',
            description: `Sort order`,
            format: 'int32',
        },
        codes: {
            type: 'array',
            contains: {
                type: 'CommonCodeResponse',
            },
        },
        children: {
            type: 'array',
            contains: {
                type: 'CommonCodeGroupResponse',
            },
        },
    },
} as const;
