import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITestResult, TestResult } from 'app/shared/model/test-result.model';
import { TestResultService } from './test-result.service';
import { IOncologist } from 'app/shared/model/oncologist.model';
import { OncologistService } from 'app/entities/oncologist/oncologist.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';

type SelectableEntity = IOncologist | IPatient;

@Component({
  selector: 'jhi-test-result-update',
  templateUrl: './test-result-update.component.html'
})
export class TestResultUpdateComponent implements OnInit {
  isSaving = false;
  oncologists: IOncologist[] = [];
  patients: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    dateTime: [],
    description: [],
    url: [],
    oncologist: [],
    patient: []
  });

  constructor(
    protected testResultService: TestResultService,
    protected oncologistService: OncologistService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testResult }) => {
      if (!testResult.id) {
        const today = moment().startOf('day');
        testResult.dateTime = today;
      }

      this.updateForm(testResult);

      this.oncologistService.query().subscribe((res: HttpResponse<IOncologist[]>) => (this.oncologists = res.body || []));

      this.patientService.query().subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body || []));
    });
  }

  updateForm(testResult: ITestResult): void {
    this.editForm.patchValue({
      id: testResult.id,
      name: testResult.name,
      type: testResult.type,
      dateTime: testResult.dateTime ? testResult.dateTime.format(DATE_TIME_FORMAT) : null,
      description: testResult.description,
      url: testResult.url,
      oncologist: testResult.oncologist,
      patient: testResult.patient
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testResult = this.createFromForm();
    if (testResult.id !== undefined) {
      this.subscribeToSaveResponse(this.testResultService.update(testResult));
    } else {
      this.subscribeToSaveResponse(this.testResultService.create(testResult));
    }
  }

  private createFromForm(): ITestResult {
    return {
      ...new TestResult(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      dateTime: this.editForm.get(['dateTime'])!.value ? moment(this.editForm.get(['dateTime'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      url: this.editForm.get(['url'])!.value,
      oncologist: this.editForm.get(['oncologist'])!.value,
      patient: this.editForm.get(['patient'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestResult>>): void {
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
