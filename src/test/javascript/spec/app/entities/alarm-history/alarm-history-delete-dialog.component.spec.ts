/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SiemCoreTestModule } from '../../../test.module';
import { AlarmHistoryDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history-delete-dialog.component';
import { AlarmHistoryService } from '../../../../../../main/webapp/app/entities/alarm-history/alarm-history.service';

describe('Component Tests', () => {

    describe('AlarmHistory Management Delete Component', () => {
        let comp: AlarmHistoryDeleteDialogComponent;
        let fixture: ComponentFixture<AlarmHistoryDeleteDialogComponent>;
        let service: AlarmHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [AlarmHistoryDeleteDialogComponent],
                providers: [
                    AlarmHistoryService
                ]
            })
            .overrideTemplate(AlarmHistoryDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AlarmHistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlarmHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete('123');
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith('123');
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
