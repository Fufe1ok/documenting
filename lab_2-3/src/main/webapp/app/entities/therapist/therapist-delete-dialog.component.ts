import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITherapist } from 'app/shared/model/therapist.model';
import { TherapistService } from './therapist.service';

@Component({
  templateUrl: './therapist-delete-dialog.component.html'
})
export class TherapistDeleteDialogComponent {
  therapist?: ITherapist;

  constructor(protected therapistService: TherapistService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.therapistService.delete(id).subscribe(() => {
      this.eventManager.broadcast('therapistListModification');
      this.activeModal.close();
    });
  }
}
