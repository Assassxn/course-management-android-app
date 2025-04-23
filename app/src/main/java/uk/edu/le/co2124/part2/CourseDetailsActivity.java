package uk.edu.le.co2124.part2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.edu.le.co2124.part2.adapter.StudentAdapter;
import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.entity.Student;

public class CourseDetailsActivity extends AppCompatActivity {
    private TextView textCourseCode, textCourseName, textLecturerName;
    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        textCourseCode = findViewById(R.id.textCourseCode);
        textCourseName = findViewById(R.id.textCourseName);
        textLecturerName = findViewById(R.id.textLecturerName);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);

        int courseId = getIntent().getIntExtra("courseId", -1);

        if (courseId != -1) {
            CourseManagementDB db = CourseManagementDB.getDatabase(this);

            // 1. Observe course LiveData on the main thread
            db.courseDao().getCourseById(courseId).observe(this, course -> {
                if (course != null) {
                    textCourseCode.setText("Course Code: " + course.courseCode);
                    textCourseName.setText(course.courseName);
                    textLecturerName.setText("Lecturer: " + course.lecturerName);
                }
            });

            // 2. Load student list in background, then update UI
            CourseManagementDB.databaseWriteExecutor.execute(() -> {
                List<Student> students = db.courseStudentDao().getStudentsForCourse(courseId);
                Log.d("CourseDetailsActivity", "Students: " + students.size());
                runOnUiThread(() -> {
                    studentAdapter = new StudentAdapter(students);
                    recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
                    recyclerViewStudents.setAdapter(studentAdapter);
                });
            });
        }
    }
}