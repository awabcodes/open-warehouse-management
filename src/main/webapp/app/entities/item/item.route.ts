import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Item } from 'app/shared/model/item.model';
import { ItemService } from './item.service';
import { ItemComponent } from './item.component';
import { ItemDetailComponent } from './item-detail.component';
import { ItemUpdateComponent } from './item-update.component';
import { ItemDeletePopupComponent } from './item-delete-dialog.component';
import { IItem } from 'app/shared/model/item.model';

@Injectable({ providedIn: 'root' })
export class ItemResolve implements Resolve<IItem> {
    constructor(private service: ItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IItem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Item>) => response.ok),
                map((item: HttpResponse<Item>) => item.body)
            );
        }
        return of(new Item());
    }
}

export const itemRoute: Routes = [
    {
        path: '',
        component: ItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_MANAGER'],
            defaultSort: 'id,asc',
            pageTitle: 'openWarehouseManagementApp.item.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ItemDetailComponent,
        resolve: {
            item: ItemResolve
        },
        data: {
            authorities: ['ROLE_MANAGER', 'ROLE_USER', 'ROLE_AUTHORIZER'],
            pageTitle: 'openWarehouseManagementApp.item.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ItemUpdateComponent,
        resolve: {
            item: ItemResolve
        },
        data: {
            authorities: ['ROLE_MANAGER'],
            pageTitle: 'openWarehouseManagementApp.item.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ItemUpdateComponent,
        resolve: {
            item: ItemResolve
        },
        data: {
            authorities: ['ROLE_MANAGER'],
            pageTitle: 'openWarehouseManagementApp.item.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const itemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ItemDeletePopupComponent,
        resolve: {
            item: ItemResolve
        },
        data: {
            authorities: ['ROLE_MANAGER'],
            pageTitle: 'openWarehouseManagementApp.item.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
