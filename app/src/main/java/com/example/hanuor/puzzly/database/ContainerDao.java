package com.example.hanuor.puzzly.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContainerDao {
    @Query("SELECT * FROM puzzly_data")
    List<Container> getAll();

    @Query("SELECT * FROM puzzly_data where page_number LIKE  :pageNumber")
    Container findByPageNumber(int pageNumber);

    @Query("SELECT COUNT(*) from puzzly_data")
    int countPages();

    @Insert
    void insertAll(Container... data);

    @Delete
    void delete(Container data);
}
