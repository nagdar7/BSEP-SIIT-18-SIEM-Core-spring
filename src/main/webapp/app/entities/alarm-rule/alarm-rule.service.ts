import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { AlarmRule } from './alarm-rule.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AlarmRuleService {

    private resourceUrl =  SERVER_API_URL + 'api/alarm-rules';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/alarm-rules';

    constructor(private http: Http) { }

    create(alarmRule: AlarmRule): Observable<AlarmRule> {
        const copy = this.convert(alarmRule);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(alarmRule: AlarmRule): Observable<AlarmRule> {
        const copy = this.convert(alarmRule);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: string): Observable<AlarmRule> {
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
     * Convert a returned JSON object to AlarmRule.
     */
    private convertItemFromServer(json: any): AlarmRule {
        const entity: AlarmRule = Object.assign(new AlarmRule(), json);
        return entity;
    }

    /**
     * Convert a AlarmRule to a JSON which can be sent to the server.
     */
    private convert(alarmRule: AlarmRule): AlarmRule {
        const copy: AlarmRule = Object.assign({}, alarmRule);
        return copy;
    }
}
