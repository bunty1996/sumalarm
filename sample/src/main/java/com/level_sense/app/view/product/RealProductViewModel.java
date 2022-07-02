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

package com.level_sense.app.view.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.level_sense.app.domain.interactor.CartAddItemInteractor;
import com.level_sense.app.domain.interactor.ProductByIdInteractor;
import com.level_sense.app.util.Util;
import com.level_sense.app.view.BaseViewModel;
import com.level_sense.app.domain.interactor.RealCartAddItemInteractor;
import com.shopify.sample.domain.interactor.RealProductByIdInteractor;
import com.level_sense.app.domain.model.CartItem;
import com.level_sense.app.domain.model.ProductDetails;
import com.level_sense.app.util.WeakObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.level_sense.app.util.Util.firstItem;

@SuppressWarnings("WeakerAccess")
public final class RealProductViewModel extends BaseViewModel implements ProductViewModel {
  private final ProductByIdInteractor productByIdInteractor = new RealProductByIdInteractor();
  private final CartAddItemInteractor cartAddItemInteractor = new RealCartAddItemInteractor();
  private final MutableLiveData<ProductDetails> productLiveData = new MutableLiveData<>();

  private final String productId;

  public RealProductViewModel(@NonNull final String productId) {
    this.productId = Util.checkNotNull(productId, "productId == null");
    refetch();
  }

  @Override public void refetch() {
    showProgress(REQUEST_ID_PRODUCT_DETAILS);
    registerRequest(
      REQUEST_ID_PRODUCT_DETAILS,
      productByIdInteractor.execute(productId)
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<RealProductViewModel, ProductDetails>forTarget(this)
          .delegateOnNext(RealProductViewModel::onProductDetailsResponse)
          .delegateOnError(RealProductViewModel::onProductDetailsError)
          .create())
    );
  }

  @Override public LiveData<ProductDetails> productLiveData() {
    return productLiveData;
  }

  @Override public void addToCart() {
    ProductDetails product = productLiveData.getValue();
    if (product != null) {
      ProductDetails.Variant firstVariant = Util.checkNotNull(Util.firstItem(product.variants), "can't find default variant");
      CartItem cartItem = new CartItem(product.id, firstVariant.id, product.title, firstVariant.title, firstVariant.price,
        Util.mapItems(firstVariant.selectedOptions, it -> new CartItem.Option(it.name, it.value)), Util.firstItem(product.images));
      cartAddItemInteractor.execute(cartItem);
    }
  }

  private void onProductDetailsResponse(final ProductDetails product) {
    hideProgress(REQUEST_ID_PRODUCT_DETAILS);
    productLiveData.setValue(product);
  }

  private void onProductDetailsError(final Throwable t) {
    hideProgress(REQUEST_ID_PRODUCT_DETAILS);
    notifyUserError(REQUEST_ID_PRODUCT_DETAILS, t);
  }
}
