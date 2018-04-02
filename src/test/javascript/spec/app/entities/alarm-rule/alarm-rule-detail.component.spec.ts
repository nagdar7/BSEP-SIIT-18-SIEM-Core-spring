/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmRuleDetailComponent } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule-detail.component';
import { AlarmRuleService } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule.service';
import { AlarmRule } from '../../../../../../main/webapp/app/entities/alarm-rule/alarm-rule.model';

describe('Component Tests', () => {

    describe('AlarmRule Management Detail Component', () => {
        let comp: AlarmRuleDetailComponent;
        let fixture: ComponentFixture<AlarmRuleDetailComponent>;
        let service: AlarmRuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmRuleDetailComponent],
                providers: [
                    AlarmRuleService
                ]
            })
            .overrideTemplate(AlarmRuleDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmRuleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmRuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new AlarmRule('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.alarmRule).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
