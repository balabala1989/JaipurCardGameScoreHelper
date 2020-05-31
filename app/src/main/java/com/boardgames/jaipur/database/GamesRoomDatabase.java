package com.boardgames.jaipur.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.boardgames.jaipur.dao.GameDao;
import com.boardgames.jaipur.dao.RoundDao;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.utils.ApplicationConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Game.class, Round.class}, version = 1, exportSchema = false)
public abstract class GamesRoomDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
    public abstract RoundDao roundDao();

    private static volatile GamesRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(ApplicationConstants.NUMBER_OF_EXECUTABLE_THREADS_FOR_DATABASE);

    private static RoomDatabase.Callback gamesDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriterExecutor.execute(() -> {
                GameDao gameDao = INSTANCE.gameDao();
                RoundDao roundDao = INSTANCE.roundDao();
                gameDao.deleteAllGames();
                roundDao.deleteAllRounds();
            });
        }
    };

    public static GamesRoomDatabase getDabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamesRoomDatabase.class) {
                if (INSTANCE == null) {
                    //TODO Need to delete the call back before publishing the app
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesRoomDatabase.class, "games_database").addCallback(gamesDatabaseCallback).build();
                    /*INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesRoomDatabase.class, "games_database").build(); */
                }
            }
        }

        return INSTANCE;
    }

}
