/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmHistoryDialogComponent } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history-dialog.component';
import { AlarmHistoryService } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.service';
import { AlarmHistory } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.model';

describe('Component Tests', () => {

    describe('AlarmHistory Management Dialog Component', () => {
        let comp: AlarmHistoryDialogComponent;
        let fixture: ComponentFixture<AlarmHistoryDialogComponent>;
        let service: AlarmHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmHistoryDialogComponent],
                providers: [
                    AlarmHistoryService
                ]
            })
            .overrideTemplate(AlarmHistoryDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmHistoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AlarmHistory('123');
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.alarmHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'alarmHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AlarmHistory();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.alarmHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'alarmHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
