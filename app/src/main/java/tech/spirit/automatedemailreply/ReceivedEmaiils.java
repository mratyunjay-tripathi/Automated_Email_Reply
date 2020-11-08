package tech.spirit.automatedemailreply;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static tech.spirit.automatedemailreply.Application_Class.content;
import static tech.spirit.automatedemailreply.Application_Class.list;
import static tech.spirit.automatedemailreply.Application_Class.mAuth;
import static tech.spirit.automatedemailreply.Application_Class.user;

public class ReceivedEmaiils extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter myadapter;
    RecyclerView.LayoutManager layoutManager;

    TextView tvUsername,tvUserEmail,tvContent;
    ImageView ivCompose,ivLogout;
    CardView cvChangeContent;
    private  Boolean callflag=false;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    private ArrayList<EmailAttr> compList=new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(content){
            tvContent.setText("Sent");
        }else{
            tvContent.setText("Received");
        }

        myadapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_emaiils);

        ivCompose=findViewById(R.id.ivCompose);
        ivLogout=findViewById(R.id.ivLogout);
        tvUsername=findViewById(R.id.tvUserName);
        tvUserEmail=findViewById(R.id.tvUserEmail);
        tvContent=findViewById(R.id.tvContent);
        cvChangeContent=findViewById(R.id.cvChangeCont);
        recyclerView=findViewById(R.id.rvmailList);

        final String uEmail=user.getEmail();
        tvUsername.setText(user.getDisplayName());
        tvUserEmail.setText(uEmail);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        Application_Class.list=new ArrayList<>();
        myadapter=new EmailsAdapter(this,Application_Class.list);
        recyclerView.setAdapter(myadapter);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Emails");

        readData();
       // setactivityContent();


        cvChangeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setactivityContent();
            }
        });

        ivCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReceivedEmaiils.this,MainActivity.class);
                startActivity(intent);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                user=null;
                startActivity(new Intent(ReceivedEmaiils.this,Login_Activity.class));
                ReceivedEmaiils.this.finish();
            }
        });

    }

    private void setactivityContent(){
        int size=0;

        if(compList!=null)
            size=compList.size();

            if(content){
            content=false;
            tvContent.setText("Received");

            list.clear();

            for(int i=0;i<size;i++){
                if(compList.get(i).getTo().equals(user.getEmail())){
                    list.add(compList.get(i));

                }
            }
                Collections.reverse(list);
            myadapter.notifyDataSetChanged();

        }else if(!content){
            content=true;
            tvContent.setText("Sent");

            list.clear();
            for(int i=0;i<size;i++){

                if(compList.get(i).getFrom().equals(user.getEmail())){
                    list.add(compList.get(i));
                }
            }
                Collections.reverse(list);
            myadapter.notifyDataSetChanged();

        }
    }

    private void readData(){

        if(!callflag) {
            callflag=true;
            // lNointernet.setVisibility(View.GONE);
            Application_Class.list.clear();

            if(compList!=null)
            compList.clear();

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    EmailAttr emailData=dataSnapshot.getValue(EmailAttr.class);

                        callflag=false;
                        compList.add(emailData);

                        if(emailData.getTo().equals(user.getEmail())){
                            list.add(emailData);
                        }

                        Log.d("c_info", emailData.getTo());
                        Collections.reverse(list);
                        if(list==null)
                            Toast.makeText(ReceivedEmaiils.this, "No Emails Received", Toast.LENGTH_SHORT).show();
                    myadapter.notifyDataSetChanged();

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
            };

            databaseReference.addChildEventListener(childEventListener);
        }


    }
}
