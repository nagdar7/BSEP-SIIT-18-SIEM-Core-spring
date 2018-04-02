import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Log } from './log.model';
import { LogService } from './log.service';

@Component({
    selector: 'jhi-log-detail',
    templateUrl: './log-detail.component.html'
})
export class LogDetailComponent implements OnInit, OnDestroy {

    log: Log;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private logService: LogService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLogs();
    }

    load(id) {
        this.logService.find(id).subscribe((log) => {
            this.log = log;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLogs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'logListModification',
            (response) => this.load(this.log.id)
        );
    }
}
