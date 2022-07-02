package com.level_sense.app.roomDB;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.level_sense.app.R;
import java.util.List;

public class GraphDataAdapter extends RecyclerView.Adapter<GraphDataAdapter.GraphHolder> {
    Context context;
    private List<Note> arrayList;

    public GraphDataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public GraphHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new GraphHolder(view);
    }

    @Override
    public void onBindViewHolder(GraphHolder holder, int position) {
        if (arrayList != null) {
            Note note = arrayList.get(position);
            holder.txvNote.setText(note.getName());
        }

    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        else return 0;
    }

    public void setNotes(List<Note> notes) {
        arrayList = notes;
        notifyDataSetChanged();
    }

    public class GraphHolder extends RecyclerView.ViewHolder {
        TextView txvNote;
        public GraphHolder(View itemView) {
            super(itemView);
            txvNote=(TextView)itemView.findViewById(R.id.txvNote);

        }
    }
}
