package com.mobdeve.s15.taboo;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface DAO {
    @Upsert
    void updatePlayer(PlayerData playerData);

    @Upsert
    void updateTreasury(Treasure treasure);

    @Query("SELECT * FROM player_data WHERE id = 0 LIMIT 1;")
    PlayerData getPlayerData();

    @Query("SELECT * FROM treasury;")
    List<Treasure> getTreasury();

    @Query("DELETE FROM player_data;")
    void deletePlayer();

    @Query("DELETE FROM treasury;")
    void deleteTreasures();
}
