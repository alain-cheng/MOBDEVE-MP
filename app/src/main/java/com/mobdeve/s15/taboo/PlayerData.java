package com.mobdeve.s15.taboo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_data")
public class PlayerData {
    @PrimaryKey @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)private int id;

    //Player Stats
    @ColumnInfo(name = "health", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1") private int health;
    @ColumnInfo(name = "bounty", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bounty;
    @ColumnInfo(name = "diffMultiplier", typeAffinity = ColumnInfo.INTEGER, defaultValue = "1") private int diffMultiplier;
    @ColumnInfo(name = "luck", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int luck;
    @ColumnInfo(name = "bountyBonus", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int bountyBonus;

    //Treasure counts
    private String[] treasureList = {"Black_Ash", "Acacia_Bark", "Giant_Inhaler", "Kapre_Cigar"}; //Register new Treasures here
    //Tree Giant
    @ColumnInfo(name = "Black_Ash", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int Black_Ash;
    @ColumnInfo(name = "Acacia_Bark", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int Acacia_Bark;
    @ColumnInfo(name = "Giant_Inhaler", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int Giant_Inhaler;
    @ColumnInfo(name = "Kapre_Cigar", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0") private int Kapre_Cigar;

    //Conquest
    //Note: Implement other treasures later

    //Constructor. Tip: Delete constructor and use Alt+Insert when updating to avoid insanity
    public PlayerData(int id, int health, int bounty, int diffMultiplier, int luck, int bountyBonus, String[] treasureList, int black_Ash, int acacia_Bark, int giant_Inhaler, int kapre_Cigar) {
        this.id = id;
        this.health = health;
        this.bounty = bounty;
        this.diffMultiplier = diffMultiplier;
        this.luck = luck;
        this.bountyBonus = bountyBonus;
        this.treasureList = treasureList;
        Black_Ash = black_Ash;
        Acacia_Bark = acacia_Bark;
        Giant_Inhaler = giant_Inhaler;
        Kapre_Cigar = kapre_Cigar;
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
    public String[] getTreasureList(){
        return treasureList;
    }
}
