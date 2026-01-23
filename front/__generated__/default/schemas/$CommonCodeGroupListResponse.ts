/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $CommonCodeGroupListResponse = {
    description: `Paged common code group response`,
    properties: {
        items: {
            type: 'array',
            contains: {
                type: 'CommonCodeGroupSummaryResponse',
            },
        },
        nextOffset: {
            type: 'number',
            description: `Next offset for infinite scroll`,
            format: 'int32',
        },
        hasNext: {
            type: 'boolean',
            description: `Whether more items are available`,
        },
        limit: {
            type: 'number',
            description: `Applied limit size`,
            format: 'int32',
        },
    },
} as const;
