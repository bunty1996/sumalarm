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

package com.level_sense.app.domain.model;

import androidx.annotation.NonNull;

import com.level_sense.app.util.Util;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public final class ProductDetails {
  @NonNull public final String id;
  @NonNull public final String title;
  @NonNull public final String description;
  @NonNull public List<String> tags;
  @NonNull public List<String> images;
  @NonNull public List<Option> options;
  @NonNull public List<Variant> variants;

  public ProductDetails(@NonNull final String id, @NonNull final String title, @NonNull final String description,
    @NonNull final List<String> tags, @NonNull final List<String> images, @NonNull final List<Option> options,
    @NonNull final List<Variant> variants) {
    this.id = Util.checkNotNull(id, "id == null");
    this.title = Util.checkNotNull(title, "title == null");
    this.description = Util.checkNotNull(description, "description == null");
    this.tags = Util.checkNotNull(tags, "id == null");
    this.images = unmodifiableList(Util.checkNotNull(images, "images == null"));
    this.options = unmodifiableList(Util.checkNotNull(options, "options == null"));
    this.variants = unmodifiableList(Util.checkNotNull(variants, "variants == null"));
  }

  @Override public String toString() {
    return "Product{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", tags=" + tags +
      ", images=" + images +
      ", options=" + options +
      ", variants=" + variants +
      '}';
  }

  public static final class Option {
    @NonNull public final String id;
    @NonNull public final String name;
    @NonNull public final List<String> values;

    public Option(@NonNull final String id, @NonNull final String name, @NonNull final List<String> values) {
      this.id = Util.checkNotNull(id, "id == null");
      this.name = Util.checkNotNull(name, "name == null");
      this.values = unmodifiableList(Util.checkNotNull(values, "values == null"));
    }

    @Override public String toString() {
      return "Option{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", values=" + values +
        '}';
    }
  }

  public static final class SelectedOption {
    @NonNull public final String name;
    @NonNull public final String value;

    public SelectedOption(@NonNull final String name, @NonNull final String value) {
      this.name = Util.checkNotNull(name, "name == null");
      this.value = Util.checkNotNull(value, "value == null");
    }

    @Override public String toString() {
      return "SelectedOption{" +
        "name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
    }
  }

  public static final class Variant {
    @NonNull public final String id;
    @NonNull public final String title;
    public final boolean available;
    @NonNull public final List<SelectedOption> selectedOptions;
    @NonNull public final BigDecimal price;

    public Variant(@NonNull final String id, @NonNull final String title, final boolean available,
      @NonNull final List<SelectedOption> selectedOptions, @NonNull final BigDecimal price) {
      this.id = Util.checkNotNull(id, "name == null");
      this.title = Util.checkNotNull(title, "title == null");
      this.available = available;
      this.selectedOptions = unmodifiableList(Util.checkNotNull(selectedOptions, "selectedOptions == null"));
      this.price = Util.checkNotNull(price, "price == null");
    }

    @Override public String toString() {
      return "Variant{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", available=" + available +
        ", selectedOptions=" + selectedOptions +
        ", price=" + price +
        '}';
    }
  }
}
