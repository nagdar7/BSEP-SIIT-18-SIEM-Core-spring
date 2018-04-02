import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { AlarmHistory } from './alarm-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AlarmHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/alarm-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/alarm-histories';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(alarmHistory: AlarmHistory): Observable<AlarmHistory> {
        const copy = this.convert(alarmHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(alarmHistory: AlarmHistory): Observable<AlarmHistory> {
        const copy = this.convert(alarmHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: string): Observable<AlarmHistory> {
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
     * Convert a returned JSON object to AlarmHistory.
     */
    private convertItemFromServer(json: any): AlarmHistory {
        const entity: AlarmHistory = Object.assign(new AlarmHistory(), json);
        entity.date = this.dateUtils
            .convertDateTimeFromServer(json.date);
        return entity;
    }

    /**
     * Convert a AlarmHistory to a JSON which can be sent to the server.
     */
    private convert(alarmHistory: AlarmHistory): AlarmHistory {
        const copy: AlarmHistory = Object.assign({}, alarmHistory);

        copy.date = this.dateUtils.toDate(alarmHistory.date);
        return copy;
    }
}
