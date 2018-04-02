import { BaseEntity } from './../../shared';

export class Rule implements BaseEntity {
    constructor(
        public id?: string,
        public name?: string,
        public rule?: string,
    ) {
    }
}
