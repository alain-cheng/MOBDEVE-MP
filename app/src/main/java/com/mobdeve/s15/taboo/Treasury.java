package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treasury")
public class Treasury {
    private String[] treasureList = {"Black_Ash", "Acacia_Bark", "Giant_Inhaler", "Kapre_Cigar"}; //Register new Treasures here
    @PrimaryKey @NonNull @ColumnInfo(name = "name")private String name;
    @NonNull @ColumnInfo(name = "rarity")private String rarity;
    @ColumnInfo(name = "count", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1")private int count;

    public Treasury(@NonNull String name, @NonNull String rarity, int count) {
        this.name = name;
        this.rarity = rarity;
        this.count = count;
    }

    public String[] getTreasureList(){
        return treasureList;
    }
}
