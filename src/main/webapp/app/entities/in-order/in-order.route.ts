import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InOrder } from 'app/shared/model/in-order.model';
import { InOrderService } from './in-order.service';
import { InOrderComponent } from './in-order.component';
import { InOrderDetailComponent } from './in-order-detail.component';
import { InOrderUpdateComponent } from './in-order-update.component';
import { InOrderDeletePopupComponent } from './in-order-delete-dialog.component';
import { IInOrder } from 'app/shared/model/in-order.model';

@Injectable({ providedIn: 'root' })
export class InOrderResolve implements Resolve<IInOrder> {
    constructor(private service: InOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInOrder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<InOrder>) => response.ok),
                map((inOrder: HttpResponse<InOrder>) => inOrder.body)
            );
        }
        return of(new InOrder());
    }
}

export const inOrderRoute: Routes = [
    {
        path: '',
        component: InOrderComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_AUTHORIZER'],
            defaultSort: 'id,asc',
            pageTitle: 'openWarehouseManagementApp.inOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: InOrderDetailComponent,
        resolve: {
            inOrder: InOrderResolve
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_AUTHORIZER'],
            pageTitle: 'openWarehouseManagementApp.inOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: InOrderUpdateComponent,
        resolve: {
            inOrder: InOrderResolve
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_AUTHORIZER'],
            pageTitle: 'openWarehouseManagementApp.inOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: InOrderUpdateComponent,
        resolve: {
            inOrder: InOrderResolve
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_AUTHORIZER'],
            pageTitle: 'openWarehouseManagementApp.inOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const inOrderPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: InOrderDeletePopupComponent,
        resolve: {
            inOrder: InOrderResolve
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_AUTHORIZER'],
            pageTitle: 'openWarehouseManagementApp.inOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
