package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_data")
public class PlayerData {
    @PrimaryKey @NonNull @ColumnInfo(name = "playerid", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0")private int id;

    //Player Stats
    @ColumnInfo(name = "health", typeAffinity = ColumnInfo.INTEGER, defaultValue = "3") private int health;
    @ColumnInfo(name = "bounty", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bounty;
    @ColumnInfo(name = "taboo", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int taboo;
    @ColumnInfo(name = "tabooBonus", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int tabooBonus;
    @ColumnInfo(name = "luck", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int luck;
    @ColumnInfo(name = "bountyBonus", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bountyBonus;
    @ColumnInfo(name = "setBonus", defaultValue = TreasureList.EMPTY_SET_BONUS) private String setBonus;

    //Constructor. Tip: Delete constructor and use Alt+Insert when updating to avoid insanity
    public PlayerData(int id, int health, int bounty, int taboo, int tabooBonus, int luck, int bountyBonus, String setBonus) {
        this.id = id;
        this.health = health;
        this.bounty = bounty;
        this.taboo = taboo;
        this.tabooBonus = tabooBonus;
        this.luck = luck;
        this.bountyBonus = bountyBonus;
        this.setBonus = setBonus;
    }

    //Methods
    public int getHealth(){return this.health;}
    public int getId(){return this.id;}

    public int getBounty(){return this.bounty;}
    public int getTaboo(){return this.taboo;}
    public int getTabooBonus(){return this.tabooBonus;}
    public int getLuck(){return this.luck;}
    public int getBountyBonus(){return this.bountyBonus;}

    public void setId(){this.id = 0;}

    public void setHealth(int value){this.health = value;}
    public void setBounty(int value){this.bounty = value;}
    public void setLuck(int value){this.luck = value;}
    public void setTaboo(int value){this.taboo = value;}
    public void setTabooBonus(int value){this.tabooBonus = value;}
    public void setBountyBonus(int value){this.bountyBonus = value;}

    public String getSetBonus() {
        return setBonus;
    }

    public void setSetBonus(String setBonus) {
        this.setBonus = setBonus;
    }
}
