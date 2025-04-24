package uk.edu.le.co2124.part2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.CourseStudentCrossRef;
import uk.edu.le.co2124.part2.database.entity.Student;

@Dao
public interface CourseStudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void enrollStudent(CourseStudentCrossRef crossRef);

    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId")
    void removeAllStudentsFromCourse(int courseId);

    @Query("DELETE FROM CourseStudentCrossRef WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);

    @Query("SELECT * FROM Student WHERE studentId IN (SELECT studentId FROM CourseStudentCrossRef WHERE courseId = :courseId)")
    List<Student> getStudentsForCourse(int courseId);

    @Query("SELECT EXISTS(SELECT 1 FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId)")
    Boolean isStudentEnrolledInCourse(int courseId, int studentId);
}