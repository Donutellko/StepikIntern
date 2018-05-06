package com.donutellko.stepikintern;

import android.content.Context;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

import com.donutellko.stepikintern.mvp.IModel.State;

import java.util.List;

public class PresenterImpl implements IPresenter {

    private IView view;
    private IModel model;

    PresenterImpl(IView view) {
        this.view = view;
        model = new ModelImpl(this);
    }

    PresenterImpl(IView view, IModel model) {
        this.view = view;
        this.model = model;
        ((ModelImpl) model).setPresenter(this);
    }

    @Override
    public void getSearch(String query) {
        view.cleanList(); // При выведении первой страницы очищаем список
        view.showLoading();

        model.setState(State.LOADING);
        model.getSearch(query);
    }

    @Override
    public void showSearch(List<Course> courses, String query) {
        if (courses == null || courses.size() == 0) {
            view.showEmptyList();
        } else {
            view.showList(courses, query);
        }

        model.setState(State.SEARCH);
        view.showUpdating(false);
    }

    @Override
    public void getLastSearch() {
        view.showLoading();

        model.setState(State.LOADING);
        model.getLastSearch();
    }

    @Override
    public void getNextPage() {
        if (!model.isNextAvailable()) {
            view.showUpdating(false);
            return;
        }

        model.setState(State.LOADING);
        model.getNextPage();
    }

    @Override
    public void showNextPage(List<Course> courses) {
        model.setState(State.SEARCH);
        view.appendList(courses);
        view.showUpdating(false);
    }

    @Override
    public void getStarred() {
        model.setState(State.LOADING);
        model.getStarred();
    }

    @Override
    public void showStarred(List<Course> starred) {
        model.setState(State.STARRED);
        view.showStarred(starred);
    }

    @Override
    public boolean isStarred(Course course) {
        return model.isStarred(course);
    }

    @Override
    public void setStarred(Course course, boolean b) {
        model.setStarred(course, b);
    }

    @Override
    public boolean getHasNext() {
        return model.getHasNext();
    }

    @Override
    public int getPageNumber() {
        return model.getPageNumber();
    }

    @Override
    public String getLastQuery() {
        return model.getLastQuery();
    }

    @Override
    public void showError(Throwable t) {
        if (t.getMessage().contains("Unable to resolve")) {
            view.showNoConnection();
        } else {
            view.showError(t);
        }

        model.setState(State.ERROR);
        view.showUpdating(false);
    }

    @Override
    public State getState() {
        return model.getState();
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    public IModel getModel() {
        return model;
    }

}
