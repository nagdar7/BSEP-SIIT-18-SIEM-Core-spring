/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SiemCoreTestModule } from '../../../test.module';
import { RuleComponent } from '../../../../../../main/webapp/app/entities/rule/rule.component';
import { RuleService } from '../../../../../../main/webapp/app/entities/rule/rule.service';
import { Rule } from '../../../../../../main/webapp/app/entities/rule/rule.model';

describe('Component Tests', () => {

    describe('Rule Management Component', () => {
        let comp: RuleComponent;
        let fixture: ComponentFixture<RuleComponent>;
        let service: RuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [RuleComponent],
                providers: [
                    RuleService
                ]
            })
            .overrideTemplate(RuleComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Rule('123')],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.rules[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
