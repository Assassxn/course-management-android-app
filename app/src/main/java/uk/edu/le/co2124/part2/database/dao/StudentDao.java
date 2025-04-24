package uk.edu.le.co2124.part2.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.Student;
import uk.edu.le.co2124.part2.database.relations.StudentWithCourses;

@Dao
public interface StudentDao {
    @Insert
    long insert(Student student); // Updated to long for Room insert

    @Delete
    int delete(Student student);

    @Update
    void updateStudent(Student student);
    @Query("SELECT * FROM Student ORDER BY name ASC")
    LiveData<List<Student>> getAllStudents();

    @Transaction
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<StudentWithCourses> getStudentWithCoursesLive(int studentId);

    @Query("SELECT * FROM Student WHERE studentId = :studentId LIMIT 1")
    Student getStudentByStudentId(int studentId);

    @Query("SELECT * FROM Student WHERE userName = :userName LIMIT 1")
    Student getStudentByUsername(String userName);

    @Query("SELECT * FROM student WHERE studentId IN (SELECT studentId FROM coursestudentcrossref WHERE courseId = :courseId)")
    List<Student> getStudentsForCourse(int courseId);
}
