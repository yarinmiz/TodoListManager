package com.example.asus_pc.todolistappv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class TodoListManagerActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<String> arrayList = new ArrayList<>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> toDoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        ListView toDoList = (ListView) findViewById(R.id.lstTodoItems);
        toDoList.setLongClickable(true);
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             *
             * @param parent parent
             * @param view view
             * @param position position
             * @param id id
             * @return On a long click of any list item , this function creates a dialog with
             *          an option to delete the item and an option to close the dialog
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                TextView textView = (TextView) toDoListAdapter.getView(position, view, parent);
                AlertDialog.Builder itemDialog = new AlertDialog.Builder(TodoListManagerActivity.this);
                itemDialog.setTitle(textView.getText().toString());
                itemDialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toDoListAdapter.remove(toDoListAdapter.getItem(position));
                    }
                });
                itemDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                itemDialog.show();
                return true;
            }
        });

        toDoListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_item, arrayList) {
                    /*
                        This function will be called with an insertion of item into the list.
                        If the item position is even the function sets its color to red, otherwise
                        item color set to be blue.
                     */
                    public View getView (int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        String blueBackground = "#89bffc";
                        // String blueText = "#e0d22e3";
                        String redBackground = "#fc8989";
                        // String redText = "#e30d0d";
                        if (position % 2 == 1) {
                            textView.setBackgroundColor(Color.parseColor(blueBackground));
                            textView.setTextColor(Color.BLUE);
                        }
                        else{
                            textView.setBackgroundColor(Color.parseColor(redBackground));
                            textView.setTextColor(Color.RED);
                        }

                        return textView;
                    }
        };

        toDoList.setAdapter(toDoListAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Add an item to the list view
     */
    private void addToList(){

        EditText toAdd = (EditText) findViewById(R.id.edtNewItem);
        String newItemString = toAdd.getText().toString();
        // check if an item was entered, indeed
        if (newItemString.matches("")){
            return;
        }

        arrayList.add(newItemString);
        toDoListAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param menu menu
     * @return This function creates the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *
     * @param item item
     * @return This function defines what happen when user select the add option of menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                addToList();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TodoListManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.asus_pc.todolistappv1/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TodoListManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.asus_pc.todolistappv1/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
