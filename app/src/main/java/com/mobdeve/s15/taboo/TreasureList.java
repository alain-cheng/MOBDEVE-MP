package com.mobdeve.s15.taboo;

public class TreasureList {
    //Register new Treasures here
    private static final String[] treasureList = {"Black_Ash", "Acacia_Bark", "Giant_Inhaler", "Kapre_Cigar"};

    public static String[] getTreasureList(){
        return treasureList;
    }
    public static int numTreasures(){ return treasureList.length; }
}
