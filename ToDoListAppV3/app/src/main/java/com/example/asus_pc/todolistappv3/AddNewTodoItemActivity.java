package com.example.asus_pc.todolistappv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


/**
 * Created by Asus-PC on 12/03/2016.
 * Secondary activity uses for getting data from user to add to main list view.
 * Starts on clicking add item button in the main menu.
 */
public class AddNewTodoItemActivity extends Activity implements
        View.OnClickListener {

    //    private GoogleApiClient client;
    private TextView toAdd;
    private DatePicker datePicker;
    private Button ok, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setTitle("Add New Item");
        setContentView(R.layout.activity_add_new_to_do_item);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        toAdd = (TextView) findViewById(R.id.edtNewItem);
        ok = (Button) findViewById(R.id.btn_ok);
        cancel = (Button) findViewById(R.id.btn_cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = this.getIntent();
        switch (v.getId()) {
            case R.id.btn_ok:
                // taking the date string:
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                String dateString = day + "-" + month + "-" + year;

                // taking the item string:
                String itemString = toAdd.getText().toString();

                // update intent
                intent.putExtra("ITEM", itemString);
                intent.putExtra("DATE", dateString);
                this.setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_cancel:
                this.setResult(RESULT_CANCELED, intent);
                finish();
                break;
            default:
                break;
        }
    }
}