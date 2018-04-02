import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AlarmHistory } from './alarm-history.model';
import { AlarmHistoryService } from './alarm-history.service';

@Component({
    selector: 'jhi-alarm-history-detail',
    templateUrl: './alarm-history-detail.component.html'
})
export class AlarmHistoryDetailComponent implements OnInit, OnDestroy {

    alarmHistory: AlarmHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private alarmHistoryService: AlarmHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAlarmHistories();
    }

    load(id) {
        this.alarmHistoryService.find(id).subscribe((alarmHistory) => {
            this.alarmHistory = alarmHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAlarmHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'alarmHistoryListModification',
            (response) => this.load(this.alarmHistory.id)
        );
    }
}
