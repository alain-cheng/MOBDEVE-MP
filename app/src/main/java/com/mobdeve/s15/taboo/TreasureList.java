package com.mobdeve.s15.taboo;

public class TreasureList {
    //Register new Treasures here
    private static final String[] treasureList = {"Black Ash", "Acacia Bark", "Giant Inhaler", "Kapre Cigar",
    "Jamuel's Haymaker", "Alwyn's Grand Order", "Alain's Astragal"};

    private static final int[] treasureImages = {R.drawable.item_kaprecigar};

    public static String[] getTreasureList(){
        return treasureList;
    }

    public static int[] getTreasureImages(){
        return treasureImages;
    }
    public static int numTreasures(){ return treasureList.length; }
}
