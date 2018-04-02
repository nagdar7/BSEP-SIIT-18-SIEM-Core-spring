import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SiemCoreSharedModule } from '../../shared';
import {
    AlarmHistoryService,
    AlarmHistoryPopupService,
    AlarmHistoryComponent,
    AlarmHistoryDetailComponent,
    AlarmHistoryDialogComponent,
    AlarmHistoryPopupComponent,
    AlarmHistoryDeletePopupComponent,
    AlarmHistoryDeleteDialogComponent,
    alarmHistoryRoute,
    alarmHistoryPopupRoute,
    AlarmHistoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...alarmHistoryRoute,
    ...alarmHistoryPopupRoute,
];

@NgModule({
    imports: [
        SiemCoreSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AlarmHistoryComponent,
        AlarmHistoryDetailComponent,
        AlarmHistoryDialogComponent,
        AlarmHistoryDeleteDialogComponent,
        AlarmHistoryPopupComponent,
        AlarmHistoryDeletePopupComponent,
    ],
    entryComponents: [
        AlarmHistoryComponent,
        AlarmHistoryDialogComponent,
        AlarmHistoryPopupComponent,
        AlarmHistoryDeleteDialogComponent,
        AlarmHistoryDeletePopupComponent,
    ],
    providers: [
        AlarmHistoryService,
        AlarmHistoryPopupService,
        AlarmHistoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SiemCoreAlarmHistoryModule {}
