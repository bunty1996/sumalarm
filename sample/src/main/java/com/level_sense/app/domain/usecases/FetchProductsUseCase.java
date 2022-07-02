package com.level_sense.app.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.level_sense.app.core.UseCase;
import com.level_sense.app.domain.model.Product;

import java.util.List;

public interface FetchProductsUseCase {

  UseCase.Cancelable execute(@NonNull String collectionId, @Nullable String cursor, int perPage, @NonNull UseCase.Callback1<List<Product>> callback);
}
