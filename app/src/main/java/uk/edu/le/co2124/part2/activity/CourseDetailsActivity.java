package uk.edu.le.co2124.part2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.adapter.StudentAdapter;
import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.entity.Student;

public class CourseDetailsActivity extends AppCompatActivity {
    private TextView textCourseCode, textCourseName, textLecturerName;
    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;
    private int courseId;

    private final ActivityResultLauncher<Intent> addStudentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadStudents();  // Reload the student list
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // Initialize UI elements
        textCourseCode = findViewById(R.id.textCourseCode);
        textCourseName = findViewById(R.id.textCourseName);
        textLecturerName = findViewById(R.id.textLecturerName);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);

        // Get course ID from Intent
        courseId = getIntent().getIntExtra("courseId", -1);

        if (courseId != -1) {
            CourseManagementDB db = CourseManagementDB.getDatabase(this);

            // Observe course LiveData
            db.courseDao().getCourseById(courseId).observe(this, course -> {
                if (course != null) {
                    textCourseName.setText(course.courseName);
                    textCourseCode.setText("Course Code: " + course.courseCode);
                    textLecturerName.setText("Lecturer: " + course.lecturerName);
                }
            });

            // Load students
            loadStudents();
        }

        // FAB to add student
        FloatingActionButton fabAddStudent = findViewById(R.id.fabAddStudent);
        fabAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
            addStudentActivityResultLauncher.launch(intent);
        });
    }

    // Load and display students
    private void loadStudents() {
        CourseManagementDB.databaseWriteExecutor.execute(() -> {
            List<Student> students = CourseManagementDB.getDatabase(getApplicationContext())
                    .studentDao()
                    .getStudentsForCourse(courseId);

            Log.d("CourseDetailsActivity", "Students: " + students.size());

            runOnUiThread(() -> {
                if (studentAdapter == null) {
                    studentAdapter = new StudentAdapter(students, student -> {
                        Intent intent = new Intent(CourseDetailsActivity.this, StudentDetailsActivity.class);
                        intent.putExtra("studentId", student.studentId);
                        startActivity(intent);
                    });
                    recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
                    recyclerViewStudents.setAdapter(studentAdapter);
                } else {
                    studentAdapter.updateStudentList(students);
                }
            });
        });
    }
}
