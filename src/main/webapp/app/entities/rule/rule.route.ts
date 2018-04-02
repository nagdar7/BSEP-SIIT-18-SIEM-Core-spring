import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { RuleComponent } from './rule.component';
import { RuleDetailComponent } from './rule-detail.component';
import { RulePopupComponent } from './rule-dialog.component';
import { RuleDeletePopupComponent } from './rule-delete-dialog.component';

@Injectable()
export class RuleResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const ruleRoute: Routes = [
    {
        path: 'rule',
        component: RuleComponent,
        resolve: {
            'pagingParams': RuleResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rules'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rule/:id',
        component: RuleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rules'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rulePopupRoute: Routes = [
    {
        path: 'rule-new',
        component: RulePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rules'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rule/:id/edit',
        component: RulePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rules'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rule/:id/delete',
        component: RuleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rules'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
