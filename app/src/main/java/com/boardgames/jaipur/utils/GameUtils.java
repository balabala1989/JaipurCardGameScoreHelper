package com.boardgames.jaipur.utils;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Round;
import java.util.HashMap;


public class GameUtils {

    public static final HashMap<String,String> goodsNameToGoodsItems;
    public static final HashMap<String, HashMap<String, Integer>> goodsToItemsToScore;
    public static final HashMap<String, HashMap<String, Integer>> goodsToItemsToImage;
    private static final String DIAMOND_ITEMS = "1_7~2_5_1~3_5_2~4_5_3";
    private static final String GOLD_ITEMS = "1_6~2_5_1~3_5_2~4_5_3";
    private static final String SILVER_ITEMS = "1_5~2_5_1~3_5_2~4_5_3";

    static  {

        goodsNameToGoodsItems = new HashMap<>();
        goodsToItemsToScore = new HashMap<>();
        goodsToItemsToImage = new HashMap<>();

        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS,DIAMOND_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_GOLD_GOODS,GOLD_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_SILVER_GOODS,SILVER_ITEMS);

        HashMap<String, Integer> itemsToScore = new HashMap<>();
        HashMap<String, Integer> itemsToImage = new HashMap<>();
        //Diamonds

        itemsToScore.put("1_7",14);
        itemsToScore.put("2_5_1",5);
        itemsToScore.put("3_5_2", 5);
        itemsToScore.put("4_5_3", 5);
        itemsToImage.put("1_7",R.drawable.a1_1_diamonds_77_imagview);
        itemsToImage.put("2_5_1",R.drawable.a1_2_diamonds_5_imageview);
        itemsToImage.put("3_5_2",R.drawable.a1_2_diamonds_5_imageview);
        itemsToImage.put("4_5_3",R.drawable.a1_2_diamonds_5_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS, itemsToImage);

        //Gold
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_6",12);
        itemsToScore.put("2_5_1",5);
        itemsToScore.put("3_5_2", 5);
        itemsToScore.put("4_5_3", 5);
        itemsToImage.put("1_6",R.drawable.a2_1_gold_66_imageview);
        itemsToImage.put("2_5_1",R.drawable.a2_2_gold_5_imageview);
        itemsToImage.put("3_5_2",R.drawable.a2_2_gold_5_imageview);
        itemsToImage.put("4_5_3",R.drawable.a2_2_gold_5_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_GOLD_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_GOLD_GOODS, itemsToImage);

        //Silver
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_5",10);
        itemsToScore.put("2_5_1",5);
        itemsToScore.put("3_5_2", 5);
        itemsToScore.put("4_5_3", 5);
        itemsToImage.put("1_5",R.drawable.a3_1_silver_55_imageview);
        itemsToImage.put("2_5_1",R.drawable.a3_2_silver_5_imageview);
        itemsToImage.put("3_5_2",R.drawable.a3_2_silver_5_imageview);
        itemsToImage.put("4_5_3",R.drawable.a3_2_silver_5_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_SILVER_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_SILVER_GOODS, itemsToImage);
    }



    public static void setScore(Round round, int score, String goodsInDisplay) {
        switch (goodsInDisplay) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
                round.setDiamondScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                round.setGoldScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                round.setSilverScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                round.setClothScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                round.setSpiceScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                round.setLeatherScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                round.setThreeCardTokenScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                round.setFourCardTokenScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                round.setFiveCardTokenScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN:
                round.setCamelReceived('Y');
                break;
        }
    }
}
