package com.example.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "statement")
public class Statement implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int statementId;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "imageUrl")
    public String imageUrl;

    @ColumnInfo(name = "pictureName")
    public String pictureName;

    @ColumnInfo(name = "isActive")
    public boolean isActive;

    @ColumnInfo(name = "intensityLevel")
    public int intensityLevel;

    public Statement() {
        // Default constructor
    }

    public boolean isActive() {
        return isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    protected Statement(Parcel in) {
        statementId = in.readInt();
        description = in.readString();
        category = in.readString();
        imageUrl = in.readString();
        pictureName = in.readString();
        isActive = in.readByte() != 0;
        intensityLevel = in.readInt();
    }

    public static final Creator<Statement> CREATOR = new Creator<Statement>() {
        @Override
        public Statement createFromParcel(Parcel in) {
            return new Statement(in);
        }

        @Override
        public Statement[] newArray(int size) {
            return new Statement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statementId);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeString(pictureName);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(intensityLevel);
    }
}
