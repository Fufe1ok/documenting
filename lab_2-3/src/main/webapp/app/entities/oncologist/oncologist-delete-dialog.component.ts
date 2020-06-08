import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOncologist } from 'app/shared/model/oncologist.model';
import { OncologistService } from './oncologist.service';

@Component({
  templateUrl: './oncologist-delete-dialog.component.html'
})
export class OncologistDeleteDialogComponent {
  oncologist?: IOncologist;

  constructor(
    protected oncologistService: OncologistService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.oncologistService.delete(id).subscribe(() => {
      this.eventManager.broadcast('oncologistListModification');
      this.activeModal.close();
    });
  }
}
