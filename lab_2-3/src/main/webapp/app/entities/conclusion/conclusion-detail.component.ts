import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConclusion } from 'app/shared/model/conclusion.model';

@Component({
  selector: 'jhi-conclusion-detail',
  templateUrl: './conclusion-detail.component.html'
})
export class ConclusionDetailComponent implements OnInit {
  conclusion: IConclusion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conclusion }) => (this.conclusion = conclusion));
  }

  previousState(): void {
    window.history.back();
  }
}
