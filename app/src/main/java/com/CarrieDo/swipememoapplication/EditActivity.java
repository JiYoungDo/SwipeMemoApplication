package com.CarrieDo.swipememoapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        //DBHelper 객체 생성
        String dbName = "Memo0.db";
        int dbVersion =1;
        final DBHelper dbHelper;
        dbHelper = new DBHelper(this,dbName,null,dbVersion);

        Intent intent = getIntent();
        final String got_title = intent.getStringExtra("title");
        String got_contents = intent.getStringExtra("contents");

        final EditText title = findViewById(R.id.detail_et_title);
        final EditText contents = findViewById(R.id.detail_et_contents);
        final ImageButton save_btn = findViewById(R.id.detail_ib_save);
        final ImageButton cancel_btn = findViewById(R.id.detail_ib_cancel);
        final ImageButton back_btn = findViewById(R.id.detail_ib_back);

        title.setText(got_title);
        contents.setText(got_contents);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이 데이터 값을 바꿔줘야 한다.
                // 받아온 title 로  조회해서 바꾼다.
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.scale_anim);
                save_btn.startAnimation(anim);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String final_title = title.getText().toString();
                        final_title = final_title.replace("'","''");
                        String final_contents = contents.getText().toString();
                        final_contents = final_contents.replace("'","''");
                        String sql = "UPDATE Memo0 SET title='"+final_title+"',contents='"+final_contents+"'WHERE title='"+got_title+"';";
                        db.execSQL(sql);

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

                        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setMessage("메모가 잘 수정 되었습니다 :)");
                        builder.setTitle("알림창");
                        builder.show();
                    }
                }, 1000);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.scale_anim);
                cancel_btn.startAnimation(anim);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        finish();}
                }, 1000);

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.scale_anim);
                back_btn.startAnimation(anim);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        finish();}
                }, 1000);
            }
        });

    }
}
