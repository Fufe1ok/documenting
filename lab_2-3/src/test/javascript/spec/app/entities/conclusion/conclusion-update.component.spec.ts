import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { ConclusionUpdateComponent } from 'app/entities/conclusion/conclusion-update.component';
import { ConclusionService } from 'app/entities/conclusion/conclusion.service';
import { Conclusion } from 'app/shared/model/conclusion.model';

describe('Component Tests', () => {
  describe('Conclusion Management Update Component', () => {
    let comp: ConclusionUpdateComponent;
    let fixture: ComponentFixture<ConclusionUpdateComponent>;
    let service: ConclusionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [ConclusionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ConclusionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConclusionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConclusionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Conclusion(123);
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
        const entity = new Conclusion();
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
