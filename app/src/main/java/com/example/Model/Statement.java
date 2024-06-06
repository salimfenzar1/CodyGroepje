package com.example.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "statement"


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

    @ColumnInfo(name = "isActive")
    public boolean isActive;



    @ColumnInfo(name = "intensityLevel")
    public int intensityLevel;

    public int getStatementId() {
        return statementId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getIntensityLevel() {
        return intensityLevel;
    }

    public void setIntensityLevel(int intensityLevel) {
        this.intensityLevel = intensityLevel;
    }
}
