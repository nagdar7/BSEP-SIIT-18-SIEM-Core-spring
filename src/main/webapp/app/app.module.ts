import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ngx-webstorage';

import { SiemCoreSharedModule, UserRouteAccessService } from './shared';
import { SiemCoreAppRoutingModule} from './app-routing.module';
import { SiemCoreHomeModule } from './home/home.module';
import { SiemCoreAdminModule } from './admin/admin.module';
import { SiemCoreAccountModule } from './account/account.module';
import { SiemCoreEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        SiemCoreAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        SiemCoreSharedModule,
        SiemCoreHomeModule,
        SiemCoreAdminModule,
        SiemCoreAccountModule,
        SiemCoreEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class SiemCoreAppModule {}
