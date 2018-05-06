package com.donutellko.stepikintern;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IModel;
import com.donutellko.stepikintern.mvp.IPresenter;

import java.util.ArrayList;
import java.util.List;

class ModelImpl implements IModel {

    IPresenter presenter;

    List<Course> starredList;

    public ModelImpl(PresenterImpl presenter) {
        this.presenter = presenter;
        starredList = loadStarredList();
    }

    @Override
    public void getStarred() {
        presenter.showStarred(starredList);
    }

    @Override
    public void setStarred(Course course, boolean b) {
        if (starredList.contains(course) && !b) starredList.remove(course);
        else if (b) starredList.add(course);
    }

    @Override
    public boolean isStarred(Course course) {
        return starredList.contains(course);
    }

    /**
     * Получает сохранённый на устройстве список курсов
     */
    List<Course> loadStarredList() {
        ArrayList<Course> list = new ArrayList<>();

        // TODO

        return list;
    }
}
