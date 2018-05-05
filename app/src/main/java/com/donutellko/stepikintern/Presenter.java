package com.donutellko.stepikintern;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.api.SearchRequestResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter {

    IView view;

    Presenter(IView view) {
        this.view = view;
    }

    public void showSearch(String query) {
        App.getStepikApi().getSearchResults("ielts").enqueue(new Callback<SearchRequestResult>() {
            @Override
            public void onResponse(Call<SearchRequestResult> call, Response<SearchRequestResult> response) {
                if (response.body() == null) {
                    view.showEmptyList();
                    return;
                }

                List<Course> courses = new ArrayList<>();
                SearchRequestResult requestResult = response.body();

                for (Course c : requestResult.getCourses()) {
                    if (c.getCourseTitle() != null)
                        courses.add(c);
                }

                view.showList(courses, requestResult.getMeta().getHasNext());
            }

            @Override
            public void onFailure(Call<SearchRequestResult> call, Throwable t) {
                view.showError(t);

                t.printStackTrace();
            }
        });

        view.showList(getTestList(), false);
    }

    public void showTest() {
        view.showList(getTestList(), false);
    }

    private ArrayList<Course> getTestList() {
        ArrayList<Course> courseList = new ArrayList<>();

        courseList.add(new Course(1133195,  "Научное мышление", "https://stepik.org/media/cache/images/courses/578/cover/eb8e8141f886654cc4a8667b483929cb.png"));
        courseList.add(new Course(38793712, "Теория аргументации", "https://stepik.org/media/cache/images/courses/4471/cover_jpiX2mZ/44b17da165ab3d7c93bce072f7dc3c6c.png"));
        courseList.add(new Course(38793712, "Логика", "https://stepik.org/media/cache/images/courses/4598/cover/b19b4436d1c06ae66a8e24d8b72ddeb0.png"));
        courseList.add(new Course(38793712, "Риторика", "https://stepik.org/media/cache/images/courses/4594/cover/b989e2bcd4018e2a06e54ca6984a9822.jpg"));
        courseList.add(new Course(736914,   "Java. Базовый курс", "https://stepik.org/media/cache/images/courses/187/cover/b5afd0216dbb45100bdcc6a607791cc2.png"));
        courseList.add(new Course(37547707, "Основы логики", "https://stepik.org/media/cache/images/courses/4270/cover/1ff73444e9e1ac100495fcfd44b79a2a.png"));
        courseList.add(new Course(43769607, "Быстрый старт в Android-разработку", "https://stepik.org/media/cache/images/courses/6022/cover/3e47e9ec12ace7e0de7a185313b7c74c.png"));
        courseList.add(new Course(35511496, "IT-интенсив (Python)", "https://stepik.org/media/cache/images/courses/6075/cover/ce3d71731e582963966fb9481ee75995.png"));
        courseList.add(new Course(777290,   "Политические процессы в современной России", "https://stepik.org/media/cache/images/courses/132/cover/4e82d78955f0f6fc40cf14f074706966.jpg"));
        courseList.add(new Course(651763,   "Основы статистики", "https://stepik.org/media/cache/images/courses/76/cover_mp4DfiD/306acff9258c690702002d0b651abf35.jpg"));
        courseList.add(new Course(34379223, "Базы данных", "https://stepik.org/media/cache/images/courses/2614/cover/ed9778b8b9989f7e414ae34e726cee3b.jpg"));
        courseList.add(new Course(33141053, "Электроника и схематехника аналоговых устройств", "https://stepik.org/media/cache/images/courses/3005/cover/20f220cdbfdeb70120db1d43faf742af.gif"));
        courseList.add(new Course(651763,   "Программирование на Python", "https://stepik.org/media/cache/images/courses/67/cover/cf7621ccee379e4bf27d7cd6927adf2a.png"));
        courseList.add(new Course(1382921 , "Web-технологии", "https://stepik.org/media/cache/images/courses/154/cover/c2f8c6d2e5cb22ddff5ebb5c27a84ee2.png"));
        courseList.add(new Course(33853692, "Программирование на Pascal", "https://stepik.org/media/cache/images/courses/3352/cover/34bf1ef6162f4cfb79358fa2cdd81171.jpeg"));
        courseList.add(new Course(777203,   "Операционные системы", "https://stepik.org/media/cache/images/courses/1780/cover/9418453098c82a7f7fb5142548f1330f.png"));
        courseList.add(new Course(36708621, "Основы работы в iRidium pro", "https://stepik.org/media/cache/images/courses/4902/cover/018e715cf8a70de3c000b4e6662d0d2b.png"));
        courseList.add(new Course(1382921,  "Анализ безопасности веб-проектов", "https://stepik.org/media/cache/images/courses/127/cover/343e92e4cfd2c0d550289242e388b573.png"));
        courseList.add(new Course(1382921,  "Алгоритмы и структуры данных", "https://stepik.org/media/cache/images/courses/156/cover/c52e2ae8ccca4f8d8cd271edb16945e6.png"));
        courseList.add(new Course(36708621, "Работа в iRidium lite", "https://stepik.org/media/cache/images/courses/4700/cover/101c1b81cf7dfaa4654ca1766cca8d47.png"));

        return courseList;
    }

}
