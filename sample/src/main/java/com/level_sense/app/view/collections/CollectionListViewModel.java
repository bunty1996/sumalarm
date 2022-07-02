package com.level_sense.app.view.collections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;

import com.level_sense.app.core.UseCase;
import com.level_sense.app.domain.model.Collection;
import com.level_sense.app.util.Util;
import com.level_sense.app.view.Constant;
import com.level_sense.app.view.base.BasePaginatedListViewModel;
import com.level_sense.app.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public final class CollectionListViewModel extends BasePaginatedListViewModel<Collection> {

    private final LiveData<List<ListItemViewModel>> items = Transformations
            .map(data(), collections -> Util.reduce(collections, (viewModels, collection) -> {
                viewModels.add(new CollectionTitleListItemViewModel(collection));
                //viewModels.add(new CollectionImageListItemViewModel(collection));
                viewModels.add(new ProductsListItemViewModel(collection.products));
                viewModels.add(new CollectionDescriptionSummaryListItemViewModel(collection));
                viewModels.add(new CollectionDividerListItemViewModel(collection));
                return viewModels;
            }, new ArrayList<ListItemViewModel>()));

    public LiveData<List<ListItemViewModel>> items() {
        return items;
    }

    @Override
    protected UseCase.Cancelable onFetchData(@NonNull final List<Collection> data) {
        String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
        return useCases().fetchCollections().execute(cursor, Constant.PAGE_SIZE, this);

    }

}
