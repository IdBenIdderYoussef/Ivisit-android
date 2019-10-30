package ensa.mobile.ivisitmobile.beta.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Post post = postList.get(position);
        holder.setPostInfos(post);

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
        private TextView userFullnameTextView;
        private TextView dateCreationTextView;
        private TextView descriptionTextView;
        private TextView titleTextView;
        private ImageView postImage;
        private Button commentsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            postImage = itemView.findViewById(R.id.image_post_detail);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setPostInfos(Post post){
            userFullnameTextView = view.findViewById(R.id.user_full_name_post_detail);
            dateCreationTextView = view.findViewById(R.id.date_creation_post_detail);
            descriptionTextView = view.findViewById(R.id.description_post_detail);
            titleTextView = view.findViewById(R.id.title_post_detail);
            commentsButton = view.findViewById(R.id.comments_count_btn_post_detail);

            if(post.getAccount() != null){
                userFullnameTextView.setText(post.getAccount().getUsername());
            }
            if(post.getCreatedDate() != null){

                dateCreationTextView.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(post.getCreatedDate())));
            }
            commentsButton.setText(post.getComments().size() +" Comments");
            descriptionTextView.setText(post.getDescription());
            titleTextView.setText(post.getTitle());


        }

    }

}
