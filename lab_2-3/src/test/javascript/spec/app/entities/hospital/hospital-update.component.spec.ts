import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { HospitalUpdateComponent } from 'app/entities/hospital/hospital-update.component';
import { HospitalService } from 'app/entities/hospital/hospital.service';
import { Hospital } from 'app/shared/model/hospital.model';

describe('Component Tests', () => {
  describe('Hospital Management Update Component', () => {
    let comp: HospitalUpdateComponent;
    let fixture: ComponentFixture<HospitalUpdateComponent>;
    let service: HospitalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [HospitalUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HospitalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HospitalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HospitalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Hospital(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Hospital();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
