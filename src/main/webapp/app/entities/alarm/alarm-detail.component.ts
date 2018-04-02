import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Alarm } from './alarm.model';
import { AlarmService } from './alarm.service';

@Component({
    selector: 'jhi-alarm-detail',
    templateUrl: './alarm-detail.component.html'
})
export class AlarmDetailComponent implements OnInit, OnDestroy {

    alarm: Alarm;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private alarmService: AlarmService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAlarms();
    }

    load(id) {
        this.alarmService.find(id).subscribe((alarm) => {
            this.alarm = alarm;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAlarms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'alarmListModification',
            (response) => this.load(this.alarm.id)
        );
    }
}
