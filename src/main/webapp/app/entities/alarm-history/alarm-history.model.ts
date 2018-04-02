import { BaseEntity } from './../../shared';

export class AlarmHistory implements BaseEntity {
    constructor(
        public id?: string,
        public date?: any,
    ) {
    }
}
