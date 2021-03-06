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

package com.shopify.sample.domain.interactor;

import androidx.annotation.NonNull;

import com.level_sense.app.domain.interactor.CheckoutCreateInteractor;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.level_sense.app.domain.model.Checkout;
import com.level_sense.app.domain.model.UserMessageError;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.level_sense.app.domain.repository.UserError;

import java.util.List;

import io.reactivex.Single;

import static com.level_sense.app.util.Util.checkNotEmpty;
import static com.level_sense.app.util.Util.mapItems;

public final class RealCheckoutCreateInteractor implements CheckoutCreateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutCreateInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Checkout> execute(@NonNull final List<Checkout.LineItem> lineItems) {
    checkNotEmpty(lineItems, "lineItems can't be empty");

    List<Storefront.CheckoutLineItemInput> storefrontLineItems = mapItems(lineItems, lineItem ->
      new Storefront.CheckoutLineItemInput(lineItem.quantity, new ID(lineItem.variantId)));

    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput().setLineItems(storefrontLineItems);

    return repository
      .create(input, q -> q.checkout(new CheckoutCreateFragment()))
      .map(Converters::convertToCheckout)
      .onErrorResumeNext(t -> Single.error((t instanceof UserError) ? new UserMessageError(t.getMessage()) : t));
  }
}
