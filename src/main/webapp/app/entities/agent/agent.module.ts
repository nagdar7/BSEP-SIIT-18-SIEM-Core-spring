import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SiemCoreSharedModule } from '../../shared';
import {
    AgentService,
    AgentPopupService,
    AgentComponent,
    AgentDetailComponent,
    AgentDialogComponent,
    AgentPopupComponent,
    AgentDeletePopupComponent,
    AgentDeleteDialogComponent,
    agentRoute,
    agentPopupRoute,
    AgentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...agentRoute,
    ...agentPopupRoute,
];

@NgModule({
    imports: [
        SiemCoreSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AgentComponent,
        AgentDetailComponent,
        AgentDialogComponent,
        AgentDeleteDialogComponent,
        AgentPopupComponent,
        AgentDeletePopupComponent,
    ],
    entryComponents: [
        AgentComponent,
        AgentDialogComponent,
        AgentPopupComponent,
        AgentDeleteDialogComponent,
        AgentDeletePopupComponent,
    ],
    providers: [
        AgentService,
        AgentPopupService,
        AgentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SiemCoreAgentModule {}
