package uk.edu.le.co2124.part2.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"courseCode"}, unique = true)})
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int courseId;

    @NonNull
    public String courseCode;

    @NonNull
    public String courseName;

    @NonNull
    public String lecturerName;
}
