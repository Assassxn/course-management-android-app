package uk.edu.le.co2124.part2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.edu.le.co2124.part2.database.CourseManagementDB;
import uk.edu.le.co2124.part2.database.relations.StudentWithCourses;
import uk.edu.le.co2124.part2.database.dao.StudentDao;
import uk.edu.le.co2124.part2.database.entity.Student;

public class StudentRepository {
    private StudentDao studentDao;
    private ExecutorService executorService;

    public StudentRepository(Application application) {
        CourseManagementDB db = CourseManagementDB.getDatabase(application);
        studentDao = db.studentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void delete(Student student) {
        executorService.execute(() -> studentDao.delete(student));
    }

    public LiveData<StudentWithCourses> getStudentWithCourses(int studentId) {
        return studentDao.getStudentWithCoursesLive(studentId);
    }
}
