import { BaseEntity } from './../../shared';

export class Log implements BaseEntity {
    constructor(
        public id?: string,
        public facility?: number,
        public severnity?: number,
        public message?: string,
    ) {
    }
}
