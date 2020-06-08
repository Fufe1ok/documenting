import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { HospitalDetailComponent } from 'app/entities/hospital/hospital-detail.component';
import { Hospital } from 'app/shared/model/hospital.model';

describe('Component Tests', () => {
  describe('Hospital Management Detail Component', () => {
    let comp: HospitalDetailComponent;
    let fixture: ComponentFixture<HospitalDetailComponent>;
    const route = ({ data: of({ hospital: new Hospital(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [HospitalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HospitalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HospitalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load hospital on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hospital).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
