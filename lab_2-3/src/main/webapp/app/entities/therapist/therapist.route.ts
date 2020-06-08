import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITherapist, Therapist } from 'app/shared/model/therapist.model';
import { TherapistService } from './therapist.service';
import { TherapistComponent } from './therapist.component';
import { TherapistDetailComponent } from './therapist-detail.component';
import { TherapistUpdateComponent } from './therapist-update.component';

@Injectable({ providedIn: 'root' })
export class TherapistResolve implements Resolve<ITherapist> {
  constructor(private service: TherapistService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITherapist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((therapist: HttpResponse<Therapist>) => {
          if (therapist.body) {
            return of(therapist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Therapist());
  }
}

export const therapistRoute: Routes = [
  {
    path: '',
    component: TherapistComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Therapists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TherapistDetailComponent,
    resolve: {
      therapist: TherapistResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Therapists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TherapistUpdateComponent,
    resolve: {
      therapist: TherapistResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Therapists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TherapistUpdateComponent,
    resolve: {
      therapist: TherapistResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Therapists'
    },
    canActivate: [UserRouteAccessService]
  }
];
