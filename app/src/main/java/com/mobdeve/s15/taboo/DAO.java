package com.mobdeve.s15.taboo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updatePlayer(PlayerData playerData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateTreasury(Treasure treasure);

    @Query("SELECT * FROM player_data;")
    LiveData<PlayerData> getPlayerData();
    @Query("SELECT * FROM player_data;")
    PlayerData getCurrentPlayerData();

    @Query("SELECT * FROM treasury ORDER BY itemid ASC;")
    LiveData<List<Treasure>> getTreasury();
    @Query("SELECT * FROM treasury ORDER BY itemid ASC;")
    List<Treasure> getCurrentTreasury();

    @Query("DELETE FROM player_data;")
    void deletePlayer();

    @Query("DELETE FROM treasury;")
    void deleteTreasures();
}
