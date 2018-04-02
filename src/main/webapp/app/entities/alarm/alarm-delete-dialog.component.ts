import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Alarm } from './alarm.model';
import { AlarmPopupService } from './alarm-popup.service';
import { AlarmService } from './alarm.service';

@Component({
    selector: 'jhi-alarm-delete-dialog',
    templateUrl: './alarm-delete-dialog.component.html'
})
export class AlarmDeleteDialogComponent {

    alarm: Alarm;

    constructor(
        private alarmService: AlarmService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.alarmService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'alarmListModification',
                content: 'Deleted an alarm'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-alarm-delete-popup',
    template: ''
})
export class AlarmDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private alarmPopupService: AlarmPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.alarmPopupService
                .open(AlarmDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
