package tech.spirit.automatedemailreply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static tech.spirit.automatedemailreply.Application_Class.user;

public class MainActivity extends AppCompatActivity {

    EditText etTo,etSubject,etBody;
    TextView tvSendFrom;
    ImageView ivSend;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseRefrence;

    String replyText="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSendFrom=findViewById(R.id.tvSendFrom);
        etTo=findViewById(R.id.etTo);
        etSubject=findViewById(R.id.etSubject);
        etBody=findViewById(R.id.etBody);
        ivSend=findViewById(R.id.ivSendemail);


        final String uName=user.getDisplayName();
        final String uEmail=user.getEmail();
        tvSendFrom.setText(uName+"\n"+uEmail);

        ArrayList<String> receive;
        receive=getIntent().getStringArrayListExtra("to");

        if(receive!=null) {
            etTo.setText(receive.get(0));
            etBody.setText(receive.get(1));
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseRefrence=firebaseDatabase.getReference().child("Emails");






        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!etTo.getText().toString().equals("") && !etSubject.getText().toString().equals("")
                        && !etBody.getText().toString().equals("")) {


                    java.util.Date date = new java.util.Date();

                    EmailAttr emailAttr = new EmailAttr(uEmail, uName, etTo.getText().toString(),
                            etSubject.getText().toString(), etBody.getText().toString(), date.toString());

                    databaseRefrence.push().setValue(emailAttr);

                    MainActivity.this.finish();
                    Toast.makeText(MainActivity.this, "Email has been sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Please fill all fields! ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
