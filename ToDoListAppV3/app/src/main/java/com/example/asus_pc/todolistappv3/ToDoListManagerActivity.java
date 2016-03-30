package com.example.asus_pc.todolistappv3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

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
    private ArrayList<TodoListItem> todoListItems = new ArrayList<>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ItemAdapter toDoListAdapter;
    private static final int ADD_ACTIVITY_CODE = 1;

    private static MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_manager);
        // create db
        db = new MySQLiteHelper(this);
        final ListView toDoList = (ListView) findViewById(R.id.lstTodoItems);
        toDoList.setLongClickable(true);

        /**
         * This function defines the dialog opens on a long click of the listView items
         */
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

                TodoListItemView itemView =
                        (TodoListItemView) toDoListAdapter.getView(position, null, toDoList);
                AlertDialog.Builder itemDialog = new AlertDialog.Builder(ToDoListManagerActivity.this);
                String itemString = itemView.get_titleView().getText().toString();
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
                        // delete item from db
                        db.deleteTodoItem(toDoListAdapter.getItem(position));
                        // delete item from listview
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



        toDoListAdapter = new ItemAdapter(getApplicationContext(), todoListItems);
        toDoList.setAdapter(toDoListAdapter);

        syncTodoListWithDB();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Add an item to the list view
     */
    private void addToList(String itemDateString, String itemTxtString){

        // check if an item was entered, indeed
        if (itemTxtString.matches("")){
            return;
        }
        TodoListItem toAdd = new TodoListItem(itemTxtString, itemDateString);
        // todo maybe its a problem to use the same item for both db and list
        // add item to db
        db.addTodoItem(toAdd);
        // add item to list
        todoListItems.add(toAdd);
        // update adapter
        toDoListAdapter.notifyDataSetChanged();
    }

    /**
     * This function populate the list view with the DB items
     */
    private void syncTodoListWithDB(){
        // Define cursor that runs over the whole DB
        Cursor cursor = db.getReadableDatabase().query("TodoList", new String[]{ "_id", "title","due" },
                null, null, null, null, null);
        // running over DB and updating the list view in accord
        if (cursor.moveToFirst()){
            do{
                TodoListItem listItem = new TodoListItem(cursor.getString(1), cursor.getString(2));
                listItem.set_id(Integer.parseInt(cursor.getString(0)));
                todoListItems.add(listItem);
                toDoListAdapter.notifyDataSetChanged();
            }while (cursor.moveToNext());
        }
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
                Uri.parse("android-app://com.example.asus_pc.todolistappv3/http/host/path")
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
                Uri.parse("android-app://com.example.asus_pc.todolistappv3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
