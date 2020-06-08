import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHospital } from 'app/shared/model/hospital.model';
import { HospitalService } from './hospital.service';

@Component({
  templateUrl: './hospital-delete-dialog.component.html'
})
export class HospitalDeleteDialogComponent {
  hospital?: IHospital;

  constructor(protected hospitalService: HospitalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hospitalService.delete(id).subscribe(() => {
      this.eventManager.broadcast('hospitalListModification');
      this.activeModal.close();
    });
  }
}
