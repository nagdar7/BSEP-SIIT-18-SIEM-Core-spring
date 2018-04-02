import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmHistory } from './alarm-history.model';
import { AlarmHistoryPopupService } from './alarm-history-popup.service';
import { AlarmHistoryService } from './alarm-history.service';

@Component({
    selector: 'jhi-alarm-history-delete-dialog',
    templateUrl: './alarm-history-delete-dialog.component.html'
})
export class AlarmHistoryDeleteDialogComponent {

    alarmHistory: AlarmHistory;

    constructor(
        private alarmHistoryService: AlarmHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.alarmHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'alarmHistoryListModification',
                content: 'Deleted an alarmHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-alarm-history-delete-popup',
    template: ''
})
export class AlarmHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmHistoryPopupService: AlarmHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.alarmHistoryPopupService
                .open(AlarmHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
