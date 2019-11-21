package ensa.mobile.ivisitmobile.beta.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
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
import ensa.mobile.ivisitmobile.beta.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailsActivity extends AppCompatActivity {


    private boolean alreadyLiked;

    private RelativeLayout commentRelativeLayout;

    private ImageView userPictureView, postImageView;
    private TextView usernameTextView, dateCreationTimeTextView, postTitleTextView, postDescriptionTextView, addressTextView;
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

    private LinearLayoutManager linearLayoutManager;

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
        addressTextView = findViewById(R.id.address_creation_post_detail);

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
        linearLayoutManager = new LinearLayoutManager((this));
        linearLayoutManager.setStackFromEnd(true);
        commentListView.setLayoutManager(linearLayoutManager);
        commentListView.setAdapter(commentRecyclerAdapter);

        progressDoalog = new ProgressDialog(PostDetailsActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();


        commentRelativeLayout = findViewById(R.id.add_comment_layout);

        if (App.getSession().getAccessToken() == null || App.getSession().getAccessToken().equals("")) {
            commentRelativeLayout.setVisibility(View.GONE);
            moreBtn.setVisibility(View.GONE);
        }

        getPost();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getSession().getAccessToken() == null || App.getSession().getAccessToken().equals("")) {
                    showAlertDialogButtonClicked();
                } else {
                    if (post.getIsLiked()) {

                        post.setIsLiked(false);
                        deleteLike(App.getSession().getUsername(), post.getId());
                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                        if (alreadyLiked == true) {
                            likeBtn.setText(post.getLikes().size() - 1 + " Likes");
                        } else {
                            likeBtn.setText(post.getLikes().size() + " Likes");
                        }

                    } else {
                        post.setIsLiked(true);
                        createLike(post.getId());
                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed, 0, 0, 0);
                        if (alreadyLiked == true) {
                            likeBtn.setText(post.getLikes().size() + " Likes");
                        } else {
                            likeBtn.setText(post.getLikes().size() + 1 + " Likes");
                        }
                    }
                }
            }

        });


        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOption(moreBtn, post);
            }
        });

    }


    public void showAlertDialogButtonClicked() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("You have to sign in fo like or comment !");
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PostDetailsActivity.this, LoginActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void getPost() {

        Call<Post> call = postService.getApi().get(getIntent().getLongExtra("postID", 0));
        System.out.println("Post Id :" + getIntent().getLongExtra("postID", 0));
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

                comment.setAccount(Account.builder().username(App.getSession().getUsername()).build());
                comment.setCreatedDate(LocalDate.now().toString());
                commentList.add(comment);
                commentRecyclerAdapter.notifyDataSetChanged();
                post.getComments().add(comment);
                commentBtn.setText(post.getComments().size() + " Comments");
                commentContentEditText.setText("");
                closeKeyboard();
                commentContentEditText.clearFocus();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

    }

    private void closeKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

        if (post.getCreatedDate() != null) {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            Date parsed = null;
            try {
                parsed = parser.parse(post.getCreatedDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateCreationTimeTextView.setText(formatter.format(parsed));
        }

        postTitleTextView.setText(post.getTitle());
        getSupportActionBar().setTitle(post.getTitle());
        postDescriptionTextView.setText(post.getDescription());
        commentBtn.setText(post.getComments().size() + " Comments");
        likeBtn.setText(post.getLikes().size() + " Likes");
        if (post.getAddress() != null) {
            addressTextView.setText(post.getAddress().getCity() + " " + post.getAddress().getCountry());
        }
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


    private void showMoreOption(ImageButton moreButton, final Post postSelected) {

        PopupMenu popupMenu = new PopupMenu(this.getBaseContext(), moreButton, Gravity.END);
        if (postSelected.getAccount() != null && postSelected.getAccount().getUsername().equals(App.getSession().getUsername())) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        } else {
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Report");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {

                    deletePost(post);
                }
                if (item.getItemId() == 1) {
                    reportPost(postSelected);
                }
                return false;
            }
        });

        popupMenu.show();

    }


    public void deletePost(final Post post) {
        Call<Void> call = postService.getApi().delete(post.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Intent intent = new Intent(PostDetailsActivity.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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

                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });


    }

    public void deleteLike(String username, Long postId) {

        Call<Void> call = likeService.getApi().delete(username, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public void reportPost(Post post) {

        Intent intent = new Intent(this, AddReportActivity.class);
        intent.putExtra("postID", post.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }


}
