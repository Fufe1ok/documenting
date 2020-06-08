import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IConclusion, Conclusion } from 'app/shared/model/conclusion.model';
import { ConclusionService } from './conclusion.service';
import { IOncologist } from 'app/shared/model/oncologist.model';
import { OncologistService } from 'app/entities/oncologist/oncologist.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';

type SelectableEntity = IOncologist | IPatient;

@Component({
  selector: 'jhi-conclusion-update',
  templateUrl: './conclusion-update.component.html'
})
export class ConclusionUpdateComponent implements OnInit {
  isSaving = false;
  oncologists: IOncologist[] = [];
  patients: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    resultDescription: [],
    url: [],
    signedBy: [],
    forPatient: []
  });

  constructor(
    protected conclusionService: ConclusionService,
    protected oncologistService: OncologistService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conclusion }) => {
      if (!conclusion.id) {
        const today = moment().startOf('day');
        conclusion.date = today;
      }

      this.updateForm(conclusion);

      this.oncologistService.query().subscribe((res: HttpResponse<IOncologist[]>) => (this.oncologists = res.body || []));

      this.patientService.query().subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body || []));
    });
  }

  updateForm(conclusion: IConclusion): void {
    this.editForm.patchValue({
      id: conclusion.id,
      date: conclusion.date ? conclusion.date.format(DATE_TIME_FORMAT) : null,
      resultDescription: conclusion.resultDescription,
      url: conclusion.url,
      signedBy: conclusion.signedBy,
      forPatient: conclusion.forPatient
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conclusion = this.createFromForm();
    if (conclusion.id !== undefined) {
      this.subscribeToSaveResponse(this.conclusionService.update(conclusion));
    } else {
      this.subscribeToSaveResponse(this.conclusionService.create(conclusion));
    }
  }

  private createFromForm(): IConclusion {
    return {
      ...new Conclusion(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      resultDescription: this.editForm.get(['resultDescription'])!.value,
      url: this.editForm.get(['url'])!.value,
      signedBy: this.editForm.get(['signedBy'])!.value,
      forPatient: this.editForm.get(['forPatient'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConclusion>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
