package uk.edu.le.co2124.part2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.database.entity.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private final OnStudentClickListener listener;
    private int selectedPosition = -1;  // To track the long-pressed item

    // Interface to handle click events
    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    // Constructor with student list and listener
    public StudentAdapter(List<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.textName.setText("Name: " + student.name);
        holder.textEmail.setText("Email: " + student.email);
        holder.textMatric.setText("Matric No: " + student.userName); // Assuming userName is matric

        // Handle click
        holder.itemView.setOnClickListener(v -> listener.onStudentClick(student));

        // Handle long press (set selected position)
        holder.itemView.setOnLongClickListener(v -> {
            selectedPosition = position;
            return false;  // Allow normal click behavior after long press
        });
    }

    @Override
    public int getItemCount() {
        return studentList == null ? 0 : studentList.size();
    }

    // Update the student list and notify changes
    public void updateStudentList(List<Student> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }

    // Method to get the selected student based on long press
    public Student getStudentAtPosition(int position) {
        return studentList != null && position >= 0 && position < studentList.size() ? studentList.get(position) : null;
    }

    // Method to get the selected position
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textEmail, textMatric;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textStudentName);
            textEmail = itemView.findViewById(R.id.textStudentEmail);
            textMatric = itemView.findViewById(R.id.textStudentMatric);
        }
    }
}
