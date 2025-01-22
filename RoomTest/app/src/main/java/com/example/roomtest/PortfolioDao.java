package com.example.roomtest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPortfolio(Portfolio portfolio);

    @Query("SELECT * FROM portfolio")
    List<Portfolio> getAllPortfolioItems();

    @Update
    void updatePortfolio(Portfolio portfolio);

    @Delete
    void deletePortfolio(Portfolio portfolio); // Delete method
}
