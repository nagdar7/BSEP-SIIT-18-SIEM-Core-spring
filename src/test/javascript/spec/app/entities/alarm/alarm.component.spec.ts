/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmComponent } from '../../../../../../main/webapp/app/entities/alarm/alarm.component';
import { AlarmService } from '../../../../../../main/webapp/app/entities/alarm/alarm.service';
import { Alarm } from '../../../../../../main/webapp/app/entities/alarm/alarm.model';

describe('Component Tests', () => {

    describe('Alarm Management Component', () => {
        let comp: AlarmComponent;
        let fixture: ComponentFixture<AlarmComponent>;
        let service: AlarmService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmComponent],
                providers: [
                    AlarmService
                ]
            })
            .overrideTemplate(AlarmComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Alarm('123')],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.alarms[0]).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });

});
