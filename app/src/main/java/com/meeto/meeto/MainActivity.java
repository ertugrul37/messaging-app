package com.meeto.meeto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.ChildEventRegistration;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
   FirebaseDatabase firebaseDatabase;
   DatabaseReference reference;
    List<String> list;
    String Username;
    RecyclerView userRecyview;
    UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tandem();
        listele();


    }
    public void tandem()
    {
        Username=getIntent().getExtras().getString("kadi");
        firebaseDatabase= FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        list = new ArrayList<>();
        userRecyview = (RecyclerView) findViewById(R.id.userRecyview);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(MainActivity.this,2);
        userRecyview.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(MainActivity.this,list,MainActivity.this,Username);
        userRecyview.setAdapter(userAdapter);
    }

    public void listele()
    {
        reference.child("kullanıcılar").addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(!snapshot.getKey().equals(Username)){
                    list.add(snapshot.getKey());
                    Log.i("kullanıcı",snapshot.getKey());}
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

