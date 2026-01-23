/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeGroupSummaryResponse = {
    description: `Common code group summary`,
    properties: {
        id: {
            type: 'number',
            description: `Group ID`,
            format: 'int64',
        },
        groupCode: {
            type: 'string',
            description: `Group code`,
        },
        groupName: {
            type: 'string',
            description: `Group name`,
        },
        parentGroupId: {
            type: 'number',
            description: `Parent group ID`,
            format: 'int64',
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
        description: {
            type: 'string',
            description: `Group description`,
        },
    },
} as const;
