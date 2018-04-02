/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmHistoryComponent } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.component';
import { AlarmHistoryService } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.service';
import { AlarmHistory } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.model';

describe('Component Tests', () => {

    describe('AlarmHistory Management Component', () => {
        let comp: AlarmHistoryComponent;
        let fixture: ComponentFixture<AlarmHistoryComponent>;
        let service: AlarmHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmHistoryComponent],
                providers: [
                    AlarmHistoryService
                ]
            })
            .overrideTemplate(AlarmHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new AlarmHistory('123')],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.alarmHistories[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
