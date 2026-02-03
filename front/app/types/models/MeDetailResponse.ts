/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SportsClubSummary } from './SportsClubSummary';
import type { UserResponse } from './UserResponse';
import type { UserRoleSummary } from './UserRoleSummary';
/**
 * Response payload
 */
export type MeDetailResponse = {
    user?: UserResponse;
    /**
     * User roles
     */
    userRoles?: Array<UserRoleSummary>;
    /**
     * Sports clubs owned by user
     */
    sportsClubList?: Array<SportsClubSummary>;
};

