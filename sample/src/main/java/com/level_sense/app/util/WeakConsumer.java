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

package com.level_sense.app.util;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;


public class WeakConsumer<TARGET, RESPONSE> {
  final WeakReference<TARGET> targetRef;
  AcceptDelegate<TARGET, Throwable> acceptDelegate;

  public static <TARGET, RESPONSE> WeakConsumer<TARGET, RESPONSE> forTarget(@Nullable final TARGET target) {
    return new WeakConsumer<>(target);
  }

  private WeakConsumer(@Nullable final TARGET target) {
    this.targetRef = new WeakReference<>(target);
  }

  public WeakConsumer<TARGET, RESPONSE> delegateAccept(@Nullable final AcceptDelegate<TARGET, Throwable> to) {
    this.acceptDelegate = to;
    return this;
  }

  public Consumer<? super Throwable> create() {
    return response -> {
      final TARGET target = targetRef.get();
      if (target != null && acceptDelegate != null) {
        acceptDelegate.accept(target, response);
      }
    };
  }

  public interface AcceptDelegate<TARGET, RESPONSE> {
    void accept(TARGET target, RESPONSE response);
  }
}
