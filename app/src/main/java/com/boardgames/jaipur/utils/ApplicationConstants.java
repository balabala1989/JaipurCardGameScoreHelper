package com.boardgames.jaipur.utils;

public class ApplicationConstants {

    /*
        All the request code between the activities or fragment/activity will start with 9xxx
        All the parameters passed between the activities or fragment/activity will start with 8xxx
     */
    //Database constants
    public static final int PLAYER_DATABASE_NUMBER_OF_EXECUTABLE_THREADS = 4;


    //PlayerManagementFragment constants
    public static final int PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS = 3;
    public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE = 9000;
    public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER = 8000;
    public static final int PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER = 8001;

    //PlayerActivity constants
    public static final String PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.PROFILE_URI";
    public static final String PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.PLAYER_NAME";
    public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_IMAGE = 9001;
    public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE = 8002;
    public static final int PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE = 8003;

    //ImagePickAndCrop activity constants
    public static final String PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_URI_PATH_REPLY = "com.boardgames.jaipur.ui.playersmanagement.imagepickandcrop.URI_PATH";
}
