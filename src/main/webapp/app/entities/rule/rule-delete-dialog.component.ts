import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RulePopupService } from './rule-popup.service';
import { RuleService } from './rule.service';

@Component({
    selector: 'jhi-rule-delete-dialog',
    templateUrl: './rule-delete-dialog.component.html'
})
export class RuleDeleteDialogComponent {

    rule: Rule;

    constructor(
        private ruleService: RuleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.ruleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ruleListModification',
                content: 'Deleted an rule'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-delete-popup',
    template: ''
})
export class RuleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rulePopupService: RulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rulePopupService
                .open(RuleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
