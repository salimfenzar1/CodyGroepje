package com.example.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "statement",
        foreignKeys = @ForeignKey(
                entity = Intensity.class,
                parentColumns = "intensityId",
                childColumns = "intensityId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Statement {
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
    public int intensityId; // This is the foreign key column
}
