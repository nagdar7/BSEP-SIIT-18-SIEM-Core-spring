import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SiemCoreSharedModule } from '../../shared';
import {
    AlarmService,
    AlarmPopupService,
    AlarmComponent,
    AlarmDetailComponent,
    AlarmDialogComponent,
    AlarmPopupComponent,
    AlarmDeletePopupComponent,
    AlarmDeleteDialogComponent,
    alarmRoute,
    alarmPopupRoute,
    AlarmResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...alarmRoute,
    ...alarmPopupRoute,
];

@NgModule({
    imports: [
        SiemCoreSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AlarmComponent,
        AlarmDetailComponent,
        AlarmDialogComponent,
        AlarmDeleteDialogComponent,
        AlarmPopupComponent,
        AlarmDeletePopupComponent,
    ],
    entryComponents: [
        AlarmComponent,
        AlarmDialogComponent,
        AlarmPopupComponent,
        AlarmDeleteDialogComponent,
        AlarmDeletePopupComponent,
    ],
    providers: [
        AlarmService,
        AlarmPopupService,
        AlarmResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SiemCoreAlarmModule {}
