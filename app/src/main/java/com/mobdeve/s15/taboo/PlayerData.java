package com.mobdeve.s15.taboo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_data")
public class PlayerData {
    @PrimaryKey @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0")private int id;

    //Player Stats
    @ColumnInfo(name = "health", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1") private int health;
    @ColumnInfo(name = "bounty", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bounty;
    @ColumnInfo(name = "diffMultiplier", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1") private int diffMultiplier;
    @ColumnInfo(name = "luck", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int luck;
    @ColumnInfo(name = "bountyBonus", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bountyBonus;

    //Constructor. Tip: Delete constructor and use Alt+Insert when updating to avoid insanity
    public PlayerData(int id, int health, int bounty, int diffMultiplier, int luck, int bountyBonus) {
        this.id = id;
        this.health = health;
        this.bounty = bounty;
        this.diffMultiplier = diffMultiplier;
        this.luck = luck;
        this.bountyBonus = bountyBonus;
    }

    //Methods
    public int getHealth(){return this.health;}
    public int getId(){return this.id;}
    public int getBounty(){return this.bounty;}
    public int getDiffMultiplier(){return this.diffMultiplier;}
    public int getLuck(){return this.luck;}
    public int getBountyBonus(){return this.bountyBonus;}

    public void setId(){this.id = 0;}
    public void setHealth(int value){this.health = value;}
    public void setBounty(int value){this.bounty = bounty;}
    public void setLuck(int value){this.luck = luck;}
    public void setDiffMultiplier(int value){this.diffMultiplier = diffMultiplier;}
    public void setBountyBonus(int value){this.bountyBonus = bountyBonus;}
}
