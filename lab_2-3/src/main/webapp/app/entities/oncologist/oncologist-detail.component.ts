import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOncologist } from 'app/shared/model/oncologist.model';

@Component({
  selector: 'jhi-oncologist-detail',
  templateUrl: './oncologist-detail.component.html'
})
export class OncologistDetailComponent implements OnInit {
  oncologist: IOncologist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oncologist }) => (this.oncologist = oncologist));
  }

  previousState(): void {
    window.history.back();
  }
}
