import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'item',
                loadChildren: './item/item.module#OpenWarehouseManagementItemModule'
            },
            {
                path: 'out-order',
                loadChildren: './out-order/out-order.module#OpenWarehouseManagementOutOrderModule'
            },
            {
                path: 'in-order',
                loadChildren: './in-order/in-order.module#OpenWarehouseManagementInOrderModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenWarehouseManagementEntityModule {}
