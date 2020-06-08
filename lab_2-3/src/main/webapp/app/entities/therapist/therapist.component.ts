import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITherapist } from 'app/shared/model/therapist.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TherapistService } from './therapist.service';
import { TherapistDeleteDialogComponent } from './therapist-delete-dialog.component';

@Component({
  selector: 'jhi-therapist',
  templateUrl: './therapist.component.html'
})
export class TherapistComponent implements OnInit, OnDestroy {
  therapists: ITherapist[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected therapistService: TherapistService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.therapists = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.therapistService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITherapist[]>) => this.paginateTherapists(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.therapists = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTherapists();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITherapist): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTherapists(): void {
    this.eventSubscriber = this.eventManager.subscribe('therapistListModification', () => this.reset());
  }

  delete(therapist: ITherapist): void {
    const modalRef = this.modalService.open(TherapistDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.therapist = therapist;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTherapists(data: ITherapist[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.therapists.push(data[i]);
      }
    }
  }
}
