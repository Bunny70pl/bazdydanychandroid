package com.example.bazadanych3a;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.Async;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private DataBasePracownicy dataBasePracownicy;
    private EditText imieEdit;
    private EditText nazwiskoEdit;
    private Spinner spinner;
    private Button dodajDoBazy;
    private List<Pracownik> pracownicy;
    private ListView listView;
    private ArrayAdapter<Pracownik> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imieEdit = findViewById(R.id.editTextText);
        nazwiskoEdit = findViewById(R.id.editTextText2);
        spinner = findViewById(R.id.spinner);
        dodajDoBazy = findViewById(R.id.button);
        listView = findViewById(R.id.listViewPracownicy);
        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        dataBasePracownicy = Room.databaseBuilder(
                getApplicationContext(),
                DataBasePracownicy.class,
                "pracownicyDB"
                ).addCallback(myCallBack).allowMainThreadQueries().build();
        dodajDoBazy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String imie = imieEdit.getText().toString();
                        String nazwisko = nazwiskoEdit.getText().toString();
                        String stanowsiko = spinner.getSelectedItem().toString();
                        Pracownik pracownik = new Pracownik(imie,nazwisko,"Polski","Angielski",4600.0,stanowsiko);
                        dodajDaneDoBazyWTle(pracownik);
                    }
                }
        );
        wypiszPracownikow();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        usunPracownika(pracownicy.get(i));
                    }
                }
        );
    }
    private void wypiszPracownikow(){
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService2.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        pracownicy = dataBasePracownicy.getDaoPracownicy().wypiszWszystkichPracownikow();
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,pracownicy);
                                        listView.setAdapter(arrayAdapter);
                                    }
                                }
                        );
                    }
                }
        );
    }
    private void usunPracownika(Pracownik pracownik){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        dataBasePracownicy.getDaoPracownicy().usunPracownika(pracownik);
                        pracownicy.remove(pracownik);
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );
    }
    private void dodajDaneDoBazyWTle(Pracownik pracownik){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        dataBasePracownicy.getDaoPracownicy().dodajPracownika(pracownik);
                        /*dataBasePracownicy.getDaoPracownicy().dodajPracownika(new Pracownik("Jas","Nowak","Polski","Angielski",12300.99,"Programista"));*/
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Dodano do bazy", Toast.LENGTH_SHORT).show();
                                        pracownicy.add(pracownik);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );
    }
}



//1 klasa na rekordy pracownicy java
//2 do klasy adnotacje bazodanowe pracownicy.java
//3 Dao DaoPracownicy
//4 Klasa z baza DataBasePracownicy
//