package com.example.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.example.Model.Intensity;

@Entity(tableName = "statement")
public class Statement {

    @ColumnInfo(name = "statementId")
    @PrimaryKey(autoGenerate = true)
    public int statementId;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "imageUrl")
    public String imageUrl;

    @ColumnInfo(name = "gameName")
    public String gameName;

    @ColumnInfo(name = "intensityId")
    @ForeignKey(entity = Intensity.class, parentColumns = "Id", childColumns = "intensityId")
    public int intensityId;


}
