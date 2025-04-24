package uk.edu.le.co2124.part2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.database.entity.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList = new ArrayList<>();
    private final OnCourseClickListener listener;
    private int selectedPosition = -1;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
        void onCourseDelete(Course course);
    }

    public CourseAdapter(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public void updateCourseList(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void clearSelection() {
        int oldPos = selectedPosition;
        selectedPosition = -1;
        notifyItemChanged(oldPos);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseCode.setText(course.courseCode);
        holder.courseName.setText(course.courseName);
        holder.lecturerName.setText(course.lecturerName);

        boolean shouldShowDelete = (position == selectedPosition);

        if (shouldShowDelete) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.buttonDelete.setAlpha(0f);
            holder.buttonDelete.setTranslationY(30f);
            holder.buttonDelete.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(250)
                    .start();
        } else {
            holder.buttonDelete.animate()
                    .alpha(0f)
                    .translationY(30f)
                    .setDuration(250)
                    .withEndAction(() -> holder.buttonDelete.setVisibility(View.GONE))
                    .start();
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition != -1 && selectedPosition != position) {
                int prevPos = selectedPosition;
                selectedPosition = -1;
                notifyItemChanged(prevPos); // Hide previous delete
            } else if (selectedPosition == position) {
                selectedPosition = -1;
                notifyItemChanged(position);
            } else {
                listener.onCourseClick(course); // Normal click to view course details
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = (selectedPosition == position) ? -1 : position;

            if (previous != -1) notifyItemChanged(previous);
            notifyItemChanged(position);
            return true;
        });

        holder.buttonDelete.setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = -1;
            notifyItemChanged(previous);
            listener.onCourseDelete(course); // Handle course deletion
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseCode, courseName, lecturerName;
        Button buttonDelete;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.courseCode);
            courseName = itemView.findViewById(R.id.courseName);
            lecturerName = itemView.findViewById(R.id.lecturerName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
