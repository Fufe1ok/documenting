import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHospital } from 'app/shared/model/hospital.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { HospitalService } from './hospital.service';
import { HospitalDeleteDialogComponent } from './hospital-delete-dialog.component';

@Component({
  selector: 'jhi-hospital',
  templateUrl: './hospital.component.html'
})
export class HospitalComponent implements OnInit, OnDestroy {
  hospitals: IHospital[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected hospitalService: HospitalService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.hospitals = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.hospitalService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IHospital[]>) => this.paginateHospitals(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.hospitals = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHospitals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHospital): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHospitals(): void {
    this.eventSubscriber = this.eventManager.subscribe('hospitalListModification', () => this.reset());
  }

  delete(hospital: IHospital): void {
    const modalRef = this.modalService.open(HospitalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hospital = hospital;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateHospitals(data: IHospital[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.hospitals.push(data[i]);
      }
    }
  }
}
