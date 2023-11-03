package com.mobdeve.s15.taboo;

public class TreasureList {
    //Register new Treasures here. Note: Keep the Creators set at the end so that they are rarer than others
    //This is due to how the full taboo gauge unique item drop works.

    public static final String[] RARITY = {"COMMON", "RARE", "FORBIDDEN", "BLASPHEMY"};

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
}
