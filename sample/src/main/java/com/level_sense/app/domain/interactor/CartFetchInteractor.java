package com.level_sense.app.domain.interactor;

import androidx.annotation.NonNull;

import com.level_sense.app.domain.model.Cart;

public interface CartFetchInteractor {
  @NonNull Cart execute();
}
