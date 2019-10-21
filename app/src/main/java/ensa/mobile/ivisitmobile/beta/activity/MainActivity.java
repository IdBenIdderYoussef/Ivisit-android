package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.PostRecyclerAdapter;
import ensa.mobile.ivisitmobile.beta.api.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {


    private Toolbar mainToolbar;
    private RecyclerView postListView;
    private List<Post> postList;
    private PostRecyclerAdapter postRecyclerAdapter;
    private FloatingActionButton addPostBtn;
    ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("I visit");

        postList = new ArrayList<>();
        postListView = findViewById(R.id.post_list);
        postRecyclerAdapter = new PostRecyclerAdapter(this,postList);
        postListView.setLayoutManager(new LinearLayoutManager(this));
        postListView.setAdapter(postRecyclerAdapter);

        addPostBtn = findViewById(R.id.add_post_btn);
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        //List<>
        //loadPosts();

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
        Call<List<Post>> call = ivisitAPIs.getAllPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                if (response.body() != null) {
                    progressDoalog.dismiss();
                    renderPosts(response.body());
                    System.out.println("Work");
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                /*
                Error callback
                */
                System.out.println("Not Working : "+t.getMessage());
                progressDoalog.dismiss();


            }
        });


    }

    private void renderPosts(List<Post> posts) {
        for (Post post : posts){
            postList.add(post);
            postRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);

        return true;
    }


    public void addPostBtnClick(View view){

        Intent newPostIntent= new Intent(MainActivity.this , AddPostActivity.class);
        startActivity(newPostIntent);

    }



    public void loadPosts(){





    }

}
