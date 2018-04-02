/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmRuleComponent } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule.component';
import { AlarmRuleService } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule.service';
import { AlarmRule } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule.model';

describe('Component Tests', () => {

    describe('AlarmRule Management Component', () => {
        let comp: AlarmRuleComponent;
        let fixture: ComponentFixture<AlarmRuleComponent>;
        let service: AlarmRuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmRuleComponent],
                providers: [
                    AlarmRuleService
                ]
            })
            .overrideTemplate(AlarmRuleComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRuleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new AlarmRule('123')],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.alarmRules[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
