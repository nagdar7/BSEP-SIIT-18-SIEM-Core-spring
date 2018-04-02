import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { LogComponent } from './log.component';
import { LogDetailComponent } from './log-detail.component';
import { LogPopupComponent } from './log-dialog.component';
import { LogDeletePopupComponent } from './log-delete-dialog.component';

export const logRoute: Routes = [
    {
        path: 'log',
        component: LogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Logs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'log/:id',
        component: LogDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Logs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const logPopupRoute: Routes = [
    {
        path: 'log-new',
        component: LogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Logs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'log/:id/edit',
        component: LogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Logs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'log/:id/delete',
        component: LogDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Logs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
