package ensa.mobile.ivisitmobile.beta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.activity.PostDetailsActivity;
import ensa.mobile.ivisitmobile.beta.api.interfaces.LikeApi;
import ensa.mobile.ivisitmobile.beta.api.model.Like;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.services.LikeService;
import ensa.mobile.ivisitmobile.beta.security.App;
import ensa.mobile.ivisitmobile.beta.security.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {


    Context context;
    public List<Post> postList;
    private Post post;
    private LikeService likeService;
    private Session session;
    private boolean isLiked;

    public PostRecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.session = App.getSession();
        likeService = new LikeService(LikeApi.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        post = postList.get(position);
        holder.setPostInfos(post);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postID", postList.get(position).getId());
                context.startActivity(intent);

                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postList.get(position).getIsLiked()) {

                    postList.get(position).setIsLiked(false);
                    deleteLike(App.getSession().getUsername(), postList.get(position).getId());
                    holder.likesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    holder.likesButton.setText(postList.get(position).getLikes().size() + " Likes");

                } else {

                    postList.get(position).setIsLiked(true);
                    createLike(postList.get(position).getId());
                    holder.likesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed, 0, 0, 0);
                    holder.likesButton.setText(postList.get(position).getLikes().size() + 1 + " Likes");
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView userFullnameTextView;
        private TextView dateCreationTextView;
        private TextView descriptionTextView;
        private TextView titleTextView;
        private ImageView postImage;
        private Button commentsButton;
        private Button likesButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            postImage = itemView.findViewById(R.id.image_post_detail);
            likesButton = itemView.findViewById(R.id.like_count_btn_post_detail);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setPostInfos(Post post) {
            userFullnameTextView = view.findViewById(R.id.user_full_name_post_detail);
            dateCreationTextView = view.findViewById(R.id.date_creation_post_detail);
            descriptionTextView = view.findViewById(R.id.description_post_detail);
            titleTextView = view.findViewById(R.id.title_post_detail);
            commentsButton = view.findViewById(R.id.comments_count_btn_post_detail);

            if (post.getAccount() != null) {
                userFullnameTextView.setText(post.getAccount().getUsername());
            }
            if (post.getCreatedDate() != null) {

                dateCreationTextView.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(post.getCreatedDate())));
            }
            if (post.getIsLiked()){
                likesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed,0,0,0);
            }
            commentsButton.setText(post.getComments().size()  + " Comments");
            descriptionTextView.setText(post.getDescription());
            titleTextView.setText(post.getTitle());
            likesButton.setText(post.getLikes().size() + " Likes");

        }

    }


    public void createLike(Long postId) {

        Call<Like> call = likeService.getApi().create(new Like(), postId);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.body() != null) {
                    Toast.makeText(context, "Like added to DataBase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(context, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void deleteLike(String username, Long postId) {

        Call<Void> call = likeService.getApi().delete(username, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.body() != null) {
                    Toast.makeText(context, "Like deleted from DataBase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(context, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });

    }


}
