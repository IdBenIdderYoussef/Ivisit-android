package ensa.mobile.ivisitmobile.beta.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.model.Comment;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment comment = commentList.get(position);
        holder.setCommentInfos(comment);

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setCommentInfos(Comment comment){
            if(comment.getAccount() != null){
                usernameComment.setText(comment.getAccount().getUsername());
            }
//            dateCreationComment.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(comment.getCreatedDate())));
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            Date parsed = null;
            try {
                parsed = parser.parse(comment.getCreatedDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateCreationComment.setText(formatter.format(parsed));
            commentContent = view.findViewById(R.id.comment_content);
            commentContent.setText(comment.getContent());
        }

    }
}
