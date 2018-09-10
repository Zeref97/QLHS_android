package com.example.tan.qunlhcsinh;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    EditText edtName, edtClass, edtBirth, edtLongitude, edtLatitude;
    RadioGroup group;
    Button btnDone, btnDel;
    TextView txt;
    int index;
    Intent intent;
    RadioButton radioMale;
    RadioButton radioFemale;
    RelativeLayout backgroundEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        backgroundEdt=(RelativeLayout)findViewById(R.id.BackgroundEdt);
        backgroundEdt.setBackgroundResource(R.drawable.backgroundedt);


        btnDone = (Button) findViewById(R.id.btnDone);
        btnDel = (Button) findViewById(R.id.btnDel);
        txt = (TextView) findViewById(R.id.txtContent);
        edtName = (EditText) findViewById(R.id.edtName);
        edtClass = (EditText) findViewById(R.id.edtClass);
        edtBirth = (EditText) findViewById(R.id.edtBirth);
        edtLongitude=(EditText) findViewById(R.id.edtlongitude);
        edtLatitude=(EditText) findViewById(R.id.edtLatitude);

        group = (RadioGroup) findViewById(R.id.radioGroup);
        radioFemale = (RadioButton) findViewById(R.id.radioNu);
        radioMale = (RadioButton) findViewById(R.id.radioNam);

        intent = getIntent();
        index = intent.getIntExtra("index", 0);
        final HocSinh student = (HocSinh) intent.getSerializableExtra("Student");

        if (index == 0) {
            txt.setText("Thêm học sinh mới");
            btnDone.setText("Thêm");
            btnDel.setText("Hủy bỏ");

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtName.getText().length() == 0 || edtClass.getText().length() == 0 || edtBirth.getText().length() == 0 || group.getCheckedRadioButtonId()==-1||edtLatitude.getText().length()==0||edtLongitude.getText().length()==0)
                        Toast.makeText(getApplicationContext(), "Bạn chưa điền đủ nội dung yêu cầu", Toast.LENGTH_SHORT).show();
                    else if(Long.parseLong(edtBirth.getText().toString())<=1990)
                        Toast.makeText(getApplicationContext(),"Năm sinh không hợp lệ, mời bạn nhập lại",Toast.LENGTH_SHORT).show();
                    else {
                        Intent intentResult = new Intent();
                        HocSinh hs1, hs2;
                        if (radioMale.isChecked()) {
                            hs1 = new HocSinh(0, edtName.getText().toString(), edtClass.getText().toString(), edtBirth.getText().toString(), true,Float.parseFloat(edtLongitude.getText().toString()),Float.parseFloat(edtLatitude.getText().toString()));
                            intentResult.putExtra("HS", hs1);
                        }
                        else if(radioFemale.isChecked()) {
                            hs2 = new HocSinh(0, edtName.getText().toString(), edtClass.getText().toString(), edtBirth.getText().toString(), false,Float.parseFloat(edtLongitude.getText().toString()),Float.parseFloat(edtLatitude.getText().toString()));
                            intentResult.putExtra("HS", hs2);
                        }
                        setResult(1, intentResult);
                        finish();
                    }
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else if(index==1){
            txt.setText("Chỉnh sửa học sinh");
            btnDone.setText("Xong");
            btnDel.setText("Xóa");

            edtName.setText(student.getHoTen().toString());
            edtClass.setText(student.getLop().toString());
            edtBirth.setText(student.getNamSinh().toString());
            edtLongitude.setText(String.valueOf(student.getLongitude()));
            edtLatitude.setText(String.valueOf(student.getLatitude()));

            if(student.isGioiTinh())
                radioMale.setChecked(true);
            else
                radioFemale.setChecked(true);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtName.getText().length() == 0 || edtClass.getText().length() == 0 || edtBirth.getText().length() == 0 || group.getCheckedRadioButtonId()==-1)
                        Toast.makeText(getApplicationContext(), "Bạn chưa điền đủ nội dung yêu cầu", Toast.LENGTH_SHORT).show();
                    else if(Long.parseLong(edtBirth.getText().toString())<=1990)
                        Toast.makeText(getApplicationContext(),"Năm sinh không hợp lệ, mời bạn nhập lại",Toast.LENGTH_SHORT).show();
                    else {
                        Intent intentResult=new Intent();
                        HocSinh hs1, hs2;
                        if (radioMale.isChecked()) {
                            hs1 = new HocSinh(student.getId(),edtName.getText().toString(), edtClass.getText().toString(), edtBirth.getText().toString(), true, Float.parseFloat(edtLongitude.getText().toString()),Float.parseFloat(edtLatitude.getText().toString()));
                            intentResult.putExtra("HSM", hs1);
                        }
                        else if(radioFemale.isChecked()) {
                            hs2 = new HocSinh(student.getId(), edtName.getText().toString(), edtClass.getText().toString(), edtBirth.getText().toString(), false,Float.parseFloat(edtLongitude.getText().toString()),Float.parseFloat(edtLatitude.getText().toString()));
                            intentResult.putExtra("HSM", hs2);
                        }
                        setResult(2, intentResult);
                        finish();
                    }
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditActivity.this);
                    builder1.setMessage("Bạn có muốn xóa học sinh "+student.getHoTen().toString()+" hay không?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    HocSinh hs=new HocSinh(student.getId(),student.getHoTen(),student.getLop(),student.getNamSinh(),student.isGioiTinh(),student.getLongitude(),student.getLatitude());
                                    Intent intentResult=new Intent();
                                    intentResult.putExtra("HSM",hs);
                                    setResult(3, intentResult);
                                    finish();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
        }
    }
}

