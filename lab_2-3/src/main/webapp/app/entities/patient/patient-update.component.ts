import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { ITherapist } from 'app/shared/model/therapist.model';
import { TherapistService } from 'app/entities/therapist/therapist.service';
import { IOncologist } from 'app/shared/model/oncologist.model';
import { OncologistService } from 'app/entities/oncologist/oncologist.service';

type SelectableEntity = ITherapist | IOncologist;

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html'
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;
  therapists: ITherapist[] = [];
  oncologists: IOncologist[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    surname: [null, [Validators.required, Validators.maxLength(100)]],
    email: [null, [Validators.required, Validators.maxLength(100)]],
    age: [],
    weight: [],
    height: [],
    phone: [null, [Validators.pattern('^(\\+\\d{1,3}[- ]?)?\\d{10}$')]],
    address: [null, [Validators.maxLength(100)]],
    therapists: [],
    oncologists: []
  });

  constructor(
    protected patientService: PatientService,
    protected therapistService: TherapistService,
    protected oncologistService: OncologistService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);

      this.therapistService.query().subscribe((res: HttpResponse<ITherapist[]>) => (this.therapists = res.body || []));

      this.oncologistService.query().subscribe((res: HttpResponse<IOncologist[]>) => (this.oncologists = res.body || []));
    });
  }

  updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      name: patient.name,
      surname: patient.surname,
      email: patient.email,
      age: patient.age,
      weight: patient.weight,
      height: patient.height,
      phone: patient.phone,
      address: patient.address,
      therapists: patient.therapists,
      oncologists: patient.oncologists
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  private createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      age: this.editForm.get(['age'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      height: this.editForm.get(['height'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      address: this.editForm.get(['address'])!.value,
      therapists: this.editForm.get(['therapists'])!.value,
      oncologists: this.editForm.get(['oncologists'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  getSelected(selectedVals: SelectableEntity[], option: SelectableEntity): SelectableEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
