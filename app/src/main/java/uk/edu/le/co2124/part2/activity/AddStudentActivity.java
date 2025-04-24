package uk.edu.le.co2124.part2.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.dao.CourseStudentDao;
import uk.edu.le.co2124.part2.database.dao.StudentDao;
import uk.edu.le.co2124.part2.database.entity.CourseStudentCrossRef;
import uk.edu.le.co2124.part2.database.entity.Student;

public class AddStudentActivity extends AppCompatActivity {

    private EditText editName, editEmail, editStudentUsername;
    private Button btnAddStudent;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editStudentUsername = findViewById(R.id.editStudentUsername);
        btnAddStudent = findViewById(R.id.btnAddStudent);

        courseId = getIntent().getIntExtra("courseId", -1);

        btnAddStudent.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String username = editStudentUsername.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            CourseManagementDB.databaseWriteExecutor.execute(() -> {
                StudentDao studentDao = CourseManagementDB.getDatabase(getApplicationContext()).studentDao();
                CourseStudentDao courseStudentDao = CourseManagementDB.getDatabase(getApplicationContext()).courseStudentDao();

                Student existing = studentDao.getStudentByUsername(username);
                int studentId;

                if (existing == null) {
                    // New student, insert into DB
                    Student student = new Student();
                    student.name = name;
                    student.email = email;
                    student.userName = username;
                    studentId = (int) studentDao.insert(student);
                } else {
                    studentId = existing.studentId;
                    if (courseStudentDao.isStudentEnrolledInCourse(courseId, studentId)) {
                        runOnUiThread(() -> Toast.makeText(this, "Student already enrolled", Toast.LENGTH_SHORT).show());
                        return;
                    }
                }

                // Enroll student in course
                CourseStudentCrossRef ref = new CourseStudentCrossRef();
                ref.courseId = courseId;
                ref.studentId = studentId;
                courseStudentDao.enrollStudent(ref);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Student added and enrolled", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Notify CourseDetailsActivity to refresh
                    finish();
                });
            });
        });


    }
}
