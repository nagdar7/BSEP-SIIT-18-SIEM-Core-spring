import { BaseEntity } from './../../shared';

export const enum ALARM_STATUS {
    'ACTIVE',
    'NOT_ACTIVE'
}

export class Alarm implements BaseEntity {
    constructor(
        public id?: string,
        public name?: string,
        public status?: ALARM_STATUS,
    ) {
    }
}
