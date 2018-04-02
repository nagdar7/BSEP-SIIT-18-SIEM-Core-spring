import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AlarmComponent } from './alarm.component';
import { AlarmDetailComponent } from './alarm-detail.component';
import { AlarmPopupComponent } from './alarm-dialog.component';
import { AlarmDeletePopupComponent } from './alarm-delete-dialog.component';

@Injectable()
export class AlarmResolvePagingParams implements Resolve<any> {

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

export const alarmRoute: Routes = [
    {
        path: 'alarm',
        component: AlarmComponent,
        resolve: {
            'pagingParams': AlarmResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Alarms'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'alarm/:id',
        component: AlarmDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Alarms'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const alarmPopupRoute: Routes = [
    {
        path: 'alarm-new',
        component: AlarmPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Alarms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm/:id/edit',
        component: AlarmPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Alarms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'alarm/:id/delete',
        component: AlarmDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Alarms'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
