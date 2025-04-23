package uk.edu.le.co2124.part2.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.database.relations.CourseWithStudents;

@Dao
public interface CourseDao {
    @Insert
    long insert(Course course); // Must be long for Room insert

    @Delete
    int delete(Course course); // int is valid for delete (number of rows affected)

    @Query("SELECT * FROM Course ORDER BY courseName ASC")
    LiveData<List<Course>> getAllCourses();

    @Transaction
    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    LiveData<CourseWithStudents> getCourseWithStudentsLive(int courseId);

    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    LiveData<Course> getCourseById(int courseId);
}
