package ensa.mobile.ivisitmobile.beta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.model.Comment;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    Context context;
    public List<Comment> commentList;


    public CommentRecyclerAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String content = commentList.get(position).getContent();
        holder.setCommentContentText(content);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private ImageView userPictureComment;
        TextView usernameComment , dateCreationComment , commentContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            userPictureComment = itemView.findViewById(R.id.user_comment_icon);
            usernameComment = itemView.findViewById(R.id.user_name_comment);
            dateCreationComment = itemView.findViewById(R.id.date_creation_comment);
            commentContent = itemView.findViewById(R.id.comment_content);

        }

        public void setCommentContentText(String contentTextText){
            commentContent = view.findViewById(R.id.comment_content);
            commentContent.setText(contentTextText);
        }

    }
}
