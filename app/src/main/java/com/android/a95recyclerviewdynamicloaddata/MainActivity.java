package com.android.a95recyclerviewdynamicloaddata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.a95recyclerviewdynamicloaddata.Adapter.MyAdapter;
import com.android.a95recyclerviewdynamicloaddata.Interface.ILoadMore;
import com.android.a95recyclerviewdynamicloaddata.Model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    List<Item> items = new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Random data
        random10Data();

        //init
        RecyclerView recyclerView = findViewById(R.id.rvData);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(recyclerView,this,items);
        recyclerView.setAdapter(adapter);

        //load more
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (items.size()<=50){ // max size
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());

                            // random more data
                            int index = items.size();
                            int end = index+10;
                            for (int i =index;i<end;i++){
                                String name = UUID.randomUUID().toString();
                                Item item = new Item(name,name.length());
                                items.add(item);
                            }

                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    },2000);
                }else{
                    Toast.makeText(MainActivity.this, "Load data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void random10Data() {
        for (int i=0;i<10;i++){
            String name = UUID.randomUUID().toString();
            Item item = new Item(name,name.length());
            items.add(item);
        }
    }
}