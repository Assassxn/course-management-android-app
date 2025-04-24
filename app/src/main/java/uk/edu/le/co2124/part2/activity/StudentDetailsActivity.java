package uk.edu.le.co2124.part2.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.adapter.CourseAdapter;
import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.database.entity.Student;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView textName, textEmail, textMatric;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        textName = findViewById(R.id.textStudentName);
        textEmail = findViewById(R.id.textStudentEmail);
        textMatric = findViewById(R.id.textStudentMatric);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        studentId = getIntent().getIntExtra("studentId", -1);

        if (studentId != -1) {
            loadStudentDetails();
            loadEnrolledCourses();
        }
    }

    private void loadStudentDetails() {
        CourseManagementDB.databaseWriteExecutor.execute(() -> {
            Student student = CourseManagementDB.getDatabase(getApplicationContext())
                    .studentDao()
                    .getStudentByStudentId(studentId);

            if (student != null) {
                runOnUiThread(() -> {
                    textName.setText("Name: " + student.name);
                    textEmail.setText("Email: " + student.email);
                    textMatric.setText("Matric No: " + student.userName);
                });
            }
        });
    }

    private void loadEnrolledCourses() {
        CourseManagementDB.databaseWriteExecutor.execute(() -> {
            List<Course> courses = CourseManagementDB.getDatabase(getApplicationContext())
                    .courseDao()
                    .getCoursesForStudent(studentId);

            runOnUiThread(() -> {
                // Adapter with readOnly=true to disable long press & delete button
                courseAdapter = new CourseAdapter(new CourseAdapter.OnCourseClickListener() {
                    @Override
                    public void onCourseClick(Course course) {
                        // You can add course detail view here if needed
                    }

                    @Override
                    public void onCourseDelete(Course course) {
                        // Delete should never be triggered in read-only mode
                    }
                }, true); // true = readOnly

                courseAdapter.updateCourseList(courses);
                recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewCourses.setAdapter(courseAdapter);
            });
        });
    }
}
