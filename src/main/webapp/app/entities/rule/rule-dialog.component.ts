import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RulePopupService } from './rule-popup.service';
import { RuleService } from './rule.service';

@Component({
    selector: 'jhi-rule-dialog',
    templateUrl: './rule-dialog.component.html'
})
export class RuleDialogComponent implements OnInit {

    rule: Rule;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private ruleService: RuleService,
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
        if (this.rule.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ruleService.update(this.rule));
        } else {
            this.subscribeToSaveResponse(
                this.ruleService.create(this.rule));
        }
    }

    private subscribeToSaveResponse(result: Observable<Rule>) {
        result.subscribe((res: Rule) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Rule) {
        this.eventManager.broadcast({ name: 'ruleListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-rule-popup',
    template: ''
})
export class RulePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rulePopupService: RulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rulePopupService
                    .open(RuleDialogComponent as Component, params['id']);
            } else {
                this.rulePopupService
                    .open(RuleDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
