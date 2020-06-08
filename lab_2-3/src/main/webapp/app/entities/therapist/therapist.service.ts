import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITherapist } from 'app/shared/model/therapist.model';

type EntityResponseType = HttpResponse<ITherapist>;
type EntityArrayResponseType = HttpResponse<ITherapist[]>;

@Injectable({ providedIn: 'root' })
export class TherapistService {
  public resourceUrl = SERVER_API_URL + 'api/therapists';

  constructor(protected http: HttpClient) {}

  create(therapist: ITherapist): Observable<EntityResponseType> {
    return this.http.post<ITherapist>(this.resourceUrl, therapist, { observe: 'response' });
  }

  update(therapist: ITherapist): Observable<EntityResponseType> {
    return this.http.put<ITherapist>(this.resourceUrl, therapist, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITherapist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITherapist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
