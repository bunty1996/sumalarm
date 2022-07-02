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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.widget.NestedScrollView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.level_sense.app.util.Util;
import com.level_sense.app.R;
import com.level_sense.app.domain.model.ProductDetails;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class ProductDescriptionView extends NestedScrollView {

    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.price)
    TextView priceView;
    @BindView(R.id.description)
    TextView descriptionView;
    private OnAddToCartClickListener onAddToCartClickListener;

    private Context context;

    public ProductDescriptionView(final Context context) {
        super(context);
        this.context = context;
    }

    public ProductDescriptionView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ProductDescriptionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void renderProduct(final String title, final double price) {
        titleView.setText(title);
        priceView.setText(getResources().getString(R.string.price_from, CURRENCY_FORMAT.format(price)));
    }

    public void renderProduct(final ProductDetails product) {
        titleView.setText(product.title);
        priceView.setText(getResources().getString(R.string.price_from, formatMinPrice(product)));
        descriptionView.setText(Html.fromHtml(product.description));
    }

    public void setOnAddToCartClickListener(final OnAddToCartClickListener onAddToCartClickListener) {
        this.onAddToCartClickListener = onAddToCartClickListener;
    }

    @OnClick(R.id.price)
    void onAddToCartClick() {

        if (onAddToCartClickListener != null) {

            Log.e("prodctprcclk", "ok");
            onAddToCartClickListener.onAddToCartClick();
            showAddCommentDialog();

        }

    }

    public void showAddCommentDialog() {

        TextView addProductDescription,addProductOk;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_product_cart);

        addProductDescription=(TextView) dialog.findViewById(R.id.addProductDescription);
        addProductOk=(TextView) dialog.findViewById(R.id.addProductOk);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL;
        addProductOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private String formatMinPrice(final ProductDetails product) {

        List<BigDecimal> prices = Util.mapItems(product.variants, variant -> variant.price);
        BigDecimal minPrice = Util.minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
        return CURRENCY_FORMAT.format(minPrice);

    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick();
    }

}
