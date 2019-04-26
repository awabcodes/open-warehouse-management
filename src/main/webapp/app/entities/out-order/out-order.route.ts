import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OutOrder } from 'app/shared/model/out-order.model';
import { OutOrderService } from './out-order.service';
import { OutOrderComponent } from './out-order.component';
import { OutOrderDetailComponent } from './out-order-detail.component';
import { OutOrderUpdateComponent } from './out-order-update.component';
import { OutOrderDeletePopupComponent } from './out-order-delete-dialog.component';
import { IOutOrder } from 'app/shared/model/out-order.model';

@Injectable({ providedIn: 'root' })
export class OutOrderResolve implements Resolve<IOutOrder> {
    constructor(private service: OutOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOutOrder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OutOrder>) => response.ok),
                map((outOrder: HttpResponse<OutOrder>) => outOrder.body)
            );
        }
        return of(new OutOrder());
    }
}

export const outOrderRoute: Routes = [
    {
        path: '',
        component: OutOrderComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openWarehouseManagementApp.outOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OutOrderDetailComponent,
        resolve: {
            outOrder: OutOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openWarehouseManagementApp.outOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OutOrderUpdateComponent,
        resolve: {
            outOrder: OutOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openWarehouseManagementApp.outOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OutOrderUpdateComponent,
        resolve: {
            outOrder: OutOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openWarehouseManagementApp.outOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const outOrderPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OutOrderDeletePopupComponent,
        resolve: {
            outOrder: OutOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openWarehouseManagementApp.outOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
