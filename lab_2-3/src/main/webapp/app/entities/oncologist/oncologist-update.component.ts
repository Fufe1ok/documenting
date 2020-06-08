import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IOncologist, Oncologist } from 'app/shared/model/oncologist.model';
import { OncologistService } from './oncologist.service';
import { IHospital } from 'app/shared/model/hospital.model';
import { HospitalService } from 'app/entities/hospital/hospital.service';

@Component({
  selector: 'jhi-oncologist-update',
  templateUrl: './oncologist-update.component.html'
})
export class OncologistUpdateComponent implements OnInit {
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
    protected oncologistService: OncologistService,
    protected hospitalService: HospitalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oncologist }) => {
      this.updateForm(oncologist);

      this.hospitalService.query().subscribe((res: HttpResponse<IHospital[]>) => (this.hospitals = res.body || []));
    });
  }

  updateForm(oncologist: IOncologist): void {
    this.editForm.patchValue({
      id: oncologist.id,
      name: oncologist.name,
      surname: oncologist.surname,
      email: oncologist.email,
      phone: oncologist.phone,
      roomIn: oncologist.roomIn,
      hospital: oncologist.hospital
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const oncologist = this.createFromForm();
    if (oncologist.id !== undefined) {
      this.subscribeToSaveResponse(this.oncologistService.update(oncologist));
    } else {
      this.subscribeToSaveResponse(this.oncologistService.create(oncologist));
    }
  }

  private createFromForm(): IOncologist {
    return {
      ...new Oncologist(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      roomIn: this.editForm.get(['roomIn'])!.value,
      hospital: this.editForm.get(['hospital'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOncologist>>): void {
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
