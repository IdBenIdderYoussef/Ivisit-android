package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.ReportRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.interfaces.CommentApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.LikeApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.model.Report;
import ensa.mobile.ivisitmobile.beta.api.services.CommentService;
import ensa.mobile.ivisitmobile.beta.api.services.LikeService;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportedPostDetailsActivity extends AppCompatActivity {

    private ImageView userPictureView, postImageView;
    private TextView usernameTextView, dateCreationTimeTextView, postTitleTextView, postDescriptionTextView;
    private ImageButton moreBtn;
    private Button likeBtn, commentBtn;
    private LinearLayout profileLayout;

    private ReportRecyclerAdapter reportRecyclerAdapter;
    private RecyclerView reportListView;
    private List<Report> reportList;

    private ProgressDialog progressDoalog;
    private Toolbar postReportedDetailToolbar;

    private Post post;
    private PostService postService;
    private CommentService commentService;
    private LikeService likeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_reported_post_details);

        postService = new PostService(PostApi.class);
        commentService = new CommentService(CommentApi.class);
        likeService = new LikeService(LikeApi.class);

        postReportedDetailToolbar = findViewById(R.id.post_reported_detail_toolbar);
        setSupportActionBar(postReportedDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        userPictureView = findViewById(R.id.user_picture_post_reported_detail);
        postImageView = findViewById(R.id.image_post_reported_detail);

        usernameTextView = findViewById(R.id.user_full_name_post_reported_detail);
        dateCreationTimeTextView = findViewById(R.id.date_creation_post_reported_detail);
        postTitleTextView = findViewById(R.id.title_post_reported_detail);
        postDescriptionTextView = findViewById(R.id.description_post_reported_detail);

        moreBtn = findViewById(R.id.moreBtn_post_reported_detail);
        likeBtn = findViewById(R.id.like_count_btn_post_reported_detail);
        commentBtn = findViewById(R.id.comments_count_btn_post_reported_detail);

        profileLayout = findViewById(R.id.post_reported_profile_layout);


        reportList = new ArrayList<>();
        reportListView = findViewById(R.id.reports_recycleciew);
        reportRecyclerAdapter = new ReportRecyclerAdapter(this, reportList);
        reportListView.setLayoutManager(new LinearLayoutManager(this));
        reportListView.setAdapter(reportRecyclerAdapter);

        progressDoalog = new ProgressDialog(AdminReportedPostDetailsActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        getPost();

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
                    renderPost(post);
                    renderReports(post.getReports());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                System.err.println("Error message : " + t.getMessage());
                progressDoalog.dismiss();

            }
        });

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
        /*if (post.getIsLiked()) {
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_pressed, 0, 0, 0);
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void renderReports(List<Report> reports) {
        for (Report report : reports) {
            reportList.add(report);
            reportRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
