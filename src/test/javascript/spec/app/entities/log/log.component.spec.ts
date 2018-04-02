/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SiemCoreTestModule } from '../../../test.module';
import { LogComponent } from '../../../../../../main/webapp/app/entities/log/log.component';
import { LogService } from '../../../../../../main/webapp/app/entities/log/log.service';
import { Log } from '../../../../../../main/webapp/app/entities/log/log.model';

describe('Component Tests', () => {

    describe('Log Management Component', () => {
        let comp: LogComponent;
        let fixture: ComponentFixture<LogComponent>;
        let service: LogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [LogComponent],
                providers: [
                    LogService
                ]
            })
            .overrideTemplate(LogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LogService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Log('123')],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.logs[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
