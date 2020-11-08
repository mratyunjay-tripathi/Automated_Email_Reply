package tech.spirit.automatedemailreply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;

import java.util.ArrayList;

import static tech.spirit.automatedemailreply.Application_Class.content;
import static tech.spirit.automatedemailreply.Application_Class.user;

public class ContentofEmail extends AppCompatActivity {

    int i;
    ArrayList<String> reply=new ArrayList<>();

    TextView tvSubject,tvDate,tvFrom,tvTo,tvBody;
    TextView tvResponse1,tvResponse2,tvResponse3;
    Button btnReply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentof_email);

        tvSubject=findViewById(R.id.tvSubject);
        tvDate=findViewById(R.id.tvDate);
        tvFrom=findViewById(R.id.tvFrom);
        tvTo=findViewById(R.id.tvTo);
        tvBody=findViewById(R.id.tvBody);
        tvResponse1=findViewById(R.id.tvResponse1);
        tvResponse2=findViewById(R.id.tvResponse2);
        tvResponse3=findViewById(R.id.tvResponse3);
        btnReply=findViewById(R.id.btnReply);


         i=getIntent().getIntExtra("Index",-1);

        tvSubject.setText(Application_Class.list.get(i).getSubject());
        tvDate.setText(Application_Class.list.get(i).getDate());

        String sender=Application_Class.list.get(i).getSenderName()+"\n"+Application_Class.list.get(i).getFrom();
        tvFrom.setText(sender);

        if(content){
            tvTo.setText(Application_Class.list.get(i).getTo());

        }else{
            tvTo.setText(user.getDisplayName()+"\n"+Application_Class.list.get(i).getTo());
        }

        tvBody.setText(Application_Class.list.get(i).getContent());

        final ArrayList<String> forward=new ArrayList<>();
        forward.add( Application_Class.list.get(i).getFrom());

        if(!Application_Class.content){
            generateSmartReply();
            btnReply.setVisibility(View.VISIBLE);
        }else{
            btnReply.setVisibility(View.GONE);
        }





        tvResponse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward.add(reply.get(0));
                btnReply.performClick();
            }
        });

        tvResponse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward.add(reply.get(1));
                btnReply.performClick();
            }
        });

        tvResponse3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward.add(reply.get(2));
                btnReply.performClick();
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(forward.size()==1){
                    forward.add("Dear Sir/Mam, \n");
                }

                Intent intent=new Intent(ContentofEmail.this,MainActivity.class);
                intent.putExtra("to",forward);
                startActivity(intent);
                ContentofEmail.this.finish();
            }
        });


    }

    private void generateSmartReply(){
        ArrayList<FirebaseTextMessage> conversation=new ArrayList<>();

        String message=Application_Class.list.get(i).getContent();
        String user=Application_Class.list.get(i).getSenderName();

        conversation.add(FirebaseTextMessage.createForRemoteUser(
                message, System.currentTimeMillis(),user ));


        FirebaseSmartReply smartReply = FirebaseNaturalLanguage.getInstance().getSmartReply();
        smartReply.suggestReplies(conversation)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult result) {
                        if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {

                            Toast.makeText(ContentofEmail.this, "Language not supported", Toast.LENGTH_SHORT).show();

                        } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            // Task completed successfully
                            // ...

                            for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                reply.add(suggestion.getText());
                            }

                            if(reply.size()!=0) {
                                tvResponse1.setText(reply.get(0));
                                tvResponse2.setText(reply.get(1));
                                tvResponse3.setText(reply.get(2));

                                tvResponse1.setVisibility(View.VISIBLE);
                                tvResponse2.setVisibility(View.VISIBLE);
                                tvResponse3.setVisibility(View.VISIBLE);
                            }

                            if(reply.size()==0){
                                Toast.makeText(ContentofEmail.this, "No suitable pridiction, sorry", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ContentofEmail.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
