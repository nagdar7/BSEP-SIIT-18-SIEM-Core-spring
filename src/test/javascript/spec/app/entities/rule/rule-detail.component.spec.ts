/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { RuleDetailComponent } from '../../../../../../main/webapp/app/entities/rule/rule-detail.component';
import { RuleService } from '../../../../../../main/webapp/app/entities/rule/rule.service';
import { Rule } from '../../../../../../main/webapp/app/entities/rule/rule.model';

describe('Component Tests', () => {

    describe('Rule Management Detail Component', () => {
        let comp: RuleDetailComponent;
        let fixture: ComponentFixture<RuleDetailComponent>;
        let service: RuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [RuleDetailComponent],
                providers: [
                    RuleService
                ]
            })
            .overrideTemplate(RuleDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Rule('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.rule).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
