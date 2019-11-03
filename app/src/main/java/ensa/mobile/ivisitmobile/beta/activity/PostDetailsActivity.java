package ensa.mobile.ivisitmobile.beta.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.CommentRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.interfaces.CommentApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.LikeApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.Comment;
import ensa.mobile.ivisitmobile.beta.api.model.Like;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.services.CommentService;
import ensa.mobile.ivisitmobile.beta.api.services.LikeService;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import ensa.mobile.ivisitmobile.beta.security.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailsActivity extends AppCompatActivity {


    private boolean alreadyLiked;

    private ImageView userPictureView, postImageView;
    private TextView usernameTextView, dateCreationTimeTextView, postTitleTextView, postDescriptionTextView;
    private ImageButton moreBtn;
    private Button likeBtn, commentBtn;
    private LinearLayout profileLayout;

    private EditText commentContentEditText;
    private ImageButton sendBtn;
    private ImageView userCommentImage;

    private CommentRecyclerAdapter commentRecyclerAdapter;
    private RecyclerView commentListView;
    private List<Comment> commentList;

    private ProgressDialog progressDoalog;
    private Toolbar postDetailToolbar;

    private Post post;
    private PostService postService;
    private CommentService commentService;
    private LikeService likeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postService = new PostService(PostApi.class);
        commentService = new CommentService(CommentApi.class);
        likeService = new LikeService(LikeApi.class);

        postDetailToolbar = findViewById(R.id.post_detail_toolbar);
        setSupportActionBar(postDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        userPictureView = findViewById(R.id.user_picture_post_detail);
        postImageView = findViewById(R.id.image_post_detail);

        usernameTextView = findViewById(R.id.user_full_name_post_detail);
        dateCreationTimeTextView = findViewById(R.id.date_creation_post_detail);
        postTitleTextView = findViewById(R.id.title_post_detail);
        postDescriptionTextView = findViewById(R.id.description_post_detail);

        moreBtn = findViewById(R.id.moreBtn_post_detail);
        likeBtn = findViewById(R.id.like_count_btn_post_detail);
        commentBtn = findViewById(R.id.comments_count_btn_post_detail);

        profileLayout = findViewById(R.id.profile_layout);

        commentContentEditText = findViewById(R.id.add_comment_content);
        sendBtn = findViewById(R.id.send_comment_btn);
        userCommentImage = findViewById(R.id.user_add_comment_icon);

        commentList = new ArrayList<>();
        commentListView = findViewById(R.id.comments_recycleciew);
        commentRecyclerAdapter = new CommentRecyclerAdapter(this, commentList);
        commentListView.setLayoutManager(new LinearLayoutManager(this));
        commentListView.setAdapter(commentRecyclerAdapter);

        progressDoalog = new ProgressDialog(PostDetailsActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        getPost();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getIsLiked()) {

                    post.setIsLiked(false);
                    deleteLike(App.getSession().getUsername(), post.getId());
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    if(alreadyLiked == true){
                        likeBtn.setText(post.getLikes().size() - 1 +  " Likes");
                    }else {
                        likeBtn.setText(post.getLikes().size()  +  " Likes");
                    }

                } else{
                    post.setIsLiked(true);
                    createLike(post.getId());
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed, 0, 0, 0);
                    if(alreadyLiked == true){
                        likeBtn.setText(post.getLikes().size()  +  " Likes");
                    }else {
                        likeBtn.setText(post.getLikes().size() + 1 +  " Likes");
                    }
                }
            }

        });

    }


    public void getPost() {

        Call<Post> call = postService.getApi().get(getIntent().getLongExtra("postID", 0));

        call.enqueue(new Callback<Post>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.body() != null) {
                    progressDoalog.dismiss();
                    post = response.body();
                    post.setIsLiked(isLiked(post));
                    alreadyLiked = isLiked(post);
                    renderPost(post);
                    renderComments(post.getComments());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                System.err.println("Error message : " + t.getMessage());
                progressDoalog.dismiss();

            }
        });

    }

    public void createComment(final View view) {

        final Comment comment = Comment.builder().content(commentContentEditText.getText().toString()).build();
        Call<Void> call = commentService.getApi().create(comment, post.getId());

        call.enqueue(new Callback<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Toast.makeText(getApplicationContext(), "Comment added to DataBase", Toast.LENGTH_LONG).show();
                comment.setAccount(Account.builder().username(App.getSession().getUsername()).build());
//                comment.setCreatedDate(LocalDate.now().toString());
                commentList.add(comment);
                commentRecyclerAdapter.notifyDataSetChanged();
                post.getComments().add(comment);
                commentBtn.setText(post.getComments().size() + " Comments");


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();


            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderPost(Post post) {

        if (post.getAccount() != null) {
            usernameTextView.setText(post.getAccount().getUsername());
        }

        //dateCreationTimeTextView.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(post.getCreatedDate())));
        postTitleTextView.setText(post.getTitle());
        getSupportActionBar().setTitle(post.getTitle());
        postDescriptionTextView.setText(post.getDescription());
        commentBtn.setText(post.getComments().size() + " Comments");
        likeBtn.setText(post.getLikes().size() + " Likes");
        if (post.getIsLiked()) {
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed, 0, 0, 0);
        }
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .override(postImageView.getWidth(), postImageView.getHeight())
                .centerCrop(); // Overrides size of downloaded image and converts it's bitmaps to your desired image size;

        Glide.with(this).load(post.getPicture()).apply(reqOpt).into(postImageView);
    }

    public void renderComments(List<Comment> comments) {
        for (Comment comment : comments) {
            commentList.add(comment);
            commentRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public boolean isLiked(Post post) {
        for (Like like : post.getLikes()) {
            if (like.getAccount().getUsername().equals(App.getSession().getUsername()))
                return true;
        }
        return false;
    }


    public void createLike(Long postId) {

        Call<Like> call = likeService.getApi().create(new Like(), postId);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Like added to DataBase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void deleteLike(String username, Long postId) {

        Call<Void> call = likeService.getApi().delete(username, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Like deleted from DataBase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });

    }


}
