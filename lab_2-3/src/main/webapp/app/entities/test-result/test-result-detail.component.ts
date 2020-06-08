import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITestResult } from 'app/shared/model/test-result.model';

@Component({
  selector: 'jhi-test-result-detail',
  templateUrl: './test-result-detail.component.html'
})
export class TestResultDetailComponent implements OnInit {
  testResult: ITestResult | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testResult }) => (this.testResult = testResult));
  }

  previousState(): void {
    window.history.back();
  }
}
