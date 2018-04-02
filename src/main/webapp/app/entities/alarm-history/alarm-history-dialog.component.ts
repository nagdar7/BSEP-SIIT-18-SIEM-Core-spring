import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmHistory } from './alarm-history.model';
import { AlarmHistoryPopupService } from './alarm-history-popup.service';
import { AlarmHistoryService } from './alarm-history.service';

@Component({
    selector: 'jhi-alarm-history-dialog',
    templateUrl: './alarm-history-dialog.component.html'
})
export class AlarmHistoryDialogComponent implements OnInit {

    alarmHistory: AlarmHistory;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alarmHistoryService: AlarmHistoryService,
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
        if (this.alarmHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.alarmHistoryService.update(this.alarmHistory));
        } else {
            this.subscribeToSaveResponse(
                this.alarmHistoryService.create(this.alarmHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<AlarmHistory>) {
        result.subscribe((res: AlarmHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: AlarmHistory) {
        this.eventManager.broadcast({ name: 'alarmHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-alarm-history-popup',
    template: ''
})
export class AlarmHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmHistoryPopupService: AlarmHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.alarmHistoryPopupService
                    .open(AlarmHistoryDialogComponent as Component, params['id']);
            } else {
                this.alarmHistoryPopupService
                    .open(AlarmHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
