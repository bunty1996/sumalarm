package com.level_sense.app.domain.usecases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.level_sense.app.core.UseCase;
import com.level_sense.app.domain.model.Collection;

import java.util.List;

public interface FetchCollectionsUseCase extends UseCase {

  Cancelable execute(@Nullable String cursor, int perPage, @NonNull Callback1<List<Collection>> callback);
}
