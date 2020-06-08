import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOncologist } from 'app/shared/model/oncologist.model';

type EntityResponseType = HttpResponse<IOncologist>;
type EntityArrayResponseType = HttpResponse<IOncologist[]>;

@Injectable({ providedIn: 'root' })
export class OncologistService {
  public resourceUrl = SERVER_API_URL + 'api/oncologists';

  constructor(protected http: HttpClient) {}

  create(oncologist: IOncologist): Observable<EntityResponseType> {
    return this.http.post<IOncologist>(this.resourceUrl, oncologist, { observe: 'response' });
  }

  update(oncologist: IOncologist): Observable<EntityResponseType> {
    return this.http.put<IOncologist>(this.resourceUrl, oncologist, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOncologist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOncologist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
