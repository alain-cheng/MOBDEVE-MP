package com.mobdeve.s15.taboo;

import java.util.ArrayList;
import java.util.Random;

public class TreasureList {
    //Register new Treasures here. Note: Keep the Creators set at the end so that they are rarer than others
    //This is due to how the full taboo gauge unique item drop works.

    // CONSTANTS FOR TABOO GAUGE LEVEL
    public static final int WIN_ADD = 3, LOSS_ADD = -1, PHASE_1 = 6, PHASE_2 = 24, PHASE_3 = 36, PHASE_4 = 51;

    public static final String EMPTY_SET_BONUS = "0000000000";

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
                    for(int j = 0; j < TreasureList.fullTreasury.length; j++){
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
                for(int i = 0; i < TreasureList.fullTreasury.length; i++){
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

    public static String[] ids = new String[50];
    public static String[] names = new String[50];

    public static String[] bonuses = new String[50];
    public static String[] lores = new String[50];

    public static String[] rarities = new String[50];

    public static int[] images = new int[50];

    //TODO: ADD New treasures in this array. initData will automatically load them into the other arrays
    public static final Treasure[] fullTreasury = {
            new Treasure(
                    "item01",
                    "Black Ash",
                    R.drawable.item01,
                    "+1 Life",
                    "“Isn’t all ash colored black?”" + System.lineSeparator() + System.lineSeparator() + "“I think ash is usually gray in color, so I guess this is kinda special.”",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item02",
                    "Ancient Bark",
                    R.drawable.item02,
                    "+1 Life",
                    "Bark from an ancient Acacia tree that has stood for 30 years. Often home to a 9-foot-tall smoking addict.",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item03",
                    "Kapre’s Inhaler",
                    R.drawable.item03,
                    "+1 Life",
                    "“Did you know Tobacco smoke is a common trigger for asthma? Didn’t stop this fella though, and I’m not going to be the one to tell him.”",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item04",
                    "Kapre’s Cigar",
                    R.drawable.item04,
                    "+1 Life",
                    "While the Kapre Cigar provides magical benefits, be cautious when using it in the presence of mischievous forest spirits. The Kapre's favor is a gift, but it demands respect.",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item05",
                    "Magellan’s Cheese",
                    R.drawable.item05,
                    "Shorter dungeon lengths",
                    "Funky cheese that was once aboard Magellan’s ship. The first dairy product to sail the Seven Seas. It is emanating some sort of pitiful aura, as if it's saying it wants to be eaten. Poor thing has been sitting in the dark for god knows how long.",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item06",
                    "Conquistador Compass",
                    R.drawable.item06,
                    "Shorter dungeon lengths",
                    "A special compass of a centuries-old design. It is specially made to navigate the Seven Seas and to find the tastiest spices ever.",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item07",
                    "Chief’s Sampilan",
                    R.drawable.item07,
                    "Shorter dungeon lengths",
                    "A fierce Datu used to wield this sword. Rumor has it he was not human at all as he stood at 7ft tall and has lived to defend Mactan for centuries. Survivors of the Victoria ship described a half-human half-horse being leading the warriors which made them retreat.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item08",
                    "Lost Spices",
                    R.drawable.item08,
                    "Shorter dungeon lengths",
                    "The ultimate reward for navigating the world. Unfortunately, these spices remain unfound.",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item09",
                    "Elder Wand",
                    R.drawable.item09,
                    "+100 bounty upon set completion",
                    "“I don’t know man, this just seems like a stick to me and it ain’t even straight.”",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item10",
                    "Excalibur",
                    R.drawable.item10,
                    "+100 bounty upon set completion",
                    "The power of Excalibur's Legacy demands a heart untainted by greed and a spirit steadfast in justice. Wield with honor, for the sword knows the true character of its master.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item11",
                    "Mjolnir",
                    R.drawable.item11,
                    "+100 bounty upon set completion",
                    "This war hammer carries the legacy of the God of Thunder, summoning lightning to smite foes and bring justice to the realms. There’s nothing it can’t do except pull nails out from wood",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item12",
                    "Iron Slippers",
                    R.drawable.item12,
                    "+100 bounty upon set completion",
                    "“Years later, I can still feel the pain when mom hit me with one of these.”",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item13",
                    "Mom’s Spaghetti",
                    R.drawable.item13,
                    "+1 Life",
                    "“I vomited this on my sweater one time, gross.”",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item14",
                    "Billy’s Jeans",
                    R.drawable.item14,
                    "+1 Life",
                    "“It dances on its own when everyone’s asleep at 3 am.”",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item15",
                    "Dragon Imagination",
                    R.drawable.item15,
                    "+1 Life",
                    "Imagination so powerful you’ll feel on top of the world.",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item16",
                    "Bruno’s Grenade",
                    R.drawable.item16,
                    "+1 Life",
                    "Thrown all the way from planet Mars. Absolute banger.",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item17",
                    "Stalker Fang (Aswang)",
                    R.drawable.item17,
                    "Gain more taboo from completed runs",
                    "The fang of the famous Manananggal, everybody has one these days.",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item18",
                    "Mananggal Wing",
                    R.drawable.item18,
                    "Gain more taboo from completed runs",
                    "Even when severed, this thing still flaps on its own as if trying to get back to its owner.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item19",
                    "Broken Toy Rattle (Tyanak)",
                    R.drawable.item19,
                    "Gain more taboo from completed runs",
                    "Not only broken but also cursed. The Tyanak does not like other people playing with its toys. They can’t stand the germs.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item20",
                    "Bakunawa Egg",
                    R.drawable.item20,
                    "Gain more taboo from completed runs",
                    "Keep it long enough in temperatures over 1000°C and you might hatch yourself a moon eater",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item21",
                    "Duwende Hat",
                    R.drawable.item21,
                    "+50 bounty upon set completion",
                    "A humble hat, not much to say about that, probably owned by a little rat.",
                    RARITY[0],
                    1
            ),
            new Treasure(
                    "item22",
                    "Siyokoy Fin",
                    R.drawable.item22,
                    "+50 bounty upon set completion",
                    "These could have been from a fish for all I know.",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item23",
                    "Tikbalang Mane",
                    R.drawable.item23,
                    "+50 bounty upon set completion",
                    "A glorious and majestic silver mane from the Tikbalang. Smooth and silky on every strand.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item24",
                    "Bathala’s Tears",
                    R.drawable.item24,
                    "+50 bounty upon set completion",
                    "Tears of the transcendent supreme god",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item25",
                    "Diaryong Tagalog",
                    R.drawable.item25,
                    "+50 bounty upon set completion",
                    "The latest issue was years ago.",
                    RARITY[0],
                    1
            ),
            //This is a sample treasure, delete before launch
            new Treasure(
                    "item26",
                    "Torn Cedula",
                    R.drawable.item26,
                    "+50 bounty upon set completion",
                    "The spark of the Philippine revolution",
                    RARITY[1],
                    1
            ),
            new Treasure(
                    "item27",
                    "Shades of Simoun",
                    R.drawable.item27,
                    "+50 bounty upon set completion",
                    "Black, circular shades. A perfect disguise.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item28",
                    "Spoliarium",
                    R.drawable.item28,
                    "+50 bounty upon set completion",
                    "A painter spent 8 months to complete this painting. It depicts dying gladiators in the basement of the Roman Colosseum.",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item29",
                    "Queen Universe Crown",
                    R.drawable.item29,
                    "+1 Life",
                    "“A future forged by women who push the limits of what's possible”. As well as a tribute to the four Ms. Universe winners from the Philippines.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item30",
                    "Magician’s Cue Ball",
                    R.drawable.item30,
                    "+1 Life",
                    "This cue ball is embedded with spells by the Pool Magician, allowing him to make impossible shots. He is arguably the greatest billiards player of all ",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item31",
                    "Pacman Boxers",
                    R.drawable.item31,
                    "+1 Life",
                    "Comfortable and nostalgic, a perfect fit for Sundays. A tribute to the one and only 8-Division Boxing Champion.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item32",
                    "127kg Gold Medal",
                    R.drawable.item32,
                    "+1 Life",
                    "The first-ever Olympic gold medal for the Philippines, won by Hidilyn Diaz. The medal weighs 127 kg. Only the strongest are given this award.",
                    RARITY[2],
                    1
            ),
            new Treasure(
                    "item45",
                    "Jamuel’s Haymaker",
                    R.drawable.creators1,
                    "x2 total bounty upon set completion",
                    "“Crafted from the hides of legendary beasts, these gloves amplify the force behind every punch, turning a mere brawl into a spectacle of might. They carry the echoes of countless victories and the resilience of those who refuse to yield. Just kidding, I like boxing, and my favorite color is purple.“ - Jamuel, 2023",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item46",
                    "Alwyn’s Grand Order",
                    R.drawable.creators2,
                    "x2 total bounty upon set completion",
                    "A meal consisting of a quarter-pounder burger with a side of fries. Seems to contain an absolutely insane amount of magical energy. You will probably die trying to eat this...",
                    RARITY[3],
                    1
            ),
            new Treasure(
                    "item47",
                    "Alain’s Astragal",
                    R.drawable.creators3,
                    "x2 total bounty upon set completion",
                    "“A manifestation of one's crippling gambling addiction. This piece of ancient tech is the gateway to RNGesus' palace. Being in possession of this astragal is a sign one needs to touch grass or scratch a lottery card immediately. Anyways, I'm not liable for whatever happens afterwards.”",
                    RARITY[3],
                    1
            ),
    };

    public static Treasure[] lostTreasures = {
            new Treasure(
                    "lost01",
                    "One Piece",
                    R.drawable.lost01,
                    "",
                    "You finally found it after years of searching, and the crowd goes... mild.",
                    RARITY[4],
                    1
            ),
    };

    public static void initData(){
        //Initialize fullTreasury by for loop during main onCreate here
        for(int i = 0; i < fullTreasury.length; i++){
            ids[i] = fullTreasury[i].getId();
            names[i] = fullTreasury[i].getName();
            bonuses[i] = fullTreasury[i].getItemBonus();
            lores[i] = fullTreasury[i].getLore();
            rarities[i] = fullTreasury[i].getRarity();
            images[i] = fullTreasury[i].getImageid();
        }
    }
}
