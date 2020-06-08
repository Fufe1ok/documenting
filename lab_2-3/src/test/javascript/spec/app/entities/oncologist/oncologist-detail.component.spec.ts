import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { OncologistDetailComponent } from 'app/entities/oncologist/oncologist-detail.component';
import { Oncologist } from 'app/shared/model/oncologist.model';

describe('Component Tests', () => {
  describe('Oncologist Management Detail Component', () => {
    let comp: OncologistDetailComponent;
    let fixture: ComponentFixture<OncologistDetailComponent>;
    const route = ({ data: of({ oncologist: new Oncologist(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [OncologistDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OncologistDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OncologistDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load oncologist on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.oncologist).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
