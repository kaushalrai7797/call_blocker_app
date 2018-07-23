package com.dslab.project.callblockerlite;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        101);
            }
        }

        final EditText editText = (EditText) findViewById(R.id.number);
        Button block = (Button) findViewById(R.id.block);
        Button blocked = (Button) findViewById(R.id.blocked);
        Button removeAll = (Button) findViewById(R.id.removeAll);

        DatabaseHelperObject.databaseHelper = new DatabaseHelper(this);

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelperObject.databaseHelper.clearDatabase();
                Toast.makeText(MainActivity.this,"List Cleared !",Toast.LENGTH_SHORT).show();
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                editText.setText("");
                boolean res = DatabaseHelperObject.databaseHelper.insert(number);
                if(res){
                    Toast.makeText(MainActivity.this,"Number Added to Block List",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Couldn't add it !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        blocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = DatabaseHelperObject.databaseHelper.getData();
                if(cursor.getCount() == 0){
                    Toast.makeText(MainActivity.this,"Empty List",Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n");
                }
                showDialog(buffer);
            }
        });
    }

    private void showDialog(StringBuffer buffer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("List of Blocked Contacts ");
        builder.setMessage(buffer);
        builder.show();
    }

}
