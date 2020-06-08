import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { AllscriptSharedModule } from 'app/shared/shared.module';
import { AllscriptCoreModule } from 'app/core/core.module';
import { AllscriptAppRoutingModule } from './app-routing.module';
import { AllscriptHomeModule } from './home/home.module';
import { AllscriptEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    AllscriptSharedModule,
    AllscriptCoreModule,
    AllscriptHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AllscriptEntityModule,
    AllscriptAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class AllscriptAppModule {}
