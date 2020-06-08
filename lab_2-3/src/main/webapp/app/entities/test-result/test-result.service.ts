import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITestResult } from 'app/shared/model/test-result.model';

type EntityResponseType = HttpResponse<ITestResult>;
type EntityArrayResponseType = HttpResponse<ITestResult[]>;

@Injectable({ providedIn: 'root' })
export class TestResultService {
  public resourceUrl = SERVER_API_URL + 'api/test-results';

  constructor(protected http: HttpClient) {}

  create(testResult: ITestResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(testResult);
    return this.http
      .post<ITestResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(testResult: ITestResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(testResult);
    return this.http
      .put<ITestResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITestResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITestResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(testResult: ITestResult): ITestResult {
    const copy: ITestResult = Object.assign({}, testResult, {
      dateTime: testResult.dateTime && testResult.dateTime.isValid() ? testResult.dateTime.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTime = res.body.dateTime ? moment(res.body.dateTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((testResult: ITestResult) => {
        testResult.dateTime = testResult.dateTime ? moment(testResult.dateTime) : undefined;
      });
    }
    return res;
  }
}
