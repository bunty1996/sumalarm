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

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.internal.Optional;
import com.shopify.sample.domain.CollectionPageWithProductsQuery;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotNull;

public final class CollectionRepository {

  private final ApolloClient apolloClient;

  public CollectionRepository(@NonNull final ApolloClient apolloClient) {
    this.apolloClient = checkNotNull(apolloClient, "apolloClient == null");
  }

  @NonNull
  public Single<List<CollectionPageWithProductsQuery.Edge>> nextPage(
    @Nullable final CollectionPageWithProductsQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.query(query))
      .map(Optional::get)
      .map(it -> it.shop)
      .map(it -> it.collectionConnection)
      .map(it -> it.edges)
      .subscribeOn(Schedulers.io());
  }
}
