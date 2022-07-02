package com.shopify.sample.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.level_sense.app.domain.usecases.FetchProductsUseCase;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.level_sense.app.core.UseCase.Callback1;
import com.level_sense.app.core.UseCase.Cancelable;
import com.shopify.sample.data.graphql.Converter;
import com.shopify.sample.data.graphql.Query;
import com.level_sense.app.domain.model.Product;
import com.level_sense.app.util.CallbackExecutors;
import com.level_sense.app.view.Constant;

import java.util.List;

public final class FetchProductsUseCaseImpl implements FetchProductsUseCase {

  private final CallbackExecutors callbackExectors;
  private final GraphClient graphClient;

  public FetchProductsUseCaseImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExectors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public Cancelable execute(@NonNull final String collectionId, @Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Product>> callback) {
    Storefront.QueryRootQuery query = Storefront.query(root -> root.node(new ID(collectionId), node -> node
      .onCollection(collection -> collection
        .products(args -> args
          .first(Constant.PAGE_SIZE)
          .after(cursor), Query::products
        )
      )
    ));
    final QueryGraphCall call = graphClient.queryGraph(query);
    call.enqueue(new Callback(callback), callbackExectors.handler());
    return call::cancel;
  }

  private class Callback implements GraphCall.Callback<Storefront.QueryRoot> {

    private final Callback1<List<Product>> callback;

    public Callback(final Callback1<List<Product>> callback) {
      this.callback = callback;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
      final Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
      this.callback.onResponse(Converter.convertProducts(collection.getProducts()));
    }

    @Override
    public void onFailure(@NonNull final GraphError error) {
      callback.onError(error);
    }
  }
}
