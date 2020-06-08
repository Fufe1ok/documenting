import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITestResult } from 'app/shared/model/test-result.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TestResultService } from './test-result.service';
import { TestResultDeleteDialogComponent } from './test-result-delete-dialog.component';

@Component({
  selector: 'jhi-test-result',
  templateUrl: './test-result.component.html'
})
export class TestResultComponent implements OnInit, OnDestroy {
  testResults: ITestResult[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected testResultService: TestResultService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.testResults = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.testResultService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITestResult[]>) => this.paginateTestResults(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.testResults = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTestResults();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITestResult): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTestResults(): void {
    this.eventSubscriber = this.eventManager.subscribe('testResultListModification', () => this.reset());
  }

  delete(testResult: ITestResult): void {
    const modalRef = this.modalService.open(TestResultDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.testResult = testResult;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTestResults(data: ITestResult[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.testResults.push(data[i]);
      }
    }
  }
}
