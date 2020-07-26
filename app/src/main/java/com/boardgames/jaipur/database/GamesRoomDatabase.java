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

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Game.class, Round.class}, version = 3, exportSchema = false)
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
                /*long playerOneId = 178;
                long playerTwoId = 179;
                Random rand = new Random();
                for (int i = 0; i < 51; i++) {
                    Game game = new Game();
                    if (i % 2 == 0) {
                        game.setPlayerOneID(playerOneId);
                        game.setPlayerTwoID(playerTwoId);
                    }
                    else {
                        game.setPlayerOneID(playerTwoId);
                        game.setPlayerTwoID(playerOneId);
                    }
                    if (new Random().nextBoolean()) {
                        game.setWinner(playerOneId);
                        game.setRoundsCompleted(3);
                    }
                    else {
                        game.setWinner(playerTwoId);
                        game.setRoundsCompleted(2);
                    }
                    game.setGamePlayStatus("C");
                    game.setTimeCreated(System.currentTimeMillis());
                    gameDao.insertGame(game);
                }*/
            });
        }
    };

    public static GamesRoomDatabase getDabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesRoomDatabase.class, "games_database")
                            .addCallback(gamesDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                    /*INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesRoomDatabase.class, "games_database")
                            .fallbackToDestructiveMigration()
                            .build();*/
                }
            }
        }

        return INSTANCE;
    }

}
