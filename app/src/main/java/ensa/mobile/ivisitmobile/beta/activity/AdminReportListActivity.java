package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.PostReportedRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportListActivity extends AppCompatActivity {
    private Toolbar postReportedToolbar;
    private RecyclerView postReportedListView;
    private List<Post> postReportedList;
    private PostReportedRecyclerAdapter postReportedRecyclerAdapter;
    ProgressDialog progressDoalog;
    private PostService postService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_report_list);

        postReportedToolbar = findViewById(R.id.post_reported_toolbar);
        setSupportActionBar(postReportedToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Post Reported List");


        postReportedList = new ArrayList<>();
        postReportedListView = findViewById(R.id.admin_report_list);
        postReportedRecyclerAdapter = new PostReportedRecyclerAdapter(this,postReportedList);
        postReportedListView.setLayoutManager(new LinearLayoutManager(this));
        postReportedListView.setAdapter(postReportedRecyclerAdapter);

        progressDoalog = new ProgressDialog(AdminReportListActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        postService = new PostService(PostApi.class);
        renderReportedPosts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void renderReportedPosts() {

        Call<List<Post>> call= postService.getApi().findAll();


        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.body() != null) {

                    for (Post post : response.body()) {
                        progressDoalog.dismiss();
                        if(post.getReports().size()>0){
                            postReportedList.add(post);
                        }

                        postReportedRecyclerAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.err.println("Error message : "+t.getMessage());
                progressDoalog.dismiss();
            }
        });

       /* Single<List<Post>> postsObservale = postService.getApi().findAll();
        postsObservale.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Post>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Post> posts) {

                        for (Post post : posts) {
                            progressDoalog.dismiss();
                            if(post.getReports().size()>0){
                                postReportedList.add(post);
                            }

                            postReportedRecyclerAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/
    }
}
