package com.meeto.meeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Giris extends AppCompatActivity {
   EditText kullanıcıadı;
   Button girisbuton;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        t1();
    }
    public void t1()
    {
        kullanıcıadı = (EditText)findViewById(R.id.kullanıcıadı);
        girisbuton=(Button)findViewById(R.id.girisbuton);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();

        girisbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username=kullanıcıadı.getText().toString();
                kullanıcıadı.setText("");
                        ekle(Username);
            }
        });
    }
    public void ekle(final String kadi)
    {
        reference.child("kullanıcılar").child(kadi).child("kullanicilar").setValue(kadi).addOnCompleteListener(new OnCompleteListener<Void>()

        {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                Toast.makeText(getApplicationContext(),"Başarı ile giris yaptınız",Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(Giris.this,MainActivity.class);
                    intent.putExtra("kadi",kadi);
                    startActivity(intent);
            }
    }
        });
    }
}