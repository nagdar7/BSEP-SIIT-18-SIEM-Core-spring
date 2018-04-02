/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmDetailComponent } from '../../../../../../main/webapp/app/entities/alarm/alarm-detail.component';
import { AlarmService } from '../../../../../../main/webapp/app/entities/alarm/alarm.service';
import { Alarm } from '../../../../../../main/webapp/app/entities/alarm/alarm.model';

describe('Component Tests', () => {

    describe('Alarm Management Detail Component', () => {
        let comp: AlarmDetailComponent;
        let fixture: ComponentFixture<AlarmDetailComponent>;
        let service: AlarmService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmDetailComponent],
                providers: [
                    AlarmService
                ]
            })
            .overrideTemplate(AlarmDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Alarm('123')));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith('123');
                expect(comp.alarm).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
