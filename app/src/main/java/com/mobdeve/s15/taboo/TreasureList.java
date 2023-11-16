package com.mobdeve.s15.taboo;

import java.util.ArrayList;
import java.util.Random;

public class TreasureList {
    //Register new Treasures here. Note: Keep the Creators set at the end so that they are rarer than others
    //This is due to how the full taboo gauge unique item drop works.

    // CONSTANTS FOR TABOO GAUGE LEVEL
    public static final int WIN_ADD = 3, LOSS_ADD = -1, PHASE_1 = 6, PHASE_2 = 24, PHASE_3 = 36, PHASE_4 = 51;

    public static Treasure lastRandom = new Treasure("0ERROR", "ERROR, BUG!", R.drawable.item_kaprecigar,
            "000000",
            "THIS IS NOT SUPPOSE TO BE HERE!", "LOST", 1);

    public static void genRandomTreasure(String mode, String rarity, PlayerData playerData){
        switch(mode){
            case "SELL":{
                if(!rarity.equals("LOST")){
                    Random rand = new Random(System.nanoTime());
                    //Get indexes of treasures with rarities[i] == rarity
                    ArrayList<Integer> indexes = new ArrayList<>();
                    for(int j = 0; j < TreasureList.rarities.length; j++){
                        if(TreasureList.rarities[j].equals(rarity))
                            indexes.add(j);
                    }
                    //Select between the indexes at random
                    int index = rand.nextInt(indexes.size());
                    //Generate Treasure and add
                    lastRandom = new Treasure(
                            TreasureList.ids[indexes.get(index)],
                            TreasureList.names[indexes.get(index)],
                            TreasureList.images[indexes.get(index)],
                            TreasureList.bonuses[indexes.get(index)],
                            TreasureList.lores[indexes.get(index)],
                            TreasureList.rarities[indexes.get(index)],
                            1
                    );
                }
                else{ //Generate lost items
                    Random rand = new Random(System.nanoTime());
                    lastRandom = lostTreasures[rand.nextInt(lostTreasures.length)];
                }
                break;
            }
            case "REWARD":{
                int totalLuck = playerData.getLuck();
                if(playerData.getTaboo() >= PHASE_4)
                    totalLuck += 10;
                Random rand = new Random(System.nanoTime());
                int rng = rand.nextInt(100) + totalLuck + 1; //The one is a correction factor

                //Select rarity based on luck
                if(rng <= 40)
                    rarity = TreasureList.RARITY[0];
                else if(rng <= 70)
                    rarity = TreasureList.RARITY[1];
                else if(rng <= 90)
                    rarity = TreasureList.RARITY[2];
                else if(rng <= 99)
                    rarity = TreasureList.RARITY[3];
                else if(rng > 99){ //Check for generating lost treasures
                    int lostRNG = rand.nextInt(100) + totalLuck + 1;
                    if(lostRNG > 66){
                        lastRandom = lostTreasures[rand.nextInt(lostTreasures.length)];
                        break;
                    }
                    else //Couldn't get Lost treasure
                        rarity = TreasureList.RARITY[3];
                }

                //Get indexes of treasures with rarities[i] == rarity
                ArrayList<Integer> indexes = new ArrayList<>();
                for(int i = 0; i < TreasureList.rarities.length; i++){
                    if(TreasureList.rarities[i].equals(rarity))
                        indexes.add(i);
                }
                //Select between the indexes at random
                int index = rand.nextInt(indexes.size());
                lastRandom = new Treasure(
                        TreasureList.ids[indexes.get(index)],
                        TreasureList.names[indexes.get(index)],
                        TreasureList.images[indexes.get(index)],
                        TreasureList.bonuses[indexes.get(index)],
                        TreasureList.lores[indexes.get(index)],
                        TreasureList.rarities[indexes.get(index)],
                        1
                );
                break;
            }
        }
    }

    public static final String[] RARITY = {"COMMON", "RARE", "FORBIDDEN", "BLASPHEMY", "LOST"};

    public static final String[] ids = {
            "item01",
            "item02",
            "item03",
            "item04",
            "item05",
            "item06",
            "item07",
            "item08",
            "item09",
            "item10",
    };
    public static final String[] names = {
            "Black Ash", //01
            "Acacia Bark", //02
            "Giant Inhaler", //03
            "Kapre Cigar", //04
            "Greedy Reign", //05
            "Last Farewell", //06
            "Untouchable", //07
            "Jamuel's Haymaker", //Creators 1
            "Alwyn's Grand Order", //Creators 2
            "Alain's Astragal", //Creators 3
    };

    public static final String[] bonuses = {
            "Not Implemented", //01
            "Not Implemented", //02
            "Not Implemented", //03
            "Not Implemented", //04
            "Not Implemented", //05
            "Not Implemented", //06
            "Not Implemented", //07
            "Not Implemented", //Creators 1
            "Not Implemented", //Creators 2
            "Not Implemented",  //Creators 3
    };
    public static final String[] lores = {
            //01
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                    " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                    " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
                    " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            "Not Implemented", //02
            "Not Implemented", //03
            "Not Implemented", //04
            "Not Implemented", //05
            "Not Implemented", //06
            "Not Implemented", //07
            "Not Implemented", //Creators 1
            //Creators 2
            "A meal consisting of a quarter-pounder burger with a side of fries and some soft drinks." +
                    " Seems to contain an insane amount of magical energy. You will probably die trying to eat this...",
            "Not Implemented", //Creators 3
    };

    public static final String[] rarities = {
            RARITY[0], //01
            RARITY[0], //02
            RARITY[1], //03
            RARITY[3], //04
            RARITY[2], //05
            RARITY[2], //06
            RARITY[3], //07
            RARITY[3], //Creators 1
            RARITY[3], //Creators 2
            RARITY[3], //Creators 3
    };

    public static final int[] images = {
            R.drawable.item_kaprecigar, //01
            R.drawable.item_kaprecigar, //02
            R.drawable.item_kaprecigar, //03
            R.drawable.item_kaprecigar, //04
            R.drawable.item_kaprecigar, //05
            R.drawable.item_kaprecigar, //06
            R.drawable.item_kaprecigar, //07
            R.drawable.item_kaprecigar, //Creators 1
            R.drawable.item_kaprecigar, //Creators 2
            R.drawable.item_kaprecigar, //Creators 3
    };

    public static Treasure[] lostTreasures = {
            new Treasure("lost01", "One Piece", R.drawable.item_kaprecigar, "", "", RARITY[4], 1),
    };
}
