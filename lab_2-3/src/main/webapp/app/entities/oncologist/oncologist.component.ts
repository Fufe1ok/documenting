import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOncologist } from 'app/shared/model/oncologist.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { OncologistService } from './oncologist.service';
import { OncologistDeleteDialogComponent } from './oncologist-delete-dialog.component';

@Component({
  selector: 'jhi-oncologist',
  templateUrl: './oncologist.component.html'
})
export class OncologistComponent implements OnInit, OnDestroy {
  oncologists: IOncologist[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected oncologistService: OncologistService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.oncologists = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.oncologistService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IOncologist[]>) => this.paginateOncologists(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.oncologists = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOncologists();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOncologist): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOncologists(): void {
    this.eventSubscriber = this.eventManager.subscribe('oncologistListModification', () => this.reset());
  }

  delete(oncologist: IOncologist): void {
    const modalRef = this.modalService.open(OncologistDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.oncologist = oncologist;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateOncologists(data: IOncologist[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.oncologists.push(data[i]);
      }
    }
  }
}
