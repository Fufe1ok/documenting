import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { TherapistDetailComponent } from 'app/entities/therapist/therapist-detail.component';
import { Therapist } from 'app/shared/model/therapist.model';

describe('Component Tests', () => {
  describe('Therapist Management Detail Component', () => {
    let comp: TherapistDetailComponent;
    let fixture: ComponentFixture<TherapistDetailComponent>;
    const route = ({ data: of({ therapist: new Therapist(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [TherapistDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TherapistDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TherapistDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load therapist on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.therapist).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
