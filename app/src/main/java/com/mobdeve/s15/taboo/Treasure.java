package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treasury")
public class Treasure {
    @PrimaryKey @NonNull @ColumnInfo(name = "name")private String name;
    @NonNull @ColumnInfo(name = "rarity")private String rarity;
    @ColumnInfo(name = "count", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1")private int count;

    public Treasure(@NonNull String name, @NonNull String rarity, int count) {
        this.name = name;
        this.rarity = rarity;
        this.count = count;
    }

    //Methods
    public String getName(){
        return this.name;
    }

    public String getRarity(){
        return this.rarity;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int value){
        this.count = value;
    }
}
