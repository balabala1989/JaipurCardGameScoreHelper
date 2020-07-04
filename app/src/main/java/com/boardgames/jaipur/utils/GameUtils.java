package com.boardgames.jaipur.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Round;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.SimpleFormatter;


public class GameUtils {

    private static final String BONUS_CALCULATION_TYPE = "BONUS_CALCULATION";
    private static final String GOODS_CALCULATION_TYPE = "GOODS_CALCULATION";
    public static final HashMap<String,String> goodsNameToGoodsItems;
    public static final HashMap<String, HashMap<String, Integer>> goodsToItemsToScore;
    public static final HashMap<String, HashMap<String, Integer>> goodsToItemsToImage;
    private static final String DIAMOND_ITEMS = "1_7~2_5_1~3_5_2~4_5_3";
    private static final String GOLD_ITEMS = "1_6~2_5_1~3_5_2~4_5_3";
    private static final String SILVER_ITEMS = "1_5~2_5_1~3_5_2~4_5_3";
    private static final String CLOTH_ITEMS = "1_5~2_3_1~3_3_2~4_2_1~5_2_2~6_1_1~7_1_2";
    private static final String SPICE_ITEMS = "1_5~2_3_1~3_3_2~4_2_1~5_2_2~6_1_1~7_1_2";
    private static final String LEATHER_ITEMS = "1_4~2_3~3_2~4_1_1~5_1_2~6_1_3~7_1_4~8_1_5~9_1_6";
    private static final String THREE_TOKEN_ITEMS = "1_1_1~2_1_2~3_2_1~4_2_2~5_2_3~6_3_1~7_3_2";
    private static final String FOUR_TOKEN_ITEMS = "1_4_1~2_4_2~3_5_1~4_5_2~5_6_1~6_6_2";
    private static final String FIVE_TOKEN_ITEMS = "1_8_1~2_8_2~3_9_1~4_10_1~5_10_2";
    private static final String CAMEL_TOKEN_ITEMS = "1_5";
    public static final ArrayList<String> goodsList;

    static  {

        goodsNameToGoodsItems = new HashMap<>();
        goodsToItemsToScore = new HashMap<>();
        goodsToItemsToImage = new HashMap<>();

        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS,DIAMOND_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_GOLD_GOODS,GOLD_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_SILVER_GOODS,SILVER_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS,CLOTH_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_SPICE_GOODS,SPICE_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS,LEATHER_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN,THREE_TOKEN_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN,FOUR_TOKEN_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN,FIVE_TOKEN_ITEMS);
        goodsNameToGoodsItems.put(ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN,CAMEL_TOKEN_ITEMS);

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

        //Cloth
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_5", 5);
        itemsToScore.put("2_3_1", 3);
        itemsToScore.put("3_3_2", 3);
        itemsToScore.put("4_2_1", 2);
        itemsToScore.put("5_2_2", 2);
        itemsToScore.put("6_1_1", 1);
        itemsToScore.put("7_1_2", 1);
        itemsToImage.put("1_5", R.drawable.a4_1_cloth_5_imageview);
        itemsToImage.put("2_3_1", R.drawable.a4_2_cloth_3_imageview);
        itemsToImage.put("3_3_2", R.drawable.a4_2_cloth_3_imageview);
        itemsToImage.put("4_2_1", R.drawable.a4_3_cloth_2_imageview);
        itemsToImage.put("5_2_2", R.drawable.a4_3_cloth_2_imageview);
        itemsToImage.put("6_1_1", R.drawable.a4_4_cloth_1_imageview);
        itemsToImage.put("7_1_2", R.drawable.a4_4_cloth_1_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS, itemsToImage);

        //Spice
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_5", 5);
        itemsToScore.put("2_3_1", 3);
        itemsToScore.put("3_3_2", 3);
        itemsToScore.put("4_2_1", 2);
        itemsToScore.put("5_2_2", 2);
        itemsToScore.put("6_1_1", 1);
        itemsToScore.put("7_1_2", 1);
        itemsToImage.put("1_5", R.drawable.a5_1_spice_5_imageview);
        itemsToImage.put("2_3_1", R.drawable.a5_2_spice_3_imageview);
        itemsToImage.put("3_3_2", R.drawable.a5_2_spice_3_imageview);
        itemsToImage.put("4_2_1", R.drawable.a5_3_spice_2_imagview);
        itemsToImage.put("5_2_2", R.drawable.a5_3_spice_2_imagview);
        itemsToImage.put("6_1_1", R.drawable.a5_4_spice_1_imageview);
        itemsToImage.put("7_1_2", R.drawable.a5_4_spice_1_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_SPICE_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_SPICE_GOODS, itemsToImage);

        //Leather
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_4", 4);
        itemsToScore.put("2_3", 3);
        itemsToScore.put("3_2", 2);
        itemsToScore.put("4_1_1", 1);
        itemsToScore.put("5_1_2", 1);
        itemsToScore.put("6_1_3", 1);
        itemsToScore.put("7_1_4", 1);
        itemsToScore.put("8_1_5", 1);
        itemsToScore.put("9_1_6", 1);
        itemsToImage.put("1_4", R.drawable.a6_1_leather_4_imageview);
        itemsToImage.put("2_3", R.drawable.a6_2_leather_3_imageview);
        itemsToImage.put("3_2", R.drawable.a6_3_leather_2_imageview);
        itemsToImage.put("4_1_1", R.drawable.a6_4_leather_1_imageview);
        itemsToImage.put("5_1_2", R.drawable.a6_4_leather_1_imageview);
        itemsToImage.put("6_1_3", R.drawable.a6_4_leather_1_imageview);
        itemsToImage.put("7_1_4", R.drawable.a6_4_leather_1_imageview);
        itemsToImage.put("8_1_5", R.drawable.a6_4_leather_1_imageview);
        itemsToImage.put("9_1_6", R.drawable.a6_4_leather_1_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS, itemsToImage);

        //Three Token
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_1_1", 1);
        itemsToScore.put("2_1_2", 1);
        itemsToScore.put("3_2_1", 2);
        itemsToScore.put("4_2_2", 2);
        itemsToScore.put("5_2_3", 2);
        itemsToScore.put("6_3_1", 3);
        itemsToScore.put("7_3_2", 3);
        itemsToImage.put("1_1_1", R.drawable.a7_1_three_token_1_imageview);
        itemsToImage.put("2_1_2", R.drawable.a7_1_three_token_1_imageview);
        itemsToImage.put("3_2_1", R.drawable.a7_2_three_token_2_imageview);
        itemsToImage.put("4_2_2", R.drawable.a7_2_three_token_2_imageview);
        itemsToImage.put("5_2_3", R.drawable.a7_2_three_token_2_imageview);
        itemsToImage.put("6_3_1", R.drawable.a7_3_three_token_3_imageview);
        itemsToImage.put("7_3_2", R.drawable.a7_3_three_token_3_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN, itemsToImage);

        //Four Token
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_4_1", 4);
        itemsToScore.put("2_4_2", 4);
        itemsToScore.put("3_5_1", 5);
        itemsToScore.put("4_5_2", 5);
        itemsToScore.put("5_6_1", 6);
        itemsToScore.put("6_6_2", 6);
        itemsToImage.put("1_4_1", R.drawable.a8_1_four_token_4_imageview);
        itemsToImage.put("2_4_2", R.drawable.a8_1_four_token_4_imageview);
        itemsToImage.put("3_5_1", R.drawable.a8_2_four_token_5_imageview);
        itemsToImage.put("4_5_2", R.drawable.a8_2_four_token_5_imageview);
        itemsToImage.put("5_6_1", R.drawable.a8_3_four_token_6_imageview);
        itemsToImage.put("6_6_2", R.drawable.a8_3_four_token_6_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN, itemsToImage);

        //Five Token
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_8_1", 8);
        itemsToScore.put("2_8_2", 8);
        itemsToScore.put("3_9_1", 9);
        itemsToScore.put("4_10_1", 10);
        itemsToScore.put("5_10_2", 10);
        itemsToImage.put("1_8_1", R.drawable.a9_1_five_token_8_imageview);
        itemsToImage.put("2_8_2", R.drawable.a9_1_five_token_8_imageview);
        itemsToImage.put("3_9_1", R.drawable.a9_2_five_token_9_imageview);
        itemsToImage.put("4_10_1", R.drawable.a9_3_five_token_10_imageview);
        itemsToImage.put("5_10_2", R.drawable.a9_3_five_token_10_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN, itemsToImage);

        //Camel Token
        itemsToScore = new HashMap<>();
        itemsToImage = new HashMap<>();
        itemsToScore.put("1_5", 5);
        itemsToImage.put("1_5", R.drawable.a10_1_camel_token_5_imageview);
        goodsToItemsToScore.put(ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN,itemsToScore);
        goodsToItemsToImage.put(ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN, itemsToImage);

        goodsList = new ArrayList<>();
        goodsList.add(ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_GOLD_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_SILVER_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_SPICE_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN);
        goodsList.add(ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN);

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
                if (score != 0)
                    round.setCamelReceived('Y');
                break;
        }
    }

    public static long resolveTieToFindWinner(GameDetails gameDetails, GoodsDetailsForARound goodsDetailsForARound) {
        goodsDetailsForARound.setPlayerOneBonusTokens(0);
        goodsDetailsForARound.setPlayerTwoBonusTokens(0);
        goodsDetailsForARound.setPlayerOneGoodsTokens(0);
        goodsDetailsForARound.setPlayerTwoGoodsTokens(0);

        long playerOneID = gameDetails.getPlayersInAGame().getPlayerOne().getId();

        //Check for bonus tokens
        if (goodsDetailsForARound.getThreeTokenDetail() != null && !goodsDetailsForARound.getThreeTokenDetail().trim().isEmpty()) {
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getThreeTokenDetail(), BONUS_CALCULATION_TYPE, false);
        }

        if (goodsDetailsForARound.getFourTokenDetail() != null && !goodsDetailsForARound.getFourTokenDetail().trim().isEmpty()) {
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getFourTokenDetail(), BONUS_CALCULATION_TYPE, false);
        }

        if (goodsDetailsForARound.getFiveTokenDetail() != null && !goodsDetailsForARound.getFiveTokenDetail().trim().isEmpty()) {
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getFiveTokenDetail(), BONUS_CALCULATION_TYPE, false);
        }

        if (goodsDetailsForARound.getPlayerOneBonusTokens() > goodsDetailsForARound.getPlayerTwoBonusTokens())
            return gameDetails.getPlayersInAGame().getPlayerOne().getId();
        else if (goodsDetailsForARound.getPlayerOneBonusTokens() < goodsDetailsForARound.getPlayerTwoBonusTokens())
            return gameDetails.getPlayersInAGame().getPlayerTwo().getId();

        //Check for goods token only when bonus tokens count of the players match

        if (goodsDetailsForARound.getDiamondGoodsDetail() != null && !goodsDetailsForARound.getDiamondGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getDiamondGoodsDetail(), GOODS_CALCULATION_TYPE, true);
        if (goodsDetailsForARound.getGoldGoodsDetail() != null && !goodsDetailsForARound.getGoldGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getGoldGoodsDetail(), GOODS_CALCULATION_TYPE, true);
        if (goodsDetailsForARound.getSilverGoodsDetail() != null && !goodsDetailsForARound.getSilverGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getSilverGoodsDetail(), GOODS_CALCULATION_TYPE, true);
        if (goodsDetailsForARound.getClothGoodsDetail() != null && !goodsDetailsForARound.getClothGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getClothGoodsDetail(), GOODS_CALCULATION_TYPE, false);
        if (goodsDetailsForARound.getSpiceGoodsDetail() != null && !goodsDetailsForARound.getSpiceGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getSpiceGoodsDetail(), GOODS_CALCULATION_TYPE, false);
        if (goodsDetailsForARound.getLeatherGoodsDetail() != null && !goodsDetailsForARound.getLeatherGoodsDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getLeatherGoodsDetail(), GOODS_CALCULATION_TYPE, false);
        if (goodsDetailsForARound.getCamelTokenDetail() != null && !goodsDetailsForARound.getCamelTokenDetail().trim().isEmpty())
            computeTokenCount(playerOneID, goodsDetailsForARound, goodsDetailsForARound.getCamelTokenDetail(), GOODS_CALCULATION_TYPE, false);


        if (goodsDetailsForARound.getPlayerOneGoodsTokens() > goodsDetailsForARound.getPlayerTwoGoodsTokens())
            return gameDetails.getPlayersInAGame().getPlayerOne().getId();
        else
            return gameDetails.getPlayersInAGame().getPlayerTwo().getId();
    }

    private static void computeTokenCount(long playerOneID, GoodsDetailsForARound goodsDetailsForARound, String tokenDetails, String calculationType, boolean shouldCountTwo) {
        StringTokenizer tokenizer = new StringTokenizer(tokenDetails, ApplicationConstants.SEPARATOR_OF_DATA);
        int incrementValue = 1;
        while (tokenizer.hasMoreTokens()) {
            String[] splitData = tokenizer.nextToken().trim().split(ApplicationConstants.SEPARATOR_OF_VIEWS);

            if (calculationType.equalsIgnoreCase(BONUS_CALCULATION_TYPE)) {
                if (Long.parseLong(splitData[1]) == playerOneID)
                    goodsDetailsForARound.setPlayerOneBonusTokens(goodsDetailsForARound.getPlayerOneBonusTokens() + incrementValue);
                else
                    goodsDetailsForARound.setPlayerTwoBonusTokens(goodsDetailsForARound.getPlayerTwoBonusTokens() + incrementValue);
            }
            else {

                if (shouldCountTwo && (splitData[0].equalsIgnoreCase("1_7") || splitData[0].equalsIgnoreCase("1_6") || splitData[0].equalsIgnoreCase("1_5"))) {
                    incrementValue = 2;
                }
                if (Long.parseLong(splitData[1]) == playerOneID)
                    goodsDetailsForARound.setPlayerOneGoodsTokens(goodsDetailsForARound.getPlayerOneGoodsTokens() + incrementValue);
                else
                    goodsDetailsForARound.setPlayerTwoGoodsTokens(goodsDetailsForARound.getPlayerTwoGoodsTokens() + incrementValue);
            }
        }
    }

    public static File getAbsolutePathForImage(Context context, Bitmap bitmap) {
        File storageDir = new File (context.getExternalFilesDir(null), context.getString(R.string.gameactivity_external_path));
        if (!storageDir.exists()) {storageDir.mkdir();}
        File imageFile = new File(storageDir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByteArray = outputStream.toByteArray();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(imageByteArray);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
