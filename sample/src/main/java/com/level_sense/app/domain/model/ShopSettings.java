package com.level_sense.app.domain.model;

import androidx.annotation.NonNull;

import com.level_sense.app.util.Util;
import com.shopify.buy3.pay.CardNetworkType;

import java.util.Collections;
import java.util.Set;

public final class ShopSettings {
  @NonNull public final String name;
  @NonNull public final Set<CardNetworkType> acceptedCardBrands;
  @NonNull public final String countryCode;

  public ShopSettings(@NonNull final String name, @NonNull final Set<CardNetworkType> acceptedCardBrands, @NonNull final String countryCode) {
    this.name = Util.checkNotNull(name, "name can't be null");
    this.acceptedCardBrands = Collections.unmodifiableSet(Util.checkNotNull(acceptedCardBrands, "acceptedCardBrands can't be null)"));
    this.countryCode = Util.checkNotNull(countryCode, "countryCode can't be null");
  }

  @Override public String toString() {
    return "ShopSettings{" +
      "name='" + name + '\'' +
      ", acceptedCardBrands=" + acceptedCardBrands +
      ", countryCode='" + countryCode + '\'' +
      '}';
  }
}
