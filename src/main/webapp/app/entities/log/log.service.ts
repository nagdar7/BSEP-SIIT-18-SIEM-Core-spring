import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Log } from './log.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LogService {

    private resourceUrl =  SERVER_API_URL + 'api/logs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/logs';

    constructor(private http: Http) { }

    create(log: Log): Observable<Log> {
        const copy = this.convert(log);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(log: Log): Observable<Log> {
        const copy = this.convert(log);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: string): Observable<Log> {
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
     * Convert a returned JSON object to Log.
     */
    private convertItemFromServer(json: any): Log {
        const entity: Log = Object.assign(new Log(), json);
        return entity;
    }

    /**
     * Convert a Log to a JSON which can be sent to the server.
     */
    private convert(log: Log): Log {
        const copy: Log = Object.assign({}, log);
        return copy;
    }
}
