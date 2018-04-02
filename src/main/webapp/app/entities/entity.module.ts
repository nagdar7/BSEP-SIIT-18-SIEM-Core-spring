import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SiemCoreAlarmRuleModule } from './alarm-rule/alarm-rule.module';
import { SiemCoreAgentModule } from './agent/agent.module';
import { SiemCoreAlarmModule } from './alarm/alarm.module';
import { SiemCoreAlarmHistoryModule } from './alarm-history/alarm-history.module';
import { SiemCoreLogModule } from './log/log.module';
import { SiemCoreRuleModule } from './rule/rule.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SiemCoreAlarmRuleModule,
        SiemCoreAgentModule,
        SiemCoreAlarmModule,
        SiemCoreAlarmHistoryModule,
        SiemCoreLogModule,
        SiemCoreRuleModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SiemCoreEntityModule {}
