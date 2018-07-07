package com.hanuor.puzzly.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "puzzly_data")
public class Container {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "page_number")
    private int page_number;

    @ColumnInfo(name = "json_data")
    private int json_data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getJson_data() {
        return json_data;
    }

    public void setJson_data(int json_data) {
        this.json_data = json_data;
    }
}
