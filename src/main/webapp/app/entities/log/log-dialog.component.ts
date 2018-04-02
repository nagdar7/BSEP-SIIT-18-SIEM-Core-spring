import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Log } from './log.model';
import { LogPopupService } from './log-popup.service';
import { LogService } from './log.service';

@Component({
    selector: 'jhi-log-dialog',
    templateUrl: './log-dialog.component.html'
})
export class LogDialogComponent implements OnInit {

    log: Log;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private logService: LogService,
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
        if (this.log.id !== undefined) {
            this.subscribeToSaveResponse(
                this.logService.update(this.log));
        } else {
            this.subscribeToSaveResponse(
                this.logService.create(this.log));
        }
    }

    private subscribeToSaveResponse(result: Observable<Log>) {
        result.subscribe((res: Log) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Log) {
        this.eventManager.broadcast({ name: 'logListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-log-popup',
    template: ''
})
export class LogPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private logPopupService: LogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.logPopupService
                    .open(LogDialogComponent as Component, params['id']);
            } else {
                this.logPopupService
                    .open(LogDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
