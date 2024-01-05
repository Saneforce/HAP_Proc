package com.hap.checkinproc.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.ModelOutletsApprovalHistory;

import java.util.ArrayList;

public class AdapterOutletsApprovalHistory extends RecyclerView.Adapter<AdapterOutletsApprovalHistory.ViewHolder> {
    ArrayList<ModelOutletsApprovalHistory> list;
    Context context;
    ArrayList<ModelOutletsApprovalHistory> listNew;
    public AdapterOutletsApprovalHistory(ArrayList<ModelOutletsApprovalHistory> list, Context context) {
        this.list = list;
        this.context = context;
        this.listNew=list;
    }

    @NonNull
    @Override
    public AdapterOutletsApprovalHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOutletsApprovalHistory.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOutletsApprovalHistory.ViewHolder holder, int position) {
        ModelOutletsApprovalHistory model = list.get(position);
        holder.Name.setText(model.getName());
        holder.Code.setText(model.getCode());
        holder.Mobile.setText(model.getMobile());
        holder.Address.setText(model.getAddress());

        holder.Mobile.setOnClickListener(v -> {
            Common_Class common_class = new Common_Class(context);
            common_class.makeCall(model.getMobile());
        });

        if (model.getStatus().equals("0")) {
            holder.Approve.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.GONE);
            holder.Remarks.setVisibility(View.GONE);
            holder.ApprovedBy.setText("Approved By: " + model.getApprovedBy());
            holder.ModifiedOn.setText("Approved On: " + model.getModifiedOn());
        } else if (model.getStatus().equals("1")) {
            holder.Approve.setVisibility(View.GONE);
            holder.Reject.setVisibility(View.VISIBLE);
            holder.ApprovedBy.setText("Rejected By: " + model.getApprovedBy());
            holder.ModifiedOn.setText("Rejected On: " + model.getModifiedOn());
            holder.Remarks.setText("Remarks: " + model.getRemarks());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim().replaceAll("\\s", "");
                ArrayList<ModelOutletsApprovalHistory> filteredList = new ArrayList<>();
                ArrayList<ModelOutletsApprovalHistory> filteredany = new ArrayList<>();
                for (ModelOutletsApprovalHistory row : listNew) {
                    String sName = row.getName().toLowerCase().trim().replaceAll("\\s", "");
                    String getModifyDate = (row.getModifiedOn()!= null) ? row.getModifiedOn().toLowerCase().trim().replaceAll("\\s", "") : "";
                    //String getPhone = (row.getCustomerMobile() != null) ? row.getCustomerMobile().toLowerCase().trim().replaceAll("\\s", "") : "";
                    if ((";" + sName).contains(";" + charString) || (";" + getModifyDate).contains(";" + charString)) {
                        filteredList.add(row);
                    } else if (sName.contains(charString) || getModifyDate.contains(charString)) {
                        filteredany.add(row);
                    }
                }
                filteredList.addAll(filteredany);
                list = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<ModelOutletsApprovalHistory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Approve, Reject, Name, Code, Mobile, Address, ApprovedBy, ModifiedOn, Remarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Approve = itemView.findViewById(R.id.approved);
            Reject = itemView.findViewById(R.id.rejected);
            Name = itemView.findViewById(R.id.customerName_outletInfo);
            Code = itemView.findViewById(R.id.customerId_outletInfo);
            Mobile = itemView.findViewById(R.id.mobile_outletInfo);
            Address = itemView.findViewById(R.id.address_outletInfo);
            ApprovedBy = itemView.findViewById(R.id.approvedBy);
            ModifiedOn = itemView.findViewById(R.id.approvedOn);
            Remarks = itemView.findViewById(R.id.remarks);
        }
    }
}
