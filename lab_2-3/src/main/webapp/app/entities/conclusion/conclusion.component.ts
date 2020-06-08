import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConclusion } from 'app/shared/model/conclusion.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ConclusionService } from './conclusion.service';
import { ConclusionDeleteDialogComponent } from './conclusion-delete-dialog.component';

@Component({
  selector: 'jhi-conclusion',
  templateUrl: './conclusion.component.html'
})
export class ConclusionComponent implements OnInit, OnDestroy {
  conclusions: IConclusion[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected conclusionService: ConclusionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.conclusions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.conclusionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IConclusion[]>) => this.paginateConclusions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.conclusions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInConclusions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IConclusion): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInConclusions(): void {
    this.eventSubscriber = this.eventManager.subscribe('conclusionListModification', () => this.reset());
  }

  delete(conclusion: IConclusion): void {
    const modalRef = this.modalService.open(ConclusionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conclusion = conclusion;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateConclusions(data: IConclusion[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.conclusions.push(data[i]);
      }
    }
  }
}
