import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Rule } from './rule.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RuleService {

    private resourceUrl =  SERVER_API_URL + 'api/rules';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rules';

    constructor(private http: Http) { }

    create(rule: Rule): Observable<Rule> {
        const copy = this.convert(rule);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(rule: Rule): Observable<Rule> {
        const copy = this.convert(rule);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: string): Observable<Rule> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Rule.
     */
    private convertItemFromServer(json: any): Rule {
        const entity: Rule = Object.assign(new Rule(), json);
        return entity;
    }

    /**
     * Convert a Rule to a JSON which can be sent to the server.
     */
    private convert(rule: Rule): Rule {
        const copy: Rule = Object.assign({}, rule);
        return copy;
    }
}
