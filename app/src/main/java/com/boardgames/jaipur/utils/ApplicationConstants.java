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
        public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE = 8002;
        public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE = 8003;
        public static final String PLAYERACTIVITY_TITLE= "com.boardgames.jaipur.ui.playersmanagement.playeractivity.TITLE";
        public static final String PLAYERACTIVITY_REQUEST_TYPE_REPLY = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.REQUEST_TYPE_REPLY";
        public static final String PLAYERACTIVITY_DELETE_OPERATION_REQUESTED = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.DELETE_OPERATION_REQUESTED";
        public static final String PLAYERACTIVITY_UPDATE_OPERATION_PROFILE_IMAGE_CHANGED = "com.boardgames.jaipur.ui.playersmanagement.playeractivity.UPDATE_OPERATION_PROFILE_IMAGE_CHANGED";


        //ImagePickAndCrop activity constants
        public static final String PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_URI_PATH_REPLY = "com.boardgames.jaipur.ui.utils.imagepickandcrop.URI_PATH";

        //NewGameFragement constants
        public  static final String NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERONE_DETAILS = "com.boardgames.jaipur.ui.newgame.newgamefragment.PLAYER_ONE_DETAILS";
        public  static final String NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERTWO_DETAILS = "com.boardgames.jaipur.ui.newgame.newgamefragment.PLAYER_TWO_DETAILS";
}
