package com.donutellko.stepikintern;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.donutellko.stepikintern.mvp.IView;

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

        model.getSearch(query);
    }

    @Override
    public void showSearch(List<Course> courses, String query) {
        if (courses.size() == 0) {
            view.showEmptyList();
        } else {
            view.showList(courses, query);
        }

        view.showUpdating(false);
    }

    @Override
    public void getLastSearch() {
        view.showLoading();
        model.getLastSearch();
    }

    @Override
    public void getNextPage() {
        if (!model.isNextAvailable()) {
            view.showUpdating(false);
            return;
        }

        model.getNextPage();
    }

    @Override
    public void showNextPage(List<Course> courses) {
        view.appendList(courses);
        view.showUpdating(false);
    }

    @Override
    public void getStarred() {
        model.getStarred();
    }

    @Override
    public void showStarred(List<Course> starred) {
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

        view.showUpdating(false);
    }


    public IModel getModel() {
        return model;
    }
}
