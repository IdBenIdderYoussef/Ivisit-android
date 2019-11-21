package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    private Toolbar mainToolbar;
    public static RecyclerView postListView;
    private List<Post> postList;
    private List<Post> postListAll;
    public static PostRecyclerAdapter postRecyclerAdapter;
    private FloatingActionButton addPostBtn;
    ProgressDialog progressDialog;
    private ClipData.Item profileItem;
    private PostService postService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        System.out.println("ROLE ----------------------- :" + App.getSession().getRole());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("I visit");


        postList = new ArrayList<>();
        postListAll = new ArrayList<>(postList);
        postListView = findViewById(R.id.post_list);
        postRecyclerAdapter = new PostRecyclerAdapter(this, postList);
        postListView.setLayoutManager(new LinearLayoutManager(this));
        postListView.setAdapter(postRecyclerAdapter);

        addPostBtn = findViewById(R.id.add_post_btn);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        postService = new PostService(PostApi.class);

        renderPosts();

    }

    private void renderPosts() {

        Call<List<Post>> call = postService.getApi().findAll();


        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.body() != null) {
                    System.out.println(response.body().get(0));
                    for (Post post : response.body()) {
                        progressDialog.dismiss();
                        post.setIsLiked(isLiked(post));
                        post.setIsAlreadyLiked(post.getIsLiked());
                        postList.add(post);
                        postListAll.add(post);
                        postRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.err.println("Error message : " + t.getMessage());
                progressDialog.dismiss();
            }
        });
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
        postList.removeAll(postList);
        postListAll.removeAll(postListAll);
        postRecyclerAdapter = new PostRecyclerAdapter(this, postList);
        postListView.setLayoutManager(new LinearLayoutManager(this));
        postListView.setAdapter(postRecyclerAdapter);
        renderPosts();
        super.onRestart();
    }


    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //aniss

        if(App.getSession().getRole().equals("ROLE_ADMIN")){
            getMenuInflater().inflate(R.menu.main_menu_admin, menu);

        }else{
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    closeKeyboard(v);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) {
                    postList.clear();
                    postList.addAll(postListAll);
                } else {
                    postList.clear();
                    for (Post post : postListAll) {
                        if (post.getTitle().toLowerCase().contains(query.toLowerCase())
                                || post.getAddress().getCountry().toLowerCase().contains(query.toLowerCase())
                                || post.getAddress().getCity().toLowerCase().contains(query.toLowerCase())) {
                            postList.add(post);

                        }
                    }
                    if (postList.size() == 0) {
                        Toast.makeText(MainActivity.this, "we couldn't find anything for :" + query, Toast.LENGTH_SHORT).show();
                    }
                }
                postRecyclerAdapter.notifyDataSetChanged();
                //mabghatch tkhdem b interface filterable
                //postRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });
        //aniss-fin

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.profile:
                    if (App.getSession().getAccessToken().equals("")) {
                        Intent compteIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(compteIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Intent compteIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(compteIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    break;
                case R.id.reported_post_list:
                    Intent ReportIntent = new Intent(MainActivity.this, AdminReportListActivity.class);
                    startActivity(ReportIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        } catch (Exception e) {
            Log.i("Sleep Recorder", e.toString());
        }
        return true;
    }

    public void addPostBtnClick(View view) {

        if (App.getSession().getAccessToken().equals("")) {
            Intent newPostIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(newPostIntent);
        } else {
            Intent newPostIntent = new Intent(MainActivity.this, AddPostActivity.class);
            startActivity(newPostIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


}
