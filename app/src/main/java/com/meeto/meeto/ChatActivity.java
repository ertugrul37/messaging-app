package com.meeto.meeto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String Username,othername;
    TextView chatusername;
    ImageView geri,gonder;
    EditText chatEdittext;
    RecyclerView chatRecyView;
    MesajAdapter mesajAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<MesajModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanımla();
        loadMesaj();


    }



    public void tanımla()
    {
          list = new ArrayList<>();
          Username = getIntent().getExtras().getString("username");
          othername = getIntent().getExtras().getString("othername");

        Log.i("alınandeger",Username+"--"+othername);

        chatusername=(TextView)findViewById(R.id.chatusername);
        geri=(ImageView) findViewById(R.id.geri);
        gonder=(ImageView) findViewById(R.id.gonder);
        chatEdittext=(EditText) findViewById(R.id.chatEdittext);
        chatusername.setText(othername);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,MainActivity.class);
                intent.putExtra("kadi",Username);
                startActivity(intent);
            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mesaj= chatEdittext.getText().toString();
                chatEdittext.setText("");
                mesajGonder(mesaj);
            }
        });
        chatRecyView = (RecyclerView)findViewById(R.id.chatRecyView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this,1);
        chatRecyView.setLayoutManager(layoutManager);
        mesajAdapter = new MesajAdapter(ChatActivity.this,list,ChatActivity.this,Username);
        chatRecyView.setAdapter(mesajAdapter);


    }
    public void mesajGonder(String text)
    {
        final String key = reference.child("Mesajlar").child(Username).child(othername).push().getKey();
        final Map messageMap=new HashMap();
        messageMap.put("text",text);
        messageMap.put("from",Username);
     reference.child("Mesajlar").child(Username).child(othername).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful())
             {
                 reference.child("Mesajlar").child(othername).child(Username).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {

                     }
                 });
             }
         }
     });

    }

    public void loadMesaj(){
        reference.child("Mesajlar").child(Username).child(othername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MesajModel mesajModel = dataSnapshot.getValue(MesajModel.class);
                list.add(mesajModel);
                mesajAdapter.notifyDataSetChanged();
                chatRecyView.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}