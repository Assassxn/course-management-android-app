package uk.edu.le.co2124.part2.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.Student;
import uk.edu.le.co2124.part2.database.relations.StudentWithCourses;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM Student ORDER BY name ASC")
    LiveData<List<Student>> getAllStudents();

    @Transaction
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<StudentWithCourses> getStudentWithCoursesLive(int studentId);
}