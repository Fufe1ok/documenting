import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITherapist } from 'app/shared/model/therapist.model';

@Component({
  selector: 'jhi-therapist-detail',
  templateUrl: './therapist-detail.component.html'
})
export class TherapistDetailComponent implements OnInit {
  therapist: ITherapist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ therapist }) => (this.therapist = therapist));
  }

  previousState(): void {
    window.history.back();
  }
}
