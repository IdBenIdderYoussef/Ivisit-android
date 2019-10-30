package ensa.mobile.ivisitmobile.beta.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.CommentRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.model.Comment;
import ensa.mobile.ivisitmobile.beta.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostDetailsActivity extends AppCompatActivity {


    ImageView userPictureView, postImageView;
    TextView usernameTextView , dateCreationTimeTextView , postTitleTextView , postDescriptionTextView;
    ImageButton moreBtn;
    Button likeBtn , commentBtn;
    LinearLayout profileLayout;

    EditText commentContentEditText;
    ImageButton sendBtn;
    ImageView userCommentImage;

    CommentRecyclerAdapter commentRecyclerAdapter;
    RecyclerView commentListView;
    List<Comment> commentList;

    ProgressDialog progressDoalog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);



 //       ActionBar actionBar = getSupportActionBar();
   //     actionBar.setTitle("Post Detail");
     //   actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setDisplayHomeAsUpEnabled(true);




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
        commentRecyclerAdapter = new CommentRecyclerAdapter(this , commentList);
        commentListView.setLayoutManager(new LinearLayoutManager(this));
        commentListView.setAdapter(commentRecyclerAdapter);

        progressDoalog = new ProgressDialog(PostDetailsActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
        Call<Post> call = ivisitAPIs.getPost(getIntent().getLongExtra("postID",0));

        call.enqueue(new Callback<Post>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                if (response.body() != null) {
                    progressDoalog.dismiss();
                    renderPost(response.body());
                    renderComments(response.body().getComments());
                    System.out.println("Work");
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                /*
                Error callback
                */
                System.out.println("Not Working : "+t.getMessage());
                progressDoalog.dismiss();


            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderPost(Post post) {
        usernameTextView.setText(post.getAccount().getUsername());
        dateCreationTimeTextView.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(post.getCreatedDate())));
        postTitleTextView.setText(post.getTitle());
        postDescriptionTextView.setText(post.getDescription());
        commentBtn.setText(post.getComments().size() + " Comments");
    }

    public void renderComments(List<Comment> comments){
        for (Comment comment : comments){
            commentList.add(comment);
            commentRecyclerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



}
