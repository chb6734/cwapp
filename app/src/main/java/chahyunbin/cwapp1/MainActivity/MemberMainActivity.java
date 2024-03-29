package chahyunbin.cwapp1.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import chahyunbin.cwapp1.Bible.BibleActivity;
import chahyunbin.cwapp1.Login.EmailSignInActivity;
import chahyunbin.cwapp1.Login.LoginHomeActivity;
import chahyunbin.cwapp1.MainActivity.Transformer.DepthPageTransformer;
import chahyunbin.cwapp1.MemberCheck;
import chahyunbin.cwapp1.Personal_Info_Fix;
import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.Video.VideoListActivity;
import chahyunbin.cwapp1.model.User;

public class MemberMainActivity extends Activity implements AdFirebaseLoad, ValueEventListener {

    Button button1,button2,button3;
    ImageView imageView;
    public static String username = null;
    String phonenumber;
    String emaildata;
    LoginHomeActivity googlelogin;
    EmailSignInActivity signIn;
    // FirebaseUI firebaseUI;
    TextView textView;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static boolean outboolean =false;

    public static String email;
    FirebaseUser user;

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    DatabaseReference adref;
    AdFirebaseLoad adFirebaseLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_member);


        user = FirebaseAuth.getInstance().getCurrentUser();
        textView = (TextView)findViewById(R.id.loginName);


        //get email
        emaildata = user.getEmail();
        if (emaildata != null) {
            email = emaildata.substring(0, emaildata.indexOf("@"));

        }

        if(email == null){
            Intent intent = new Intent(MemberMainActivity.this, LoginHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);

        }
        // find username in firebase UserInfo
        //if username is null go back to personal_Info.class

        GetUserName();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberMainActivity.this, Personal_Info_Fix.class);
                startActivity(intent);
            }
        });


        //ad load

        adref = FirebaseDatabase.getInstance().getReference("AD");
        adFirebaseLoad = this;


        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        loadAd();








        //textView.setText(firebaseUI.userName);

        findViewById(R.id.memberlogoutbtn).setOnClickListener(myClick);
        findViewById(R.id.memberbutton1).setOnClickListener(myClick);
        findViewById(R.id.memberbutton2).setOnClickListener(myClick);
        findViewById(R.id.memberbutton3).setOnClickListener(myClick);

    }
    private void loadAd() {


        adref.addValueEventListener(new ValueEventListener() {
            List<AD> adList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    adList.add(snapshot.getValue(AD.class));


                }
                adFirebaseLoad.onFirebaseLoadSuccess(adList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adFirebaseLoad.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }


    private void GetUserName() {
        Query query =  FirebaseDatabase.getInstance().getReference("User/"+email).orderByChild("UserInfo");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    username = user.getName();
                    Log.d("Firebase", "username1 : "+username);
                    textView.setText(username+"님");

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    Button.OnClickListener myClick = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.memberbutton1:
                    startActivity(new Intent(MemberMainActivity.this,MemberCheck.class));

                    break;
                case R.id.memberbutton2:
                    startActivity(new Intent(MemberMainActivity.this, BibleActivity.class));
                    break;

                case R.id.memberbutton3:
                    startActivity(new Intent(MemberMainActivity.this, VideoListActivity.class));
                    break;

                case R.id.memberlogoutbtn:
                    googlelogin.signOut();
                    email = null;
                    //AuthUI.getInstance().signOut(getApplicationContext());
                    finish();
                    startActivity(new Intent(MemberMainActivity.this, LoginHomeActivity.class));

            }
        }
    };


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            outboolean = true;
        }


    }
    @Override
    public void onFirebaseLoadSuccess(List<AD> mAdList) {
        pagerAdapter = new PagerAdapter(MemberMainActivity.this, mAdList);
        viewPager.setAdapter(pagerAdapter);


    }

    @Override
    public void onFirebaseLoadFailed(String massage) {
        Toast.makeText(this, "" + massage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<AD> adList = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            adList.add(snapshot.getValue(AD.class));
            adFirebaseLoad.onFirebaseLoadSuccess(adList);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        adFirebaseLoad.onFirebaseLoadFailed(databaseError.getMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        adref.addValueEventListener(this);
    }

    @Override
    protected void onDestroy() {
        adref.removeEventListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        adref.removeEventListener(this);
        super.onStop();
    }
}



