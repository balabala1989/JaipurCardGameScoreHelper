package com.boardgames.jaipur.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.boardgames.jaipur.dao.PlayerDao;
import com.boardgames.jaipur.entities.Player;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class PlayerRoomDatabase extends RoomDatabase {

    public PlayerDao playerDao;

    private static volatile PlayerRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static RoomDatabase.Callback playerRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriterExecutor.execute(() -> {
                PlayerDao playerDao = INSTANCE.playerDao;
                playerDao.deleteAllPlayers();

                Player player = new Player();
                player.setPlayerName("Test player 1");
                player.setPlayerAvatar("Test Location 2");
                player.setTimeCreated(System.currentTimeMillis()/100);
                player.setTimeUpdated(System.currentTimeMillis()/100);
                playerDao.insertPlayer(player);

                player = null;
                player.setPlayerName("Test player 2");
                player.setPlayerAvatar("Test Location 2");
                player.setTimeCreated(System.currentTimeMillis()/100);
                player.setTimeUpdated(System.currentTimeMillis()/100);
                playerDao.insertPlayer(player);
            });
        }
    };


    public static PlayerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database").addCallback(playerRoomDatabaseCallback).build();
                }
            }

        }

        return INSTANCE;
    }


}
