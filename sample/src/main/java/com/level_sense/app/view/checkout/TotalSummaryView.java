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

package com.level_sense.app.view.checkout;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.level_sense.app.util.Util;
import com.level_sense.app.R;

import java.math.BigDecimal;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TotalSummaryView extends ConstraintLayout {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.subtotal) TextView subtotalView;
  @BindView(R.id.shipping) TextView shippingView;
  @BindView(R.id.tax) TextView taxView;
  @BindView(R.id.total) TextView totalView;

  public TotalSummaryView(final Context context) {
    super(context);
  }

  public TotalSummaryView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public TotalSummaryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void render(@NonNull final BigDecimal subtotal, @NonNull final BigDecimal shipping, @NonNull final BigDecimal tax,
    @NonNull final BigDecimal total) {
    subtotalView.setText(CURRENCY_FORMAT.format(Util.checkNotNull(subtotal, "subtotal == null")));
    shippingView.setText(CURRENCY_FORMAT.format(Util.checkNotNull(shipping, "shipping == null")));
    taxView.setText(CURRENCY_FORMAT.format(Util.checkNotNull(tax, "tax == null")));
    totalView.setText(getResources().getString(R.string.checkout_summary_total,
      CURRENCY_FORMAT.format(Util.checkNotNull(total, "total == null"))));
  }
}
