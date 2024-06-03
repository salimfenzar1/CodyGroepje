package com.example.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "intensity")
public class Intensity {

    @PrimaryKey(autoGenerate = true)
    public int intensityId;
    public String intensityLevel;


}
