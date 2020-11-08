package tech.spirit.automatedemailreply;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static tech.spirit.automatedemailreply.Application_Class.content;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.ViewHolder> {

    ArrayList<EmailAttr> list;
    Context context;

    public EmailsAdapter(Context context,ArrayList<EmailAttr> list){

        this.context=context;
        this.list=list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDate,tvSubject,tvContent;
        CardView cvemailList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            tvName=itemView.findViewById(R.id.tvName);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvSubject=itemView.findViewById(R.id.tvSubject);
            tvContent=itemView.findViewById(R.id.tvContent);
            cvemailList=itemView.findViewById(R.id.cvemailList);

            cvemailList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               //     Toast.makeText(context, "show email", Toast.LENGTH_SHORT).show();
                    int i=list.indexOf(view.getTag());

                    Intent intent=new Intent(view.getContext(),ContentofEmail.class);
                    intent.putExtra("Index",i);
                    view.getContext().startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @NonNull
    @Override
    public EmailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.emails_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailsAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(list.get(position));
        holder.cvemailList.setTag(list.get(position));
        if(!content){
            holder.tvName.setText(list.get(position).getSenderName());
        }else{
            holder.tvName.setText("me");
        }

        holder.tvDate.setText(list.get(position).getDate().toString());
        holder.tvSubject.setText(list.get(position).getSubject());
        holder.tvContent.setText(list.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
}
