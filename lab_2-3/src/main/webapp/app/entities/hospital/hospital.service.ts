import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHospital } from 'app/shared/model/hospital.model';

type EntityResponseType = HttpResponse<IHospital>;
type EntityArrayResponseType = HttpResponse<IHospital[]>;

@Injectable({ providedIn: 'root' })
export class HospitalService {
  public resourceUrl = SERVER_API_URL + 'api/hospitals';

  constructor(protected http: HttpClient) {}

  create(hospital: IHospital): Observable<EntityResponseType> {
    return this.http.post<IHospital>(this.resourceUrl, hospital, { observe: 'response' });
  }

  update(hospital: IHospital): Observable<EntityResponseType> {
    return this.http.put<IHospital>(this.resourceUrl, hospital, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHospital>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHospital[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
