package com.shopify.sample.view.base;

import android.arch.lifecycle.ViewModel;

import com.shopify.sample.BaseApplication;
import com.shopify.sample.domain.UseCases;
import com.shopify.sample.util.UseCase.Cancelable;

import java.util.ArrayList;
import java.util.List;

public class BaseViewModel extends ViewModel {

  private final List<Cancelable> tasks = new ArrayList<>();

  protected UseCases getUseCases() {
    return BaseApplication.getInstance().getUseCases();
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
