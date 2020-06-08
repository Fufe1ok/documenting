import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IConclusion } from 'app/shared/model/conclusion.model';

type EntityResponseType = HttpResponse<IConclusion>;
type EntityArrayResponseType = HttpResponse<IConclusion[]>;

@Injectable({ providedIn: 'root' })
export class ConclusionService {
  public resourceUrl = SERVER_API_URL + 'api/conclusions';

  constructor(protected http: HttpClient) {}

  create(conclusion: IConclusion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conclusion);
    return this.http
      .post<IConclusion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(conclusion: IConclusion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conclusion);
    return this.http
      .put<IConclusion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConclusion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConclusion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(conclusion: IConclusion): IConclusion {
    const copy: IConclusion = Object.assign({}, conclusion, {
      date: conclusion.date && conclusion.date.isValid() ? conclusion.date.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((conclusion: IConclusion) => {
        conclusion.date = conclusion.date ? moment(conclusion.date) : undefined;
      });
    }
    return res;
  }
}
