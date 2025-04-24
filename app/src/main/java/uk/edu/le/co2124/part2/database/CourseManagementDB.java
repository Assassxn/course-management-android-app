package uk.edu.le.co2124.part2.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.edu.le.co2124.part2.database.dao.CourseDao;
import uk.edu.le.co2124.part2.database.dao.CourseStudentDao;
import uk.edu.le.co2124.part2.database.dao.StudentDao;
import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.database.entity.CourseStudentCrossRef;
import uk.edu.le.co2124.part2.database.entity.Student;

@Database(entities = {Course.class, Student.class, CourseStudentCrossRef.class}, version = 1, exportSchema = false)
public abstract class CourseManagementDB extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract StudentDao studentDao();
    public abstract CourseStudentDao courseStudentDao();

    private static volatile CourseManagementDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CourseManagementDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CourseManagementDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CourseManagementDB.class, "course_management_db").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
//                CourseDao courseDao = INSTANCE.courseDao();
//                StudentDao studentDao = INSTANCE.studentDao();
//                CourseStudentDao courseStudentDao = INSTANCE.courseStudentDao();
//
//                // Insert courses
//                Course course1 = new Course();
//                course1.courseCode = "CS101";
//                course1.courseName = "Introduction to Computer Science";
//                course1.lecturerName = "John Smith";
//                long course1Id = courseDao.insert(course1);
//
//                Course course2 = new Course();
//                course2.courseCode = "CS202";
//                course2.courseName = "Data Structures and Algorithms";
//                course2.lecturerName = "Jane Doe";
//                long course2Id = courseDao.insert(course2);
//
//                // Insert students
//                Student student1 = new Student();
//                student1.name = "Alice Johnson";
//                student1.email = "alice.johnson@example.com";
//                student1.userName = "alicej";
//                long student1Id = studentDao.insert(student1);
//
//                Student student2 = new Student();
//                student2.name = "Bob Smith";
//                student2.email = "bob.smith@example.com";
//                student2.userName = "bobs";
//                long student2Id = studentDao.insert(student2);
//
//                // Enroll students in courses
//                CourseStudentCrossRef crossRef1 = new CourseStudentCrossRef();
//                crossRef1.courseId = (int) course1Id;
//                crossRef1.studentId = (int) student1Id;
//                courseStudentDao.enrollStudent(crossRef1);
//
//                CourseStudentCrossRef crossRef2 = new CourseStudentCrossRef();
//                crossRef2.courseId = (int) course1Id;
//                crossRef2.studentId = (int) student2Id;
//                courseStudentDao.enrollStudent(crossRef2);
//
//                CourseStudentCrossRef crossRef3 = new CourseStudentCrossRef();
//                crossRef3.courseId = (int) course2Id;
//                crossRef3.studentId = (int) student1Id;
//                courseStudentDao.enrollStudent(crossRef3);
            });
        }
    };
}
