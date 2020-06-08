import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { TherapistUpdateComponent } from 'app/entities/therapist/therapist-update.component';
import { TherapistService } from 'app/entities/therapist/therapist.service';
import { Therapist } from 'app/shared/model/therapist.model';

describe('Component Tests', () => {
  describe('Therapist Management Update Component', () => {
    let comp: TherapistUpdateComponent;
    let fixture: ComponentFixture<TherapistUpdateComponent>;
    let service: TherapistService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [TherapistUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TherapistUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TherapistUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TherapistService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Therapist(123);
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
        const entity = new Therapist();
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
