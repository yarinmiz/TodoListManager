package com.example.asus_pc.todolistappv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Main activity
 */
public class ToDoListManagerActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<String> arrayList = new ArrayList<>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> toDoListAdapter;
    private static final int ADD_ACTIVITY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_manager);

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
                AlertDialog.Builder itemDialog = new AlertDialog.Builder(ToDoListManagerActivity.this);
                String itemString = getItemSubString(textView, ItemPart.ITEM_CONTENT);
                itemDialog.setTitle(itemString);
                itemString = itemString.toLowerCase();

                // add a dial button if there is a call command
                if (itemString.contains("call")) {
                    // cut all non numeric chars from string
                    final String phoneNumber = itemString.replaceAll("[^\\d]", "");
                    itemDialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                }

                itemDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toDoListAdapter.remove(toDoListAdapter.getItem(position));
                    }
                });
                itemDialog.setNeutralButton("Close", new DialogInterface.OnClickListener() {
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
            try { // if date is due (or today) paint list item in red
                checkItemDate(textView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
                return textView;
            }
        };

        toDoList.setAdapter(toDoListAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // An enum uses to know if we want to parse the date string or the item text string from list view
    private enum ItemPart{ITEM_DATE, ITEM_CONTENT}

    /**
     *
     * @param itemView itemView
     * @param itemPart itemPart
     * @return The item date string or item content string from item view in acoord to ItemPart enum
     */
    private String getItemSubString(TextView itemView, ItemPart itemPart){
        String itemString = itemView.getText().toString();
        String[] splitByTabs = itemString.split("   ");
        String itemDateString = splitByTabs[splitByTabs.length - 1];
        String toReturn = "";
        switch (itemPart) {
            case ITEM_DATE:
                toReturn = itemDateString;
                break;
            case ITEM_CONTENT:
                toReturn = itemString.replace(itemDateString,"");
                break;
            default:
                break;
        }
        return toReturn;
    }
        /**
     * This function compare the given date with today date and color it accordingly
     * @param itemView itemView
     * @throws ParseException ParseException
     */
    private void checkItemDate(TextView itemView) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String itemDateString = getItemSubString(itemView, ItemPart.ITEM_DATE);
        Date userDate = sdf.parse(itemDateString);
        Calendar cal = Calendar.getInstance();
        // comparing the given date with today
        int compareValue = cal.getTime().compareTo(userDate);
        // if given date is passed we color list item in red
        if (compareValue > 0)
        {
            itemView.setTextColor(Color.RED);
        }
        // if given date isn't passed we color list item in black
        else {
            itemView.setTextColor(Color.BLACK);
        }
    }
    /**
     * Add an item to the list view
     */
    private void addToList(String itemDateString, String itemTxtString){

        // check if an item was entered, indeed
        if (itemTxtString.matches("")){
            return;
        }
        String toAdd = itemTxtString + "    " + itemDateString;
        arrayList.add(toAdd);
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
                Intent addActivity = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
                startActivityForResult(addActivity, ADD_ACTIVITY_CODE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_ACTIVITY_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String itemDateString = bundle.getString("DATE");
                String itemTxtString = bundle.getString("ITEM");
                addToList(itemDateString, itemTxtString);
            }
        }
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
                Uri.parse("android-app://com.example.asus_pc.todolistappv2/http/host/path")
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
                Uri.parse("android-app://com.example.asus_pc.todolistappv2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
