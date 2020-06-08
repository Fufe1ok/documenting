import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITherapist, Therapist } from 'app/shared/model/therapist.model';
import { TherapistService } from './therapist.service';
import { IHospital } from 'app/shared/model/hospital.model';
import { HospitalService } from 'app/entities/hospital/hospital.service';

@Component({
  selector: 'jhi-therapist-update',
  templateUrl: './therapist-update.component.html'
})
export class TherapistUpdateComponent implements OnInit {
  isSaving = false;
  hospitals: IHospital[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    surname: [null, [Validators.required, Validators.maxLength(100)]],
    email: [null, [Validators.required, Validators.maxLength(100)]],
    phone: [null, [Validators.pattern('^(\\+\\d{1,3}[- ]?)?\\d{10}$')]],
    roomIn: [],
    hospital: []
  });

  constructor(
    protected therapistService: TherapistService,
    protected hospitalService: HospitalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ therapist }) => {
      this.updateForm(therapist);

      this.hospitalService.query().subscribe((res: HttpResponse<IHospital[]>) => (this.hospitals = res.body || []));
    });
  }

  updateForm(therapist: ITherapist): void {
    this.editForm.patchValue({
      id: therapist.id,
      name: therapist.name,
      surname: therapist.surname,
      email: therapist.email,
      phone: therapist.phone,
      roomIn: therapist.roomIn,
      hospital: therapist.hospital
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const therapist = this.createFromForm();
    if (therapist.id !== undefined) {
      this.subscribeToSaveResponse(this.therapistService.update(therapist));
    } else {
      this.subscribeToSaveResponse(this.therapistService.create(therapist));
    }
  }

  private createFromForm(): ITherapist {
    return {
      ...new Therapist(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      roomIn: this.editForm.get(['roomIn'])!.value,
      hospital: this.editForm.get(['hospital'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITherapist>>): void {
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

  trackById(index: number, item: IHospital): any {
    return item.id;
  }
}
