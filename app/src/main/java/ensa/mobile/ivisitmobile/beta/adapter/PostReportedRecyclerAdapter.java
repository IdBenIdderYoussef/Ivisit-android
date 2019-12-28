package ensa.mobile.ivisitmobile.beta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.activity.AdminReportedPostDetailsActivity;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.security.App;
import ensa.mobile.ivisitmobile.beta.security.Session;


public class PostReportedRecyclerAdapter extends RecyclerView.Adapter<PostReportedRecyclerAdapter.ViewHolder> {


    Context context;
    public List<Post> postReportedList;
    private Post postReported;
    private Session session;

    public PostReportedRecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postReportedList = postList;
        this.session = App.getSession();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_post_reported_item,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)//ask
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        postReported = postReportedList.get(position);
        holder.setPostReportedInfos(postReported);

        //set onClick pour voir les reports en details..
        holder.postreportedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, AdminReportedPostDetailsActivity.class);
                intent.putExtra("postID", postReportedList.get(position).getId());
                context.startActivity(intent);

                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postReportedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView userFullnameTextView;
        private TextView dateCreationTextView;
        private TextView titleTextView;
        private TextView nbrReports;
        private LinearLayout postreportedLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setPostReportedInfos(Post post) {
            userFullnameTextView = view.findViewById(R.id.user_full_name_post_reported_detail);
            dateCreationTextView = view.findViewById(R.id.date_creation_post_reported_detail);
            titleTextView = view.findViewById(R.id.title_post_reported_detail);
            nbrReports = view.findViewById(R.id.nbr_report);
            postreportedLayout = view.findViewById(R.id.post_reported);

            if (post.getAccount() != null) {
                userFullnameTextView.setText(post.getAccount().getUsername());
            }
            if (post.getCreatedDate() != null) {

                //           dateCreationTextView.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(post.getCreatedDate())));
                if (post.getCreatedDate() != null) {
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    Date parsed = null;
                    try {
                        parsed = parser.parse(post.getCreatedDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateCreationTextView.setText(formatter.format(parsed));
                }
            }
            titleTextView.setText(post.getTitle());
            nbrReports.setText(post.getReports().size()  + " Reports");

        }
    }

}
