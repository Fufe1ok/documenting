import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHospital, Hospital } from 'app/shared/model/hospital.model';
import { HospitalService } from './hospital.service';
import { HospitalComponent } from './hospital.component';
import { HospitalDetailComponent } from './hospital-detail.component';
import { HospitalUpdateComponent } from './hospital-update.component';

@Injectable({ providedIn: 'root' })
export class HospitalResolve implements Resolve<IHospital> {
  constructor(private service: HospitalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHospital> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((hospital: HttpResponse<Hospital>) => {
          if (hospital.body) {
            return of(hospital.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Hospital());
  }
}

export const hospitalRoute: Routes = [
  {
    path: '',
    component: HospitalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Hospitals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HospitalDetailComponent,
    resolve: {
      hospital: HospitalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Hospitals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HospitalUpdateComponent,
    resolve: {
      hospital: HospitalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Hospitals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HospitalUpdateComponent,
    resolve: {
      hospital: HospitalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Hospitals'
    },
    canActivate: [UserRouteAccessService]
  }
];
