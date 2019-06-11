package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.GenericItem;


public class CropParcelAdapter extends RecyclerView.Adapter<CropParcelAdapter.ViewHolder> {

    private final List<GenericItem> dataset;
    private String role;

    public CropParcelAdapter(List<GenericItem> dataset, String role) {
        this.dataset = dataset;
        this.role = role;
    }

    /**
     * The item ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        Context context;
        TextView firstLine, secondLine;
        GenericItem item;
        int pos;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            firstLine = itemView.findViewById(R.id.item_first_line);
            secondLine = itemView.findViewById(R.id.item_second_line);
            // Set Click listener
            view.setOnClickListener(this);
        }

        void display(int position, int backgroundId) {
            pos = position;
            item = dataset.get(position);
            @ColorRes int textColor = R.color.primary_text;
            if (item.referenceName.contains(role)) {
                backgroundId = R.color.basic_blue;
                textColor = R.color.white;
            }
            // Set colors
            firstLine.setTextColor(ContextCompat.getColor(context, textColor));
            secondLine.setTextColor(ContextCompat.getColor(context, textColor));
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            // Set text
            firstLine.setText(item.name);
            String secLine = item.netSurfaceArea + (item.number != null ? " ("+item.number+")" : "");
            secondLine.setText(secLine);
        }

        @Override
        public void onClick(View v) {

            if (item.referenceName.contains(role))
                item.referenceName.remove(role);
            else
                item.referenceName.add(role);


//            if (item.isSelected) {
//                item.isSelected = false;
//                item.referenceName.remove(role);
//            } else {
//                item.isSelected = true;
//                item.referenceName.add(role);
//            }

            view.setSelected(!view.isSelected());
            notifyItemChanged(pos);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_two_lines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        @ColorRes int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
        holder.display(position, backgroundId);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
