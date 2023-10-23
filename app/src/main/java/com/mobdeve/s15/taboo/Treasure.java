package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treasury")
public class Treasure {
    @PrimaryKey @NonNull @ColumnInfo(name = "itemid")private String id;
    @NonNull @ColumnInfo(name = "name")private String name;
    @NonNull @ColumnInfo(name = "imageid")private int imageid;
    @NonNull @ColumnInfo(name = "itembonus")private String itemBonus;
    @NonNull @ColumnInfo(name = "lore")private String lore;
    @NonNull @ColumnInfo(name = "rarity")private String rarity;
    @ColumnInfo(name = "count", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1")private int count;

    public Treasure(@NonNull String id, @NonNull String name, int imageid, @NonNull String itemBonus, @NonNull String lore, @NonNull String rarity, int count) {
        this.id = id;
        this.name = name;
        this.imageid = imageid;
        this.itemBonus = itemBonus;
        this.lore = lore;
        this.rarity = rarity;
        this.count = count;
    }

    //Methods
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public int getImageid() {
        return imageid;
    }

    public String getItemBonus() {
        return itemBonus;
    }

    public String getLore() {
        return lore;
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
