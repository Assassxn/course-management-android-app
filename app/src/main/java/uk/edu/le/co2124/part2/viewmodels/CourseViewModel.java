package uk.edu.le.co2124.part2.viewmodels;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.repository.CourseRepository;

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository courseRepository;

    public CourseViewModel(Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
    }

    public LiveData<List<Course>> getAllCourses() {
        return courseRepository.getAllCourses();
    }
}
