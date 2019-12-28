package ensa.mobile.ivisitmobile.beta.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.model.Report;

public class ReportRecyclerAdapter extends RecyclerView.Adapter<ReportRecyclerAdapter.ViewHolder> {

    Context context;
    public List<Report> reportList;

    public ReportRecyclerAdapter(Context context, List<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_item,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Report report = reportList.get(position);
        holder.setReportInfos(report);

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private ImageView userPictureReport;
        TextView usernameReport , dateCreationReport , reportReason , reportDesc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            userPictureReport = itemView.findViewById(R.id.user_report_icon);
            usernameReport = itemView.findViewById(R.id.user_name_report);
            dateCreationReport = itemView.findViewById(R.id.date_creation_report);
            reportReason = itemView.findViewById(R.id.report_reason);
            reportDesc = itemView.findViewById(R.id.report_desc);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setReportInfos(Report report){
            if(report.getAccount() != null){
                usernameReport.setText(report.getAccount().getUsername());
            }
//            dateCreationComment.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(comment.getCreatedDate())));
            reportReason = view.findViewById(R.id.report_reason);
            reportReason.setText(report.getReason());
            reportDesc = view.findViewById(R.id.report_desc);
            reportDesc.setText(report.getDescription());
            if (report.getCreatedDate() != null) {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                Date parsed = null;
                try {
                    parsed = parser.parse(report.getCreatedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateCreationReport.setText(formatter.format(parsed));
            }
        }

    }


}
