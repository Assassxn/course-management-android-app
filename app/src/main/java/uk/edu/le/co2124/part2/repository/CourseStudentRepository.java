package uk.edu.le.co2124.part2.repository;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.dao.CourseDao;
import uk.edu.le.co2124.part2.database.dao.CourseStudentDao;
import uk.edu.le.co2124.part2.database.entity.Course;

public class CourseStudentRepository {
    private CourseDao courseDao;
    private CourseStudentDao courseStudentDao;
    private ExecutorService executorService;

    public CourseStudentRepository(Application application) {
        CourseManagementDB db = CourseManagementDB.getDatabase(application);
        courseDao = db.courseDao();
        courseStudentDao = db.courseStudentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void deleteCourseAndEnrollments(Course course) {
        executorService.execute(() -> {
            courseStudentDao.removeAllStudentsFromCourse(course.courseId);
            courseDao.delete(course);
        });
    }
}
