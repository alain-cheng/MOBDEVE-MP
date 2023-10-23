package com.mobdeve.s15.taboo;

public class TreasureList {
    //Register new Treasures here. Use this as a data source temporally while better solution is being made.
    //Maybe make a copy of treasury call treasure_list? Enter items in order.

    private static final String[] RARITY = {"COMMON", "RARE", "FORBIDDEN", "BLASPHEMY"};
    public static final String[] names = {"Black Ash", "Acacia Bark", "Giant Inhaler", "Kapre Cigar",
    "Jamuel's Haymaker", "Alwyn's Grand Order", "Alain's Astragal", "Greedy Reign", "Last Farewell", "Untouchable"};

    public static final String[] bonuses = {"Not Implemented"};
    public static final String[] lores = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            "Not Implemented",
            "Not Implemented",
            "Not Implemented",
            "Not Implemented",
            "A meal consisting of a quarter-pounder burger with a side of fries and some soft drinks. Seems to contain an insane amount of magical energy. You will probably die trying to eat this...",
            "Not Implemented",
            "Not Implemented",
            "Not Implemented",
            "Not Implemented",
    };

    public static final String[] rarities = {RARITY[0], RARITY[0], RARITY[1], RARITY[3],
            RARITY[3], RARITY[3], RARITY[3], RARITY[2], RARITY[2], RARITY[3]};

    public static final int[] images = {R.drawable.item_kaprecigar};
}
