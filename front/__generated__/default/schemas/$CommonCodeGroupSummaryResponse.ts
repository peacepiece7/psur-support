/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeGroupSummaryResponse = {
    description: `Common code group summary`,
    properties: {
        id: {
            type: 'number',
            format: 'int64',
        },
        groupCode: {
            type: 'string',
        },
        groupName: {
            type: 'string',
        },
        parentGroupId: {
            type: 'number',
            format: 'int64',
        },
        level: {
            type: 'number',
            format: 'int32',
        },
        sortOrder: {
            type: 'number',
            format: 'int32',
        },
        description: {
            type: 'string',
        },
    },
} as const;
