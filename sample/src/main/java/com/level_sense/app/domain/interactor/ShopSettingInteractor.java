package com.level_sense.app.domain.interactor;

import androidx.annotation.NonNull;

import com.level_sense.app.domain.model.ShopSettings;

import io.reactivex.Single;

public interface ShopSettingInteractor {

  @NonNull Single<ShopSettings> execute();
}
