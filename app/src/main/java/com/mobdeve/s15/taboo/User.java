package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username", defaultValue = "")private String username;
    @NonNull
    @ColumnInfo(name = "email", defaultValue = "")private String email;

    public User(@NonNull String username, @NonNull String email) {
        this.username = username;
        this.email = email;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
