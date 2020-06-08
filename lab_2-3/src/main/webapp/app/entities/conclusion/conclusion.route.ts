import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IConclusion, Conclusion } from 'app/shared/model/conclusion.model';
import { ConclusionService } from './conclusion.service';
import { ConclusionComponent } from './conclusion.component';
import { ConclusionDetailComponent } from './conclusion-detail.component';
import { ConclusionUpdateComponent } from './conclusion-update.component';

@Injectable({ providedIn: 'root' })
export class ConclusionResolve implements Resolve<IConclusion> {
  constructor(private service: ConclusionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConclusion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((conclusion: HttpResponse<Conclusion>) => {
          if (conclusion.body) {
            return of(conclusion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Conclusion());
  }
}

export const conclusionRoute: Routes = [
  {
    path: '',
    component: ConclusionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Conclusions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ConclusionDetailComponent,
    resolve: {
      conclusion: ConclusionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Conclusions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ConclusionUpdateComponent,
    resolve: {
      conclusion: ConclusionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Conclusions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ConclusionUpdateComponent,
    resolve: {
      conclusion: ConclusionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Conclusions'
    },
    canActivate: [UserRouteAccessService]
  }
];
