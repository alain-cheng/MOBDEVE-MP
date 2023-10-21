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
    public int getStat(String stat){
        switch (stat){
            case "health":{
                return this.health;
            }
            case "bounty":{
                return this.bounty;
            }
            case "diffMultiplier":{
                return this.diffMultiplier;
            }
            case "luck":{
                return this.luck;
            }
            case "bountyBonus":{
                return this.bountyBonus;
            }
            default:{
                return 0;
            }
        }
    }

    public void setStat(String stat, int value){
        switch (stat){
            case "health":{
                this.health = value;
                break;
            }
            case "bounty":{
                this.bounty = value;
                break;
            }
            case "diffMultiplier":{
                this.diffMultiplier = value;
                break;
            }
            case "luck":{
                this.luck = value;
                break;
            }
            case "bountyBonus":{
                this.bountyBonus = value;
                break;
            }
            default:{
                Log.v("ERROR", "Stat does not exists!");
            }
        }
    }
}
