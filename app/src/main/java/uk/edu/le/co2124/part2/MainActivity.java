package uk.edu.le.co2124.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import uk.edu.le.co2124.part2.adapter.CourseAdapter;
import uk.edu.le.co2124.part2.database.entity.Course;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and Adapter
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(null);
        recyclerViewCourses.setAdapter(courseAdapter);

        // Get ViewModel
        courseViewModel = new CourseViewModel(getApplication());

        // Observe LiveData from CourseRepository
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                Log.d("MainActivity", "onChanged: " + courses.size());
                // Update the adapter with new courses
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
}
