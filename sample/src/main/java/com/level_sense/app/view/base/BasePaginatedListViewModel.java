package com.level_sense.app.view.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.level_sense.app.core.UseCase.Callback1;
import com.level_sense.app.core.UseCase.Cancelable;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePaginatedListViewModel<T> extends BaseViewModel implements Callback1<List<T>> {

  private final MutableLiveData<List<T>> data = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();
  private final MutableLiveData<State> state = new MutableLiveData<>();

  public BasePaginatedListViewModel() {
    reset();
  }

  public BasePaginatedListViewModel(@NonNull List<T> data) {
    this();
    onResponse(data);
  }

  public void fetchDataIfNecessary() {
    if (isEmpty()) {
      fetchData();
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void fetchData() {
    if (isFetching() || hasMorePages()) {
      return;
    }
    state.setValue(State.FETCHING);
    addTask(onFetchData(data.getValue()));
  }

  public LiveData<List<T>> data() {
    return data;
  }

  public LiveData<Throwable> error() {
    return error;
  }

  public LiveData<State> state() {
    return state;
  }

  public void reset() {
    onCleared();
    state.setValue(State.IDLE);
    data.setValue(new ArrayList<>());
  }

  private boolean isEmpty() {
    return data.getValue() == null || data.getValue().size() == 0;
  }

  private boolean hasMorePages() {
    return state.getValue() == State.COMPLETED;
  }

  private boolean isFetching() {
    return state.getValue() == State.FETCHING;
  }

  @Override
  public void onError(final Throwable throwable) {
    error.setValue(throwable);
    state.setValue(State.ERROR);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onResponse(final List<T> data) {
    if (data.isEmpty()) {
      state.setValue(State.COMPLETED);
    } else {
      List<T> newData = this.data.getValue();
      newData.addAll(data);
      this.data.setValue(newData);
      state.setValue(State.FETCH_COMPLETE);
    }
  }

  public enum State {
    IDLE, FETCHING, FETCH_COMPLETE, ERROR, COMPLETED
  }

  protected abstract Cancelable onFetchData(@NonNull List<T> data);
}
