package com.shopify.sample.view.base;

import android.arch.lifecycle.ViewModel;

import com.shopify.sample.BaseApplication;
import com.shopify.sample.domain.usecases.UseCases;
import com.shopify.sample.core.UseCase.Cancelable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewModel extends ViewModel {

  private final List<Cancelable> tasks = new ArrayList<>();

  protected UseCases useCases() {
    return BaseApplication.instance().useCases();
  }

  protected void addTask(Cancelable task) {
    tasks.add(task);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    for (Cancelable task : tasks) {
      task.cancel();
    }
  }
}
