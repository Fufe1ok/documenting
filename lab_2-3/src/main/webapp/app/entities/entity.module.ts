import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: () => import('./patient/patient.module').then(m => m.AllscriptPatientModule)
      },
      {
        path: 'oncologist',
        loadChildren: () => import('./oncologist/oncologist.module').then(m => m.AllscriptOncologistModule)
      },
      {
        path: 'therapist',
        loadChildren: () => import('./therapist/therapist.module').then(m => m.AllscriptTherapistModule)
      },
      {
        path: 'conclusion',
        loadChildren: () => import('./conclusion/conclusion.module').then(m => m.AllscriptConclusionModule)
      },
      {
        path: 'hospital',
        loadChildren: () => import('./hospital/hospital.module').then(m => m.AllscriptHospitalModule)
      },
      {
        path: 'test-result',
        loadChildren: () => import('./test-result/test-result.module').then(m => m.AllscriptTestResultModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AllscriptEntityModule {}
