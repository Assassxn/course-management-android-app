package uk.edu.le.co2124.part2.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"userName"}, unique = true)})
public class Student {
    @PrimaryKey(autoGenerate = true)
    public int studentId;

    @NonNull
    public String name;

    @NonNull
    public String email;

    @NonNull
    public String userName;
}