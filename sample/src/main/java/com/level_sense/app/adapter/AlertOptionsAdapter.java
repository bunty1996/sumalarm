package com.level_sense.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.level_sense.app.R;
import com.level_sense.app.model.AlertOptionModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertOptionsAdapter extends RecyclerView.Adapter<AlertOptionsAdapter.AlertHodler> {
    private Context context;
    private ArrayList<AlertOptionModel> arrayList;
    OnOptionClick click;

    public AlertOptionsAdapter(Context context, ArrayList<AlertOptionModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public AlertHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.option_alert_adapter, parent, false);
        return new AlertHodler(v);
    }

    @Override

    public void onBindViewHolder(AlertHodler holder, int position) {

         holder.alertOPtionAdapterText.setText(arrayList.get(position).getName());

           if(arrayList.get(position).isSelected()) {
                holder.alertOptionAdapterImage.setVisibility(View.VISIBLE);
            }else{
                holder.alertOptionAdapterImage.setVisibility(View.GONE);
            }

    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    class AlertHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.alertOptionAdapterText)
        TextView alertOPtionAdapterText;
        @BindView(R.id.alertOptionAdapterImage)
        ImageView alertOptionAdapterImage;

        public AlertHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    click.onOptionClick(getAdapterPosition());

                }

            });

        }

    }

    public void setOnOptionClick(OnOptionClick click) {
        this.click = click;
    }

    public interface OnOptionClick {

        public void onOptionClick(int pos);

    }

}
