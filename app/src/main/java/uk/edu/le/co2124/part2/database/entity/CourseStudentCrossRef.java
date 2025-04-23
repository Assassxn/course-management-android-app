package uk.edu.le.co2124.part2.database.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"courseId", "studentId"})
public class CourseStudentCrossRef {
    public int courseId;
    public int studentId;
}
