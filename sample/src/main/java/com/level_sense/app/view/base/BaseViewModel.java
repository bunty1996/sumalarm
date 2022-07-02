package com.level_sense.app.view.base;

import androidx.lifecycle.ViewModel;

import com.level_sense.app.core.UseCase;
import com.level_sense.app.BaseApplication;
import com.level_sense.app.domain.usecases.UseCases;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewModel extends ViewModel {

  private final List<UseCase.Cancelable> tasks = new ArrayList<>();

  protected UseCases useCases() {
    return BaseApplication.instance().useCases();
  }

  protected void addTask(UseCase.Cancelable task) {
    tasks.add(task);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    for (UseCase.Cancelable task : tasks) {
      task.cancel();
    }
  }
}
