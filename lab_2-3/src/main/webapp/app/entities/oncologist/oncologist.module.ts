import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AllscriptSharedModule } from 'app/shared/shared.module';
import { OncologistComponent } from './oncologist.component';
import { OncologistDetailComponent } from './oncologist-detail.component';
import { OncologistUpdateComponent } from './oncologist-update.component';
import { OncologistDeleteDialogComponent } from './oncologist-delete-dialog.component';
import { oncologistRoute } from './oncologist.route';

@NgModule({
  imports: [AllscriptSharedModule, RouterModule.forChild(oncologistRoute)],
  declarations: [OncologistComponent, OncologistDetailComponent, OncologistUpdateComponent, OncologistDeleteDialogComponent],
  entryComponents: [OncologistDeleteDialogComponent]
})
export class AllscriptOncologistModule {}
