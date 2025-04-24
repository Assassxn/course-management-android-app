package uk.edu.le.co2124.part2.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uk.edu.le.co2124.part2.R;
import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.entity.Student;

public class EditStudentActivity extends AppCompatActivity {

    private EditText editName, editEmail, editMatric;
    private Button btnSave;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        // Initialize UI elements
        editName = findViewById(R.id.editTextName);
        editEmail = findViewById(R.id.editTextEmail);
        editMatric = findViewById(R.id.editTextMatric);
        btnSave = findViewById(R.id.buttonSave);

        // Get studentId from Intent
        studentId = getIntent().getIntExtra("studentId", -1);

        // Load the current student details
        loadStudentDetails();

        btnSave.setOnClickListener(v -> {
            updateStudent();
        });
    }

    private void loadStudentDetails() {
        CourseManagementDB.databaseWriteExecutor.execute(() -> {
            // Fetch student by ID
            Student student = CourseManagementDB.getDatabase(getApplicationContext())
                    .studentDao()
                    .getStudentByStudentId(studentId);

            runOnUiThread(() -> {
                if (student != null) {
                    editName.setText(student.name);
                    editEmail.setText(student.email);
                    editMatric.setText(student.userName);  // Assuming userName is matric number
                }
            });
        });
    }

    private void updateStudent() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String matric = editMatric.getText().toString();

        if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create updated student object
        Student updatedStudent = new Student();
        updatedStudent.studentId = studentId;
        updatedStudent.name = name;
        updatedStudent.email = email;
        updatedStudent.userName = matric;  // Assuming userName is matric number

        // Update student in the database
        CourseManagementDB.databaseWriteExecutor.execute(() -> {
            CourseManagementDB.getDatabase(getApplicationContext())
                    .studentDao()
                    .updateStudent(updatedStudent);

            runOnUiThread(() -> {
                Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);  // Notify that the result is OK
                finish();  // Close the activity and go back
            });
        });
    }
}
