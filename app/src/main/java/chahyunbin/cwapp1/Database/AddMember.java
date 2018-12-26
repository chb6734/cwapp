package chahyunbin.cwapp1.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import chahyunbin.cwapp1.AdminMember.SingleAdapter;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;

import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;


public class AddMember extends Activity {


    String name, phonenumber;
    EditText nameInput, phonenumberInput, ageInput, birthMonthInput, birthDayInput;
    Button backbutton;
    int age,month,day;
    String agei, monthi,dayi;
    public TextView textView;
    PeopleTable peopleTable;
    private SingleAdapter adapter;


    final String TAG = "BookDatabase";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);

        Button btnSaveInfo;



        btnSaveInfo = (Button)findViewById(R.id.btnSaveInfo);
        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        birthMonthInput = (EditText)findViewById(R.id.birthMonthInput);
        birthDayInput = (EditText)findViewById(R.id.birthDayInput);

        backbutton = (Button)findViewById(R.id.btnBackInAdd);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        peopleTable = PeopleTable.instance(getApplicationContext());




    btnSaveInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {





            name = nameInput.getText().toString().trim();
            phonenumber = phonenumberInput.getText().toString().trim();

            agei = ageInput.getText().toString().trim();
            if(!"".equals(agei)) age = Integer.parseInt(agei);
            else age = 0;

            monthi = birthMonthInput.getText().toString().trim();
            if(!"".equals(monthi))  month = Integer.parseInt(monthi);
            else month = 0;

            dayi = birthDayInput.getText().toString().trim();
            if(!"".equals(dayi)) day = Integer.parseInt(dayi);
            else day = 0;

            if(name.isEmpty()){
                nameInput.setError("Name is required");
                nameInput.requestFocus();
            }
            if(phonenumber.isEmpty()){
                phonenumberInput.setError("Phonenumber is required");
                phonenumberInput.requestFocus();
            }
            if(agei.isEmpty()){
                ageInput.setError("Name is required");
                ageInput.requestFocus();
            }
            if(monthi.isEmpty()){
                birthMonthInput.setError("Phonenumber is required");
                birthMonthInput.requestFocus();
            }
            if(dayi.isEmpty()){
                birthDayInput.setError("Phonenumber is required");
                birthDayInput.requestFocus();
            }
            if(name != null && phonenumber != null && age !=0 && month !=0 && day != 0){


                showDialog(name, phonenumber, age, month, day);
            }




        }
    });

    }

  public void showDialog(final String name, final String phonenumber, final int age, final int month, final int day)

    {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("저장");
        builder.setMessage("저장하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "1");
               insertToDB();
                Toast.makeText(AddMember.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

            }

        });
        //  builder.setPositiveButton();

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }



   private void insertToDB() {


        int newId = peopleTable.insert(name, phonenumber, agei, monthi, dayi);


        Person bean = new Person(newId + "", name, phonenumber, agei, monthi, dayi);


        SingleAdapter.insert(bean);


        nameInput.setText("");

    }



}
