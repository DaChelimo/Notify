package com.ramadan.notify.ui.activity;//package com.ramadan.notify.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.naz013.colorslider.ColorSlider;
import com.ramadan.notify.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class new_written_note extends AppCompatActivity {
//    static int i1 = -13553359;
//    TextView noteDate;
//    EditText noteName, noteData;
//    String key;
//    RelativeLayout layout;
//    ColorSlider noteColor, fontColor;
//    ProgressBar progressBar;
//    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
//    Date todayDate = new Date();
//
//
//    @SuppressLint("SimpleDateFormat")
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        layout = findViewById(R.id.R1);
//        noteDate = findViewById(R.id.noteDate);
//        noteName = findViewById(R.id.noteName);
//        noteData = findViewById(R.id.noteData);
//        noteColor = findViewById(R.id.noteColor);
//        fontColor = findViewById(R.id.fontColor);
//        noteColorBt = findViewById(R.id.noteColorBt);
//        fontColorBt = findViewById(R.id.fontColorBt);
//        deleteNote = findViewById(R.id.deleteNote);
//        saveNote = findViewById(R.id.saveNote);
//        editNote = findViewById(R.id.editNote);
//        progressBar = findViewById(R.id.progressBar);
//
//        noteColor.setListener(new ColorSlider.OnColorSelectedListener() {
//            @Override
//            public void onColorChanged(int position, int color) {
//                layout.setBackgroundColor(color);
//                i1 = color;
//
//            }
//        });
//        fontColor.setListener(new ColorSlider.OnColorSelectedListener() {
//            @Override
//            public void onColorChanged(int position, int color) {
//                noteData.setTextColor(color);
//                noteName.setTextColor(color);
//                noteDate.setTextColor(color);
//            }
//        });
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            key = bundle.getString("noteID");
//        }
//        if (key != null) {
//            progressBar.setVisibility(View.VISIBLE);
//            noteDate.setVisibility(View.GONE);
//            noteName.setVisibility(View.GONE);
//            noteData.setVisibility(View.GONE);
//            editNote.setVisibility(View.GONE);
//            collectionReference.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        if (doc != null) {
//                            noteName.setText(doc.getString("noteName"));
//                            noteData.setText(doc.getString("noteData"));
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                layout.setBackgroundColor(Math.toIntExact(doc.getLong("noteColor")));
//                                i1 = Math.toIntExact(doc.getLong("noteColor"));
//                                noteDate.setTextColor(Math.toIntExact(doc.getLong("fontColor")));
//                                noteName.setTextColor(Math.toIntExact(doc.getLong("fontColor")));
//                                noteData.setTextColor(Math.toIntExact(doc.getLong("fontColor")));
//
//                            }
//                            progressBar.setVisibility(View.GONE);
//                            noteDate.setVisibility(View.VISIBLE);
//                            noteName.setVisibility(View.VISIBLE);
//                            noteData.setVisibility(View.VISIBLE);
//                            editNote.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            });
//        }
//        noteDate.setText(currentDate.format(todayDate));
//        deleteNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (key != null) {
//                    collectionReference.document(key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Intent intent = new Intent(new_written_note.this, MainActivity.class);
//                            new_written_note.this.startActivity(intent);
//                            finish();
//                            Animatoo.animateZoom(new_written_note.this);
//                            Toast.makeText(new_written_note.this, "Deleted", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                Intent intent = new Intent(new_written_note.this, MainActivity.class);
//                new_written_note.this.startActivity(intent);
//                finish();
//                Animatoo.animateZoom(new_written_note.this);
//            }
//        });
//        noteColorBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fontColor.setVisibility(View.GONE);
//                noteColor.setVisibility(View.VISIBLE);
//            }
//        });
//        fontColorBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fontColor.setVisibility(View.VISIBLE);
//                noteColor.setVisibility(View.GONE);
//            }
//        });
//        saveNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (key != null) {
//                    updateNote();
//                } else if (!noteData.getEditableText().toString().isEmpty() && key == null) {
//                    saveNote();
//                }
//                Intent intent = new Intent(new_written_note.this, MainActivity.class);
//                new_written_note.this.startActivity(intent);
//                finish();
//                Animatoo.animateZoom(new_written_note.this);
//            }
//        });
//
//        editNote.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
//            @Override
//            public void onMenuExpanded() {
//                noteColorBt.setTitle("Note color");
//                fontColorBt.setTitle("Font color");
//            }
//
//            @Override
//            public void onMenuCollapsed() {
//                fontColor.setVisibility(View.GONE);
//                noteColor.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (key != null) {
//            updateNote();
//        } else if (!noteData.getEditableText().toString().isEmpty() && key == null) {
//            saveNote();
//        }
//        super.onBackPressed();
//    }
//
//    private void saveNote() {
//        String s1 = String.valueOf(System.currentTimeMillis());
//        String s2 = noteDate.getText().toString();
//        String s3 = noteName.getEditableText().toString();
//        String s4 = noteData.getText().toString();
//        int i2 = noteData.getCurrentTextColor();
//        _writtenNote = new writtenNote(s1, s2, s3, s4, i1, i2);
//        collectionReference.document(s1).set(_writtenNote, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(new_written_note.this, "Saved", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    private void updateNote() {
//        String s2 = noteDate.getText().toString();
//        String s3 = noteName.getEditableText().toString();
//        String s4 = noteData.getText().toString();
//        int i2 = noteData.getCurrentTextColor();
//        _writtenNote = new writtenNote(key, s2, s3, s4, i1, i2);
//        collectionReference.document(key).update(
//                "noteID", key,
//                "noteDate", s2,
//                "noteName", s3,
//                "noteData", s4,
//                "noteColor", i1,
//                "fontColor", i2
//        ).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(new_written_note.this, "Saved", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private boolean checkNetwork(Context context) {
//        boolean status = false;
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        if (activeNetwork != null) {
//            status = true;
//        }
//        return status;
//    }
}


