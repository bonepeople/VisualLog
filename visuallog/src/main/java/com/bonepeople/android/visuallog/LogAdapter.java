package com.bonepeople.android.visuallog;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class LogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder_data(new LogView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder_data view = (ViewHolder_data) holder;
        LogInfo data = VisualLog.data.get(position);
        view.logView.updateView(data);
    }

    @Override
    public int getItemCount() {
        return VisualLog.data.size();
    }

    private static class ViewHolder_data extends RecyclerView.ViewHolder {
        private LogView logView;

        private ViewHolder_data(@NonNull LogView itemView) {
            super(itemView);
            logView = itemView;
        }
    }
}
