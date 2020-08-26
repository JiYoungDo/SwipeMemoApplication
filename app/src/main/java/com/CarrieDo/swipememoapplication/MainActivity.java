package com.CarrieDo.swipememoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<Memo> mMemoList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    TextView memoCount;

    // [Done] DBHelper 객체 생성
    String dbName = "Memo0.db";
    int dbVersion = 1;
    DBHelper dbHelper;
    // [Done] db 에서 값 가져와서 Memo 클래스에 담아 mArrayList 에 담기


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMemoList = new ArrayList<>();
        mListView = findViewById(R.id.main_lv_memo_list);

        dbHelper = new DBHelper(this,dbName,null,dbVersion);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM Memo0;";
        Cursor cursor = db.rawQuery(sql,null);
        memoCount = findViewById(R.id.main_tv_count);

        // [Done] 데이터 바뀐거 감지 할 수 있게끔
        // mAdapter.notifyDataSetChanged();
        memoCount.setText(String.valueOf(cursor.getCount()));


        try{
            if(cursor.getCount() >0)
            {
                while(cursor.moveToNext())
                {
                    // DB에 있는 값들의 String 값들을 Memo 클래스 생성자에 담기.
                    Memo m = new Memo(cursor.getString(0),cursor.getString(1));
                    mMemoList.add(m);
                }
            }
        }finally {
            cursor.close();
        }

        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        final ImageView backBtn = findViewById(R.id.main_btn_back);
//        final ImageButton searchBtn = findViewById(R.id.main_btn_search);
        final ImageButton newBtn = findViewById(R.id.main_btn_add_text);

        // [Done] 뒤로가기 버튼
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.scale_anim);
                backBtn.startAnimation(anim);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setMessage("이 앱을 정말 종료 하시겠어요?");
                builder.setTitle("종료 알림창");
                builder.show();
            }
        });

        // [Done] 새로운 메모 추가 버튼
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(
                        getApplicationContext(),
                        R.anim.rotate_anim);
                newBtn.startAnimation(anim);

                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        // TODO
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        startActivity(intent);
                    }
                }, 1000);

            }
        });

//
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SearchDialog dialog = new SearchDialog(this);
//                dialog.setDialogListener(new MyDialogListener() {
//                    @Override
//                    public void onPositiveClicked(String t_title, String c_contents) {
//                        setResult(t_title, c_contents);
//                    }
//
//                    @Override
//                    public void onNegativeClicked() {
//                        Log.d("MyDialogListener","onNegativeClicked");
//                    }
//                });
//                dialog.show();
//            }
//        });




        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //"open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("열기");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                //"delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };

        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch(index)
                {
                    case 0:
                        //open
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        Cursor cursor = (Cursor) dbHelper.getReadableDatabase().query("Memo0",null,null,null,null,null,null);
                        cursor.moveToPosition(position);
                        String t_title = cursor.getString(0);
                        String c_contents = cursor.getString(1);
                        intent.putExtra("title",t_title);
                        intent.putExtra("contents",c_contents);
                        startActivity(intent);
                        break;

                    case 1:
                        //delete
                        final Cursor cursor2 = (Cursor) dbHelper.getReadableDatabase().query("Memo0",null,null,null,null,null,null);
                        cursor2.moveToPosition(position);
                        // String c_contents = cursor.getString(2);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 메모 삭제 작업
                                String t_title2 = cursor2.getString(0);
                                t_title2 = t_title2.replace("'","''");
                                String sql0 =String.format("DELETE FROM Memo0 WHERE title = '%s';", t_title2);
                                db.execSQL(sql0);
                                mMemoList.remove(position);
                                mAdapter.notifyDataSetChanged();
                                memoCount.setText(String.valueOf(mMemoList.size()));
                            }
                        });
                        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setMessage("이 메모를 정말 삭제하시겠어요?");
                        builder.setTitle("삭제 알림창");
                        builder.show();
                        break;
                }
                return false;
            }
        });

        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                //swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {

            }

            @Override
            public void onMenuClose(int position) {

            }
        });

//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),position+"long click", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    class AppAdapter extends BaseAdapter
    {

        @Override
        public int getCount() { return mMemoList.size();}

        @Override
        public Object getItem(int position) { return mMemoList.get(position);}

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_memo,null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder)convertView.getTag();
            Memo item =(Memo) getItem(position);
            holder.tv_title.setText(mMemoList.get(position).getTitle());
            holder.tv_contents.setText(mMemoList.get(position).getContents());


            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(MainActivity.this, "title clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    String t_title = mMemoList.get(position).getTitle();
                    String c_contents = mMemoList.get(position).getContents();
                    intent.putExtra("title",t_title);
                    intent.putExtra("contents",c_contents);
                    startActivity(intent);
                }
            });

            holder.tv_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 메모 삭제 작업
                            String t_title2 = mMemoList.get(position).getTitle();
                            t_title2 = t_title2.replace("'","''");
                            String sql0 =String.format("DELETE FROM Memo0 WHERE title = '%s';", t_title2);
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL(sql0);
                            mMemoList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            memoCount.setText(String.valueOf(mMemoList.size()));
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setMessage("이 메모를 정말 삭제하시겠어요?");
                    builder.setTitle("삭제 알림창");
                    builder.show();
                    return false;
                }
            });
            holder.tv_contents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(MainActivity.this, "contents clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    String t_title = mMemoList.get(position).getTitle();
                    String c_contents = mMemoList.get(position).getContents();
                    intent.putExtra("title",t_title);
                    intent.putExtra("contents",c_contents);
                    startActivity(intent);
                }
            });

            holder.tv_contents.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 메모 삭제 작업
                            String t_title2 = mMemoList.get(position).getTitle();
                            t_title2 = t_title2.replace("'","''");
                            String sql0 =String.format("DELETE FROM Memo0 WHERE title = '%s';", t_title2);
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL(sql0);
                            mMemoList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            memoCount.setText(String.valueOf(mMemoList.size()));
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setMessage("이 메모를 정말 삭제하시겠어요?");
                    builder.setTitle("삭제 알림창");
                    builder.show();
                    return false;
                }
            });
            return convertView;
        }

        class ViewHolder
        {
            TextView tv_title;
            TextView tv_contents;
            public ViewHolder(View view)
            {
                tv_title=(TextView)view.findViewById(R.id.memo_tv_title);
                tv_contents=(TextView)view.findViewById(R.id.memo_tv_contents);

                view.setTag(this);
            }
        }

        public boolean getSwipeEnableByPosition(int position)
        {
            if(position % 2 == 0)
            {
                return false;
            }
            return true;
        }
    }

    private int dp2px(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.action_left)
        {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if(id == R.id.action_right)
        {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setResult(String t_title, String c_contents)
    {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("title",t_title);
        intent.putExtra("contents",c_contents);
        startActivity(intent);
        finish();

    }

}

