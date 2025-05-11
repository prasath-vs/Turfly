package com.example.turfly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HelpPagerAdapter extends RecyclerView.Adapter<HelpPagerAdapter.HelpViewHolder> {

    private final List<HelpPage> helpPages;

    public HelpPagerAdapter(List<HelpPage> helpPages) { // ✅ Fixed Constructor
        this.helpPages = helpPages;
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_page, parent, false);
        // ✅ Ensure ViewPager2 respects match_parent
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));.
        return new HelpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        HelpPage page = helpPages.get(position);
        holder.title.setText(page.getTitle());
        holder.description.setText(page.getDescription());
        holder.icon.setImageResource(page.getIconResId());
    }

    @Override
    public int getItemCount() {
        return helpPages.size();
    }

    public static class HelpViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView icon;

        public HelpViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.helpTitle);
            description = itemView.findViewById(R.id.helpDescription);
            icon = itemView.findViewById(R.id.helpIcon);
        }
    }
}
