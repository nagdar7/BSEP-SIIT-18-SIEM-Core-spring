import { BaseEntity } from './../../shared';

export class Agent implements BaseEntity {
    constructor(
        public id?: string,
        public directory?: string,
        public description?: string,
        public filterExpression?: string,
    ) {
    }
}
