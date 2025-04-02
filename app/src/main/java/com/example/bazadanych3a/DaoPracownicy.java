package com.example.bazadanych3a;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.DelicateCoroutinesApi;

@Dao
public interface DaoPracownicy {
    @Insert
    public void dodajPracownika(Pracownik pracownik);

    @Insert
    public void dodajWieluPracownikow(Pracownik ...pracownicy);

    @Delete
    public void usunPracownika(Pracownik pracownik);

    @Update
    public void zaktualizujDanePracownika(Pracownik pracownik);

    @Query("SELECT * FROM pracownicy WHERE jezykOjczysty = 'polski'")
    public List<Pracownik> wypiszPracownikowPolskoJezycznych();

    @Query("Select * from pracownicy where jezykObcyKomunikatywny = :jezyk")
    public List<Pracownik> wypiszPracownikowMowiacychJezykiem(String jezyk);
    @Query("SELECT * FROM pracownicy")
    public List<Pracownik> wypiszWszystkichPracownikow();
}
