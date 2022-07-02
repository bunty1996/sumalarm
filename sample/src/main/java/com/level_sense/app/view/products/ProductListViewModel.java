package com.level_sense.app.view.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;

import com.level_sense.app.core.UseCase;
import com.level_sense.app.domain.model.Product;
import com.level_sense.app.util.Util;
import com.level_sense.app.view.Constant;
import com.level_sense.app.view.base.BasePaginatedListViewModel;
import com.level_sense.app.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends BasePaginatedListViewModel<Product> {

  private final String collectionId;

  private final LiveData<List<ListItemViewModel>> items = Transformations
    .map(data(), products -> Util.reduce(products, (viewModels, product) -> {
      viewModels.add(new ProductListItemViewModel(product));
      return viewModels;
    }, new ArrayList<ListItemViewModel>()));

  public ProductListViewModel(String collectionId) {
    super();
    this.collectionId = collectionId;
  }

  public LiveData<List<ListItemViewModel>> items() {
    return items;
  }

  @Override
  protected UseCase.Cancelable onFetchData(@NonNull final List<Product> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchProducts()
      .execute(collectionId, cursor, Constant.PAGE_SIZE, this);
  }
}
