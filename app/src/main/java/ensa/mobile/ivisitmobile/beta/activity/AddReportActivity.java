package ensa.mobile.ivisitmobile.beta.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.adapter.CustomAdapter;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.ReportApi;
import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.model.Report;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import ensa.mobile.ivisitmobile.beta.api.services.ReportService;
import ensa.mobile.ivisitmobile.beta.security.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportActivity extends AppCompatActivity {

    int preSelectedIndex = -1;
    EditText tbHideOrNot;
    Report selectedReport = Report.builder().isSelected(false).reason("Violence").description("").build();
    private Toolbar reportPostToolbar;
    private Post post;
    private ReportService reportService;
    private PostService postService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_report);

        postService = new PostService(PostApi.class);
        reportService = new ReportService(ReportApi.class);

        reportPostToolbar = findViewById(R.id.main_toolbar_report);
        setSupportActionBar(reportPostToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Please Select a Problem");

        getPost();

        tbHideOrNot = findViewById(R.id.OtherdescReport);
        tbHideOrNot.setVisibility(View.INVISIBLE);

        ListView listView = (ListView) findViewById(R.id.listview);

        final List <Report> reports = new ArrayList<>();
        reports.add(Report.builder().isSelected(false).reason("Violence").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Harassment").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Suicide or Self-Injury").description("").build());
        reports.add(Report.builder().isSelected(false).reason("False News").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Spam").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Unauthorized Sales").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Hate Speech").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Terrorism").description("").build());
        reports.add(Report.builder().isSelected(false).reason("Other").description("").build());

        final CustomAdapter adapter= new CustomAdapter(this,reports);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Report model = reports.get(i); //changed it to model because viewers will confused about it


                if (model.isSelected()){

                    model.setSelected(false);
                    reports.set(i, model);
                    preSelectedIndex = -1;

                }else{

                    model.setSelected(true);
                    reports.set(i, model);

                    if (preSelectedIndex > -1){

                        Report preRecord = reports.get(preSelectedIndex);

                        preRecord.setSelected(false);

                        reports.set(preSelectedIndex, preRecord);

                    }

                    preSelectedIndex = i;

                }
                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method
                adapter.updateRecords(reports);
                selectedReport=reports.get(i);
                if(selectedReport.isSelected() == true)
                {tbHideOrNot.setVisibility(View.VISIBLE);}
                else{tbHideOrNot.setVisibility(View.INVISIBLE);}

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
                    post = response.body();
                    renderTitleReport(post);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                System.err.println("Error message : " + t.getMessage());

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderTitleReport(Post post) {
        //getSupportActionBar().setTitle(post.getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void sendReport(View view) {
        if(selectedReport.isSelected()){
            System.out.println("ReportName :" + selectedReport.getReason() +"selected? "+selectedReport.isSelected());
            //wala report Object est selectedREport
            //Maintenant on va persister le report dans la Base de donnee!!
            selectedReport.setDescription(tbHideOrNot.getText().toString());
            final  Report report = selectedReport;
            report.setAccount(Account.builder().username(App.getSession().getUsername()).build());
            //report.setCreatedDate(LocalDate.now().toString());

            Call<Void> call = reportService.getApi().create(report, post.getId());

            call.enqueue(new Callback<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getApplicationContext(), "Report sended Successfully", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
                }
            });

            //Redirect To PostDetails...
            //...


          //  Toast.makeText(this,"Report sended Successfully",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Select an Item !!",Toast.LENGTH_LONG).show();
        }
    }
}
