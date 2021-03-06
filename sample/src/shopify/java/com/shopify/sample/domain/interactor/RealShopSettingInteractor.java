package com.shopify.sample.domain.interactor;

import androidx.annotation.NonNull;

import com.level_sense.app.domain.interactor.ShopSettingInteractor;
import com.shopify.buy3.Storefront;
import com.shopify.sample.SampleApplication;
import com.level_sense.app.domain.model.ShopSettings;
import com.shopify.sample.domain.repository.ShopRepository;

import io.reactivex.Single;

public final class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<ShopSettings> execute() {
    Storefront.ShopQueryDefinition query = q -> q
      .name()
      .paymentSettings(settings -> settings
        .countryCode()
        .acceptedCardBrands()
      );
    return repository
      .shopSettings(query)
      .map(Converters::convertToShopSettings);
  }
}
