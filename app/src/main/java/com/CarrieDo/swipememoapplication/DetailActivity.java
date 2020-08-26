package com.CarrieDo.swipememoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        Toast.makeText(getApplicationContext(),"체크 버튼: 저장, 엑스 버튼: 화면 나가기",Toast.LENGTH_LONG).show();

        //DBHelper 객체 생성
        final String dbName = "Memo0.db";
        int dbVersion =1;
        final DBHelper dbHelper;
        dbHelper = new DBHelper(this,dbName,null,dbVersion);

        final EditText title = findViewById(R.id.detail_et_title);
        final EditText contents = findViewById(R.id.detail_et_contents);
        final ImageButton save_btn = findViewById(R.id.detail_ib_save);
        final ImageButton cancel_btn = findViewById(R.id.detail_ib_cancel);
        final ImageButton back_btn = findViewById(R.id.detail_ib_back);

        // [Done] save_btn 을 누르면 데이터 베이스에 들어간다.
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.scale_anim);
                save_btn.startAnimation(anim);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        String titleStr = title.getText().toString();
                        titleStr = titleStr.replace("'","''");


                        String contentsStr =contents.getText().toString();
                        contentsStr = contentsStr.replace("'","''");

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        String sql0 ="SELECT * FROM Memo0 WHERE title = '"+ titleStr +"';";
                        Cursor cs = db.rawQuery(sql0,null);

                        String get_title,get_contents;

                        if (cs != null && cs.moveToFirst())
                        {
                            // 해당 제목이 있다면
                            get_title = cs.getString(0);
                            get_contents = cs.getString(1);
                            Toast.makeText(getApplicationContext(),"제목이 중복됩니다! 저장 불가!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String sql = String.format("INSERT INTO Memo0 VALUES('%s','%s');", titleStr, contentsStr);
                            db.execSQL(sql);
                            // 저장 된다면 toast 메세지 띄우기
                            Toast.makeText(getApplicationContext(),"저장 완료!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1000);
            }
        });

        // [Done]  cancel_btn 을 누르면 이 액티비티 끝내고 main 화면 돌아간다.
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
                        finish();
                    }
                }, 1000);
            }
        });

        // [Done] back_btn 을 누르면 이 액티비티 끝내고 main 화면 돌아간다.
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
                        finish();
                    }
                }, 1000);
            }
        });

    }
}
