/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history-detail.component';
import { AlarmHistoryService } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.service';
import { AlarmHistory } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.model';

describe('Component Tests', () => {

    describe('AlarmHistory Management Detail Component', () => {
        let comp: AlarmHistoryDetailComponent;
        let fixture: ComponentFixture<AlarmHistoryDetailComponent>;
        let service: AlarmHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmHistoryDetailComponent],
                providers: [
                    AlarmHistoryService
                ]
            })
            .overrideTemplate(AlarmHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new AlarmHistory('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.alarmHistory).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
