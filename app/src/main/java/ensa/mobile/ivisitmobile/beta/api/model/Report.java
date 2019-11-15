package ensa.mobile.ivisitmobile.beta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    boolean isSelected;

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("createdBy")
    @Expose
    private Account account;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}

