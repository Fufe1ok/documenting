import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { ConclusionDetailComponent } from 'app/entities/conclusion/conclusion-detail.component';
import { Conclusion } from 'app/shared/model/conclusion.model';

describe('Component Tests', () => {
  describe('Conclusion Management Detail Component', () => {
    let comp: ConclusionDetailComponent;
    let fixture: ComponentFixture<ConclusionDetailComponent>;
    const route = ({ data: of({ conclusion: new Conclusion(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [ConclusionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConclusionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConclusionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load conclusion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.conclusion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
