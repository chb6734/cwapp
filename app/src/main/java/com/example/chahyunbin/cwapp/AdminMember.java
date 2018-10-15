package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.R;

import java.util.ArrayList;

public class AdminMember extends Activity {
    ListView listView;
    SingleAdapter adapter;
    MemberDatabase db;
    SQLiteDatabase sqlDb;
    OnDataCallBack callback;
    Button button;

    Activity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminmember);
        db = new MemberDatabase(this);

        activity = this;
        listView = findViewById(R.id.listView);

        adapter = new SingleAdapter();
        //(adpater에 내용 추가)
        ArrayList<MemberItem> result = selectAll();
        adapter.setItems(result);
        adapter.notifyDataSetChanged();

        //
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MemberItem item = (MemberItem) adapter.getItem(i);
                Toast.makeText(AdminMember.this, "선택" + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    class SingleAdapter extends BaseAdapter {
        ArrayList<MemberItem> items = new ArrayList<MemberItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void addItem(MemberItem item) {
            items.add(item);
        }
        public void setItems(ArrayList<MemberItem> items) {
            this.items = items;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            MemberItemView view = new MemberItemView(getApplicationContext());
            MemberItem item = items.get(position);
            view.setName(item.getName());
            view.setAge(item.getAge());
            view.setBirth(item.getBirth());
            view.setPhone(item.getPhone());

            return view;
        }
    }




    public ArrayList<MemberItem> selectAll() {
        ArrayList<MemberItem> result = db.selectAll();

        return result;
    }
}

