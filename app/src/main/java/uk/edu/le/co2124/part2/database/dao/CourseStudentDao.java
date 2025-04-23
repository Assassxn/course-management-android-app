package uk.edu.le.co2124.part2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uk.edu.le.co2124.part2.database.entity.CourseStudentCrossRef;

@Dao
public interface CourseStudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void enrollStudent(CourseStudentCrossRef crossRef);

    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId")
    void removeAllStudentsFromCourse(int courseId);

    @Query("DELETE FROM CourseStudentCrossRef WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);
}