import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConclusion } from 'app/shared/model/conclusion.model';
import { ConclusionService } from './conclusion.service';

@Component({
  templateUrl: './conclusion-delete-dialog.component.html'
})
export class ConclusionDeleteDialogComponent {
  conclusion?: IConclusion;

  constructor(
    protected conclusionService: ConclusionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conclusionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('conclusionListModification');
      this.activeModal.close();
    });
  }
}
