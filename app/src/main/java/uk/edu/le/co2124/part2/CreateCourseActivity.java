package uk.edu.le.co2124.part2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.entity.Course;

public class CreateCourseActivity extends AppCompatActivity {
    private EditText editCourseCode, editCourseName, editLecturer;
    private Button btnCreateCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        editCourseCode = findViewById(R.id.editCourseCode);
        editCourseName = findViewById(R.id.editCourseName);
        editLecturer = findViewById(R.id.editLecturer);
        btnCreateCourse = findViewById(R.id.btnCreateCourse);

        btnCreateCourse.setOnClickListener(v -> {
            String code = editCourseCode.getText().toString().trim();
            String name = editCourseName.getText().toString().trim();
            String lecturer = editLecturer.getText().toString().trim();

            if (!code.isEmpty() && !name.isEmpty() && !lecturer.isEmpty()) {
                Course newCourse = new Course();
                newCourse.courseCode = code;
                newCourse.courseName = name;
                newCourse.lecturerName = lecturer;

                CourseManagementDB.databaseWriteExecutor.execute(() -> {
                    CourseManagementDB.getDatabase(getApplicationContext()).courseDao().insert(newCourse);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Course created", Toast.LENGTH_SHORT).show();
                        finish(); // Return to MainActivity
                    });
                });
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
