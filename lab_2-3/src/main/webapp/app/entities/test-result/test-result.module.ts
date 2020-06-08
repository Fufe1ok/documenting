import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AllscriptSharedModule } from 'app/shared/shared.module';
import { TestResultComponent } from './test-result.component';
import { TestResultDetailComponent } from './test-result-detail.component';
import { TestResultUpdateComponent } from './test-result-update.component';
import { TestResultDeleteDialogComponent } from './test-result-delete-dialog.component';
import { testResultRoute } from './test-result.route';

@NgModule({
  imports: [AllscriptSharedModule, RouterModule.forChild(testResultRoute)],
  declarations: [TestResultComponent, TestResultDetailComponent, TestResultUpdateComponent, TestResultDeleteDialogComponent],
  entryComponents: [TestResultDeleteDialogComponent]
})
export class AllscriptTestResultModule {}
