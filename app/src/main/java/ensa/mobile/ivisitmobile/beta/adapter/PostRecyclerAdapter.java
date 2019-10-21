package ensa.mobile.ivisitmobile.beta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.activity.PostDetailsActivity;
import ensa.mobile.ivisitmobile.beta.model.Post;


public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {


    Context context;
    public List<Post> postList;

    public PostRecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String description = postList.get(position).getDescription();
        holder.setDescriptionText(description);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postID",postList.get(position).getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView descriptionTextView;
        private ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            postImage = itemView.findViewById(R.id.image_post_detail);
        }

        public void setDescriptionText(String descriptionText){
            descriptionTextView = view.findViewById(R.id.description_post_detail);
            descriptionTextView.setText(descriptionText);
        }

    }

}
