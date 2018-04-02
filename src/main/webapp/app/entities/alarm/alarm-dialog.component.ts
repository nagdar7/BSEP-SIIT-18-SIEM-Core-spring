import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Alarm } from './alarm.model';
import { AlarmPopupService } from './alarm-popup.service';
import { AlarmService } from './alarm.service';

@Component({
    selector: 'jhi-alarm-dialog',
    templateUrl: './alarm-dialog.component.html'
})
export class AlarmDialogComponent implements OnInit {

    alarm: Alarm;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alarmService: AlarmService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.alarm.id !== undefined) {
            this.subscribeToSaveResponse(
                this.alarmService.update(this.alarm));
        } else {
            this.subscribeToSaveResponse(
                this.alarmService.create(this.alarm));
        }
    }

    private subscribeToSaveResponse(result: Observable<Alarm>) {
        result.subscribe((res: Alarm) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Alarm) {
        this.eventManager.broadcast({ name: 'alarmListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-alarm-popup',
    template: ''
})
export class AlarmPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmPopupService: AlarmPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.alarmPopupService
                    .open(AlarmDialogComponent as Component, params['id']);
            } else {
                this.alarmPopupService
                    .open(AlarmDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
