package uk.edu.le.co2124.part2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.relations.CourseWithStudents;
import uk.edu.le.co2124.part2.database.dao.CourseDao;
import uk.edu.le.co2124.part2.database.entity.Course;

public class CourseRepository {
    private CourseDao courseDao;
    private ExecutorService executorService;

    public CourseRepository(Application application) {
        CourseManagementDB db = CourseManagementDB.getDatabase(application);
        courseDao = db.courseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Course course) {
        executorService.execute(() -> courseDao.insert(course));
    }

    public void delete(Course course) {
        executorService.execute(() -> courseDao.delete(course));
    }

    public LiveData<CourseWithStudents> getCourseWithStudents(int courseId) {
        return courseDao.getCourseWithStudentsLive(courseId);
    }

    public LiveData<List<Course>> getAllCourses() {
        return courseDao.getAllCourses();
    }
}
