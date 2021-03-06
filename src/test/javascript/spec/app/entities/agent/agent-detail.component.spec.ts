/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { AgentDetailComponent } from '../../../../../../main/webapp/app/entities/agent/agent-detail.component';
import { AgentService } from '../../../../../../main/webapp/app/entities/agent/agent.service';
import { Agent } from '../../../../../../main/webapp/app/entities/agent/agent.model';

describe('Component Tests', () => {

    describe('Agent Management Detail Component', () => {
        let comp: AgentDetailComponent;
        let fixture: ComponentFixture<AgentDetailComponent>;
        let service: AgentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AgentDetailComponent],
                providers: [
                    AgentService
                ]
            })
            .overrideTemplate(AgentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AgentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AgentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Agent('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.agent).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
