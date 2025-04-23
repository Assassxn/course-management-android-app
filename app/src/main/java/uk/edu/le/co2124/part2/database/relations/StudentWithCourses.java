package uk.edu.le.co2124.part2.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import uk.edu.le.co2124.part2.database.entity.Course;
import uk.edu.le.co2124.part2.database.entity.CourseStudentCrossRef;
import uk.edu.le.co2124.part2.database.entity.Student;

public class StudentWithCourses {
    @Embedded
    public Student student;

    @Relation(
            parentColumn = "studentId",
            entityColumn = "courseId",
            associateBy = @Junction(CourseStudentCrossRef.class)
    )
    public List<Course> courses;
}