import { BaseEntity } from './../../shared';

export class AlarmRule implements BaseEntity {
    constructor(
        public id?: string,
        public name?: string,
        public rule?: string,
    ) {
    }
}
