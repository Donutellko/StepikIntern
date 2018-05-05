package com.donutellko.stepikintern.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Имеет только те поля, которые требуются для отображения и хранения списка.
 */
public class Course {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("score")
    @Expose
    private Double score;

    @SerializedName("course")
    @Expose
    private Integer course;

    @SerializedName("course_owner")
    @Expose
    private Integer courseOwner;

    @SerializedName("course_title")
    @Expose
    private String courseTitle;

    @SerializedName("course_cover")
    @Expose
    private String courseCover;

    /**
     * Конструктор для тестов
     * @param title название курса courseTitle
     * @param url Ссылка на обложку курса
     */
    public Course(int owner, String title, String url) {
        this.courseOwner = owner;
        this.courseTitle = title;
        this.courseCover = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getCourseOwner() {
        return courseOwner;
    }

    public void setCourseOwner(Integer courseOwner) {
        this.courseOwner = courseOwner;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCover() {
        return courseCover;
    }

    public void setCourseCover(String courseCover) {
        this.courseCover = courseCover;
    }
}
