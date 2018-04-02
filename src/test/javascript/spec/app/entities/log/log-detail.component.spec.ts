/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { LogDetailComponent } from '../../../../../../main/webapp/app/entities/log/log-detail.component';
import { LogService } from '../../../../../../main/webapp/app/entities/log/log.service';
import { Log } from '../../../../../../main/webapp/app/entities/log/log.model';

describe('Component Tests', () => {

    describe('Log Management Detail Component', () => {
        let comp: LogDetailComponent;
        let fixture: ComponentFixture<LogDetailComponent>;
        let service: LogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [LogDetailComponent],
                providers: [
                    LogService
                ]
            })
            .overrideTemplate(LogDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LogDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LogService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Log('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.log).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
