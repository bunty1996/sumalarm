/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample.domain.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.Storefront;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.level_sense.app.util.Util.checkNotNull;

public final class CollectionRepository {
  private final GraphClient graphClient;

  public CollectionRepository(@NonNull final GraphClient graphClient) {
    this.graphClient = checkNotNull(graphClient, "graphClient == null");
  }

  @NonNull public Single<List<Storefront.CollectionEdge>> nextPage(@Nullable final String cursor, final int perPage,
    @NonNull Storefront.CollectionConnectionQueryDefinition query) {
    checkNotNull(query, "query == null");

    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(Storefront.query(
      root -> root.shop(
        shop -> shop.collections(
          args -> args
            .first(perPage)
            .after(TextUtils.isEmpty(cursor) ? null : cursor)
            .sortKey(Storefront.CollectionSortKeys.TITLE),
          query
        )
      )
    ));

    return rxGraphQueryCall(call)
      .map(Storefront.QueryRoot::getShop)
      .map(Storefront.Shop::getCollections)
      .map(Storefront.CollectionConnection::getEdges)
      .subscribeOn(Schedulers.io());
  }
}
