import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AlarmHistoryComponent } from './alarm-history.component';
import { AlarmHistoryDetailComponent } from './alarm-history-detail.component';
import { AlarmHistoryPopupComponent } from './alarm-history-dialog.component';
import { AlarmHistoryDeletePopupComponent } from './alarm-history-delete-dialog.component';

@Injectable()
export class AlarmHistoryResolvePagingParams implements Resolve<any> {

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

export const alarmHistoryRoute: Routes = [
    {
        path: 'alarm-history',
        component: AlarmHistoryComponent,
        resolve: {
            'pagingParams': AlarmHistoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AlarmHistories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'alarm-history/:id',
        component: AlarmHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AlarmHistories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const alarmHistoryPopupRoute: Routes = [
    {
        path: 'alarm-history-new',
        component: AlarmHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AlarmHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm-history/:id/edit',
        component: AlarmHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AlarmHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm-history/:id/delete',
        component: AlarmHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AlarmHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
