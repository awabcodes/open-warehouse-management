import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenWarehouseManagementSharedModule } from 'app/shared';
import {
    ItemComponent,
    ItemDetailComponent,
    ItemUpdateComponent,
    ItemDeletePopupComponent,
    ItemDeleteDialogComponent,
    itemRoute,
    itemPopupRoute
} from './';

const ENTITY_STATES = [...itemRoute, ...itemPopupRoute];

@NgModule({
    imports: [OpenWarehouseManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ItemComponent, ItemDetailComponent, ItemUpdateComponent, ItemDeleteDialogComponent, ItemDeletePopupComponent],
    entryComponents: [ItemComponent, ItemUpdateComponent, ItemDeleteDialogComponent, ItemDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenWarehouseManagementItemModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
