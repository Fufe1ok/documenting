import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllscriptTestModule } from '../../../test.module';
import { TestResultDetailComponent } from 'app/entities/test-result/test-result-detail.component';
import { TestResult } from 'app/shared/model/test-result.model';

describe('Component Tests', () => {
  describe('TestResult Management Detail Component', () => {
    let comp: TestResultDetailComponent;
    let fixture: ComponentFixture<TestResultDetailComponent>;
    const route = ({ data: of({ testResult: new TestResult(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AllscriptTestModule],
        declarations: [TestResultDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TestResultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TestResultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load testResult on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.testResult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
