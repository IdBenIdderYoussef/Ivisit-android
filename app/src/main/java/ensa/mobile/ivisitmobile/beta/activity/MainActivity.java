package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.PostRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Like;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import ensa.mobile.ivisitmobile.beta.security.App;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    private Toolbar mainToolbar;
    private RecyclerView postListView;
    private List<Post> postList;
    private PostRecyclerAdapter postRecyclerAdapter;
    private FloatingActionButton addPostBtn;
    ProgressDialog progressDoalog;
    private ClipData.Item profileItem;
    private PostService postService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("I visit");


        postList = new ArrayList<>();
        postListView = findViewById(R.id.post_list);
        postRecyclerAdapter = new PostRecyclerAdapter(this, postList);
        postListView.setLayoutManager(new LinearLayoutManager(this));
        postListView.setAdapter(postRecyclerAdapter);

        addPostBtn = findViewById(R.id.add_post_btn);
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        postService = new PostService(PostApi.class);

        renderPosts();


    }

    private void renderPosts() {

        Single<List<Post>> postsObservale = postService.getApi().findAll();
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
                            post.setIsLiked(isLiked(post));
                            postList.add(post);
                            postRecyclerAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


        /*call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.body() != null) {
                    for (Post post : response.body()) {
                        progressDoalog.dismiss();
                        postList.add(post);
                        postRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.err.println("Error message : "+t.getMessage());
                progressDoalog.dismiss();
            }
        });*/
    }

    public boolean isLiked(Post post) {
        for (Like like : post.getLikes()) {
            if (like.getAccount().getUsername().equals(App.getSession().getUsername()))
                return true;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        postList = new ArrayList<>();
        postRecyclerAdapter.notifyDataSetChanged();
        renderPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.profile:
                    Intent compteIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(compteIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        } catch (Exception e) {
            Log.i("Sleep Recorder", e.toString());
        }
        return true;
    }

    public void addPostBtnClick(View view) {

        Intent newPostIntent = new Intent(MainActivity.this, AddPostActivity.class);
        startActivity(newPostIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


}
