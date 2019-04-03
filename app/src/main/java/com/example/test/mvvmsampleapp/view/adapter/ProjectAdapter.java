package com.example.test.mvvmsampleapp.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.test.mvvmsampleapp.R;
import com.example.test.mvvmsampleapp.databinding.ProjectListItemBinding;
import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.model.Project;
import com.example.test.mvvmsampleapp.view.callback.ProjectClickCallback;

import java.util.List;
import java.util.Objects;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    List<? extends InnerWeather> projectList;

    @Nullable
    private final ProjectClickCallback projectClickCallback;

    public ProjectAdapter(@Nullable ProjectClickCallback projectClickCallback) {
        this.projectClickCallback = projectClickCallback;
    }

    public void setProjectList(final List<? extends InnerWeather> projectList) {
        if (this.projectList == null) {
            this.projectList = projectList;
            notifyDataSetChanged();
        } else {
            this.projectList = projectList;
            notifyDataSetChanged();
        }
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProjectListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.project_list_item,
                        parent, false);
        binding.setCallback(projectClickCallback);
        return new ProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        holder.binding.setWeather(projectList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        final ProjectListItemBinding binding;

        public ProjectViewHolder(ProjectListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
