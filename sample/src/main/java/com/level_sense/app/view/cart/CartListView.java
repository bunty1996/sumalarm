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

package com.level_sense.app.view.cart;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.level_sense.app.util.Util;
import com.level_sense.app.view.BasePaginatedListViewModel;
import com.level_sense.app.view.base.ListItemViewModel;
import com.level_sense.app.view.base.RecyclerViewAdapter;
import com.level_sense.app.R;
import com.level_sense.app.domain.model.CartItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class CartListView extends FrameLayout implements LifecycleOwner {
  @BindView(R.id.list) RecyclerView listView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter();

  public CartListView(final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CartListView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  @Override public Lifecycle getLifecycle() {
    return lifecycleRegistry;
  }

  public void bindViewModel(@NonNull final BasePaginatedListViewModel<CartItem> viewModel) {
    Util.checkNotNull(viewModel, "viewModel == null");

    viewModel.reset();
    viewModel.errorErrorCallback().observe(this, error -> {
      if (error != null) {
        showDefaultErrorMessage();
      }
    });
    viewModel.listItemsLiveData().observe(this, this::swapItems);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    listView.setHasFixedSize(true);
    listView.setAdapter(listViewAdapter);
    ((SimpleItemAnimator) listView.getItemAnimator()).setSupportsChangeAnimations(false);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }

  private void swapItems(final List<ListItemViewModel> newItems) {
    listViewAdapter.swapItemsAndNotify(newItems);
  }

  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(this, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.show();
  }
}
