/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SiemCoreTestModule } from '../../../test.module';
import { RuleDialogComponent } from '../../../../../../main/webapp/app/entities/rule/rule-dialog.component';
import { RuleService } from '../../../../../../main/webapp/app/entities/rule/rule.service';
import { Rule } from '../../../../../../main/webapp/app/entities/rule/rule.model';

describe('Component Tests', () => {

    describe('Rule Management Dialog Component', () => {
        let comp: RuleDialogComponent;
        let fixture: ComponentFixture<RuleDialogComponent>;
        let service: RuleService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SiemCoreTestModule],
                declarations: [RuleDialogComponent],
                providers: [
                    RuleService
                ]
            })
            .overrideTemplate(RuleDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Rule('123');
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.rule = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ruleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Rule();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.rule = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ruleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
