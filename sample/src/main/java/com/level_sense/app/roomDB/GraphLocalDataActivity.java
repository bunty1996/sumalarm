package com.level_sense.app.roomDB;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.level_sense.app.R;

import java.util.List;
import java.util.UUID;

public class GraphLocalDataActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etNewNote;
    Button bAdd;
    public static final String NOTE_ADDED = "new_note";
    private NoteViewModel noteViewModel;
    private LinearLayout getDataLayout,setDataLayout;
    private RecyclerView recycleView;
    private GraphDataAdapter graphDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_local_data);
        etNewNote= (EditText) findViewById(R.id.etNewNote);
        bAdd=(Button) findViewById(R.id.bAdd);
        getDataLayout=(LinearLayout)findViewById(R.id.getDataLayout);
        setDataLayout=(LinearLayout)findViewById(R.id.setDataLayout);
        recycleView=(RecyclerView)findViewById(R.id.recycleView);

        setDataLayout.setVisibility(View.VISIBLE);
        getDataLayout.setVisibility(View.GONE);

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        graphDataAdapter=new GraphDataAdapter(this);
        recycleView.setAdapter(graphDataAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {

              //  Toast.makeText(getApplicationContext(),notes.toString(),Toast.LENGTH_SHORT).show();
                graphDataAdapter.setNotes(notes);
            }
        });

        //note = noteModel.getNote(noteId);

        bAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAdd:
              /*  Note note = new Note(note_id,name);
                noteViewModel.insert(note);
*/
                getDataLayout.setVisibility(View.VISIBLE);
                setDataLayout.setVisibility(View.GONE);
                String name = etNewNote.getText().toString().trim();
                final String note_id = UUID.randomUUID().toString();

                if (!TextUtils.isEmpty(etNewNote.getText())) {
                    Note note = new Note(note_id,name);
                    noteViewModel.insert(note);
                    Toast.makeText(getApplicationContext(), "saved"+"\t\t"+note.getId()+"\t\t"+note.getName(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "not saved", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }
}
