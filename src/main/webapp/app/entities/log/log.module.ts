import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SiemCoreSharedModule } from '../../shared';
import {
    LogService,
    LogPopupService,
    LogComponent,
    LogDetailComponent,
    LogDialogComponent,
    LogPopupComponent,
    LogDeletePopupComponent,
    LogDeleteDialogComponent,
    logRoute,
    logPopupRoute,
} from './';

const ENTITY_STATES = [
    ...logRoute,
    ...logPopupRoute,
];

@NgModule({
    imports: [
        SiemCoreSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LogComponent,
        LogDetailComponent,
        LogDialogComponent,
        LogDeleteDialogComponent,
        LogPopupComponent,
        LogDeletePopupComponent,
    ],
    entryComponents: [
        LogComponent,
        LogDialogComponent,
        LogPopupComponent,
        LogDeleteDialogComponent,
        LogDeletePopupComponent,
    ],
    providers: [
        LogService,
        LogPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SiemCoreLogModule {}
