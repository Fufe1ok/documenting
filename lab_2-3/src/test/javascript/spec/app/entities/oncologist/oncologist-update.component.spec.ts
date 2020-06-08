import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { OncologistUpdateComponent } from 'app/entities/oncologist/oncologist-update.component';
import { OncologistService } from 'app/entities/oncologist/oncologist.service';
import { Oncologist } from 'app/shared/model/oncologist.model';

describe('Component Tests', () => {
  describe('Oncologist Management Update Component', () => {
    let comp: OncologistUpdateComponent;
    let fixture: ComponentFixture<OncologistUpdateComponent>;
    let service: OncologistService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [OncologistUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OncologistUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OncologistUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OncologistService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Oncologist(123);
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
        const entity = new Oncologist();
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
