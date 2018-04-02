import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Log } from './log.model';
import { LogPopupService } from './log-popup.service';
import { LogService } from './log.service';

@Component({
    selector: 'jhi-log-delete-dialog',
    templateUrl: './log-delete-dialog.component.html'
})
export class LogDeleteDialogComponent {

    log: Log;

    constructor(
        private logService: LogService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.logService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'logListModification',
                content: 'Deleted an log'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-log-delete-popup',
    template: ''
})
export class LogDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private logPopupService: LogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.logPopupService
                .open(LogDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
