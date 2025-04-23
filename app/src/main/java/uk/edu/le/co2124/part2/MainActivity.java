package uk.edu.le.co2124.part2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import uk.edu.le.co2124.part2.adapter.CourseAdapter;
import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.repository.CourseStudentRepository;

public class MainActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;
    private CourseStudentRepository courseStudentRepository;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and Adapter
        courseStudentRepository = new CourseStudentRepository(getApplication());


        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(this);
        recyclerViewCourses.setAdapter(courseAdapter);
        recyclerViewCourses.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Check if there's a selected item and clear it if user clicks anywhere else
                if (courseAdapter.getSelectedPosition() != -1) {
                    courseAdapter.clearSelection();
                    v.performClick(); // This will handle the click event properly for accessibility
                    return true;
                }
            }
            return false;
        });

        // Get ViewModel
        courseViewModel = new CourseViewModel(getApplication());

        // Observe LiveData from CourseRepository
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseAdapter.updateCourseList(courses);
            }
        });

        // Set up FloatingActionButton
        FloatingActionButton fabAddCourse = findViewById(R.id.fabAddCourse);
        fabAddCourse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateCourseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        courseViewModel.getAllCourses().observe(this, courses -> courseAdapter.updateCourseList(courses));
    }

    @Override
    public void onCourseClick(Course course) {
        // Navigate to CourseDetailsActivity with the clicked course
        Intent intent = new Intent(this, CourseDetailsActivity.class);
        intent.putExtra("courseId", course.courseId);  // Pass course ID or any other necessary details
        startActivity(intent);
    }

    @Override
    public void onCourseDelete(Course course) {
        courseStudentRepository.deleteCourseAndEnrollments(course);
        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        courseAdapter.clearSelection();
    }
}
