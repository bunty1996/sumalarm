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

package com.level_sense.app.view.collections;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.level_sense.app.domain.model.Product;
import com.level_sense.app.util.Util;
import com.level_sense.app.view.ScreenActionEvent;

import java.math.BigDecimal;

public final class CollectionProductClickActionEvent extends ScreenActionEvent implements Parcelable {

    public static final Creator<CollectionProductClickActionEvent> CREATOR = new Creator<CollectionProductClickActionEvent>() {
        @Override
        public CollectionProductClickActionEvent createFromParcel(Parcel in) {
            return new CollectionProductClickActionEvent(in);
        }

        @Override
        public CollectionProductClickActionEvent[] newArray(int size) {
            return new CollectionProductClickActionEvent[size];
        }
    };

    public static final String ACTION = CollectionProductClickActionEvent.class.getSimpleName();
    private static final String EXTRAS_ID = "product_id";
    private static final String EXTRAS_IMAGE_URL = "product_image_url";
    private static final String EXTRAS_TITLE = "product_title";
    private static final String EXTRAS_PRICE = "product_price";

    CollectionProductClickActionEvent(@NonNull final Product product) {
        super(ACTION);
        Util.checkNotNull(product, "collectionProduct == null");
        payload.putString(EXTRAS_ID, product.id);
        payload.putString(EXTRAS_IMAGE_URL, product.image);
        payload.putString(EXTRAS_TITLE, product.title);
        payload.putDouble(EXTRAS_PRICE, product.price.doubleValue());
    }

    @SuppressWarnings("WeakerAccess")
    CollectionProductClickActionEvent(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String id() {
        return payload().getString(EXTRAS_ID);
    }

    public String imageUrl() {
        return payload().getString(EXTRAS_IMAGE_URL);
    }

    public String title() {
        return payload().getString(EXTRAS_TITLE);
    }

    public BigDecimal price() {
        return BigDecimal.valueOf(payload().getDouble(EXTRAS_PRICE, 0));
    }

}
