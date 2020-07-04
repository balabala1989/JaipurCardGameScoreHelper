package com.boardgames.jaipur.utils;

    public class ApplicationConstants {

        /*
            All the request code between the activities or fragment/activity will start with 9xxx
            All the parameters passed between the activities or fragment/activity will start with 8xxx
         */
        //Database constants
        public static final int NUMBER_OF_EXECUTABLE_THREADS_FOR_DATABASE = 4;


        //PlayerManagementFragment constants
        public static final int PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS = 3;
        public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE = 9000;
        public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER = 8000;
        public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER = 8001;
        public static final String PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.REQUEST_TYPE";
        public static final String PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.PLAYER_DETAILS";

        //PlayerActivity constants
        public static final String PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.PROFILE_URI";
        public static final String PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.PLAYER_NAME";
        public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_IMAGE = 9001;
        public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE = 9002;
        public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE = 9003;
        public static final String PLAYERACTIVITY_TITLE= "com.boardgames.jaipur.ui.playersmanagement.playeractivity.TITLE";
        public static final String PLAYERACTIVITY_REQUEST_TYPE_REPLY = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.REQUEST_TYPE_REPLY";
        public static final String PLAYERACTIVITY_DELETE_OPERATION_REQUESTED = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.DELETE_OPERATION_REQUESTED";
        public static final String PLAYERACTIVITY_UPDATE_OPERATION_PROFILE_IMAGE_CHANGED = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.UPDATE_OPERATION_PROFILE_IMAGE_CHANGED";


        //ImagePickAndCrop activity constants
        public static final String PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_URI_PATH_REPLY = "com.boardgames.jaipur.ui.utils.imagepickandcrop.URI_PATH";

        //NewGameFragement constants
        public static final String NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERONE_DETAILS = "com.boardgames.jaipur.ui.newgame.newgamefragment.PLAYER_ONE_DETAILS";
        public static final String NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERTWO_DETAILS = "com.boardgames.jaipur.ui.newgame.newgamefragment.PLAYER_TWO_DETAILS";
        public static final String NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERS_IN_A_GAME = "com.boardgames.jaipur.ui.newgame.newgamefragment.PLAYERS_IN_A_GAME";

        //StartingPlayerActivity constants
        public static final String STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME = "com.boardgames.jaipur.ui.newgame.startingplayeractivity.GAMEDETAILS";
        public static final int GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE = 9004;
        public static final String HYPEN = " - ";
        public static final String SEPARATOR_OF_VIEWS = "~";
        public static final String SEPARATOR_OF_DATA = "|";

        public static final String ROUNDS_CALC_DIAMOND_GOODS = "DIAMOND";
        public static final String ROUNDS_CALC_GOLD_GOODS = "GOLD";
        public static final String ROUNDS_CALC_SILVER_GOODS = "SILVER";
        public static final String ROUNDS_CALC_CLOTH_GOODS = "CLOTH";
        public static final String ROUNDS_CALC_SPICE_GOODS = "SPICE";
        public static final String ROUNDS_CALC_LEATHER_GOODS = "LEATHER";
        public static final String ROUNDS_CALC_3_CARD_TOKEN = "3_CARD_TOKEN";
        public static final String ROUNDS_CALC_4_CARD_TOKEN = "4_CARD_TOKEN";
        public static final String ROUNDS_CALC_5_CARD_TOKEN = "5_CARD_TOKEN";
        public static final String ROUNDS_CALC_CAMEL_TOKEN = "CAMEL_TOKEN";


        public static final String EXCEPTION_DUE_TO_UNAVAILABILITY_OF_INTENT = "EXCEPTION_DUE_TO_UNAVAILABILITY_OF_INTENT";
        public static final String GOODS_DATA_FROM_SUMMARY_TO_ITEM_DETAILS = "GOODS_DATA";
        public static final String GOODS_NAME_FROM_SUMMARY_TO_ITEM_DETAILS = "GOODS_NAME";
        public static final String SELECTED_PLAYER_FROM_SUMMARY_TO_ITEM_DETAILS = "SELECTED_PLAYER";
        public static final long DEFAULT_PLAYER_ID = -1;

        public static final int ROUND_SUMMARY_ITEM_DISPLAY_REQUEST_IMAGE = 9005;
        public static final String WIN_MESSAGE_PART_1 = "Won Round ";
        public static final String WIN_MESSAGE_PART_2_BY_BONUS = "in tie by more number of Bonus tokens.";
        public static final String WIN_MESSAGE_PART_2_BY_GOODS = "in tie by more number of Goods tokens.";
        public static final String SPACE = " ";
        public static final String GAME_OVER = " Won the game. Game Over!!!!";

        public static final String ROUND_CALC_SUMM_TO_GAME_SUMM_WIN_MESSAGE = "com.boardgames.jaipur.ui.newgame.newgamefragment.WIN_MESSAGE";

        public static final int GAME_SUMMARY_CAMERA_REQUEST_IMAGE = 9006;
        public static final String GAME_SUMMARY_TO_ROUND_SUMMARY_EDIT_MODE = "EDIT_MODE";
        public static final String GAME_SUMMARY_TO_ROUND_SUMMARY_NORMAL_MODE = "NORMAL_MODE";
        public static final String GAME_SUMM_TO_ROUND_SUMM_MODE = "com.boardgames.jaipur.ui.newgame.newgamefragment.MODE";
        public static final String GAME_SUMM_TO_ROUND_SUMM_ROUND_EDIT = "com.boardgames.jaipur.ui.newgame.newgamefragment.ROUND_IN_EDIT_MODE";
}
