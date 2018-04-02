import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RuleService } from './rule.service';

@Component({
    selector: 'jhi-rule-detail',
    templateUrl: './rule-detail.component.html'
})
export class RuleDetailComponent implements OnInit, OnDestroy {

    rule: Rule;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ruleService: RuleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRules();
    }

    load(id) {
        this.ruleService.find(id).subscribe((rule) => {
            this.rule = rule;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRules() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ruleListModification',
            (response) => this.load(this.rule.id)
        );
    }
}
