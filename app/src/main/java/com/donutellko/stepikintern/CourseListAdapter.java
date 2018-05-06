package com.donutellko.stepikintern;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.donutellko.stepikintern.api.Course;
import com.donutellko.stepikintern.mvp.IPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

class CourseListAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<Course> courseList;
    IPresenter presenter;

    public CourseListAdapter(List<Course> courseList, IPresenter presenter) {
        this.courseList = courseList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_card, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final Course course = courseList.get(position);
        holder.title.setText(course.getCourseTitle());
        Picasso.get()
                .load(course.getCourseCover())
                .into(holder.cover);

        holder.setStarVisibility(presenter.isStarred(course));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isStarred(course)) {
                    presenter.setStarred(course, false);
                    holder.setStarVisibility(false);
                } else {
                    presenter.setStarred(course, true);
                    holder.setStarVisibility(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void appendCourses(List<Course> courses) {
        courseList.addAll(courses);
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView title;
    ImageView cover;
    ImageView star;

    CustomViewHolder(View itemView) {
        super(itemView);

        this.view = itemView;
        title = itemView.findViewById(R.id.card_title);
        cover = itemView.findViewById(R.id.card_cover);
        star = itemView.findViewById(R.id.card_star);
    }

    void setStarVisibility(boolean b) {
        star.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }
}
