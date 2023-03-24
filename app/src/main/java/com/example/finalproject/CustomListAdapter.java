package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private LayoutInflater inflater;//Similar to recyclerview, basically a custom recyclerview?
    public CustomListAdapter(Context context, List<Course> courseList){
        this.context = context;
        this.courseList = courseList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //This is step 8 of https://www.androhub.com/room-database-android-jetpack-part-1/
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.course_data, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Course course = courseList.get(i);
        viewHolder.bindData(course);
        return view;
    }

    public void removeList(int index){ //For removing and instantly updating
        courseList.remove(index);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView label;

        public ViewHolder(View view) { //this ViewHolder is the custom one??
            label = view.findViewById(R.id.courseList);
        }

        void bindData(Course course) {
            label.setText(course.getUsername() + " ---- "  + course.getPassword()); //TODO This should output courseNum and courseName
        }
    }

}

