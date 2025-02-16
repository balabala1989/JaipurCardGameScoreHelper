package com.boardgames.jaipur.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.boardgames.jaipur.dao.PlayerDao;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.ApplicationConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class PlayerRoomDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    private static volatile PlayerRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(ApplicationConstants.NUMBER_OF_EXECUTABLE_THREADS_FOR_DATABASE);
    private static RoomDatabase.Callback playerRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriterExecutor.execute(() -> {
                PlayerDao playerDao = INSTANCE.playerDao();
                playerDao.deleteAllPlayers();

                for (int i = 0; i < 6; i++) {
                    Player player = new Player();
                    player.setPlayerName("Test player " + (i+1));
                    player.setPlayerAvatar("");
                    player.setTimeCreated(System.currentTimeMillis());
                    player.setTimeUpdated(System.currentTimeMillis());
                    playerDao.insertPlayer(player);
                    player = null;
                }
            });
        }
    };


    public static PlayerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    //TODO Need to delete the call back before publishing the app
                    /*INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database").addCallback(playerRoomDatabaseCallback).build();*/
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database").build();
                }
            }

        }

        return INSTANCE;
    }


}
