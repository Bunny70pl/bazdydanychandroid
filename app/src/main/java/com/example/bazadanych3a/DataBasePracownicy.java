package com.example.bazadanych3a;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Pracownik.class, version = 2, exportSchema = false)
public abstract class DataBasePracownicy extends RoomDatabase {
    public abstract DaoPracownicy getDaoPracownicy();
}
