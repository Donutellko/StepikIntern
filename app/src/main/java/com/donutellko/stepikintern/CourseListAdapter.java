package com.donutellko.stepikintern;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CourseListAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private ArrayList<JsonHelper.RequestResponse.Course> courseList;

    public CourseListAdapter(ArrayList<JsonHelper.RequestResponse.Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_card, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        JsonHelper.RequestResponse.Course course = courseList.get(position);
        holder.title.setText(course.course_title);

        // int img_side = holder.cover.getLayoutParams().width;
        // holder.cover.setMinimumHeight(img_side); // Делаем квадратным
        // holder.cover.setMaxHeight(img_side); // Делаем квадратным

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView cover;

    CustomViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.card_title);
        cover = itemView.findViewById(R.id.card_cover);
    }
}