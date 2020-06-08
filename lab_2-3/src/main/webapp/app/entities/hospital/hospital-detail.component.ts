import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHospital } from 'app/shared/model/hospital.model';

@Component({
  selector: 'jhi-hospital-detail',
  templateUrl: './hospital-detail.component.html'
})
export class HospitalDetailComponent implements OnInit {
  hospital: IHospital | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hospital }) => (this.hospital = hospital));
  }

  previousState(): void {
    window.history.back();
  }
}
