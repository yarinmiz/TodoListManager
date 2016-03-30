package com.example.asus_pc.todolistappv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asus-PC on 26/03/2016.
 */

/**
 * A class manages a DB of the to do list items
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // todoList table name
    private static final String TABLE_TODO_LIST = "TodoList";

    // todoList Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE = "due";
    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE, KEY_DUE};

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "todo_db";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create items table
        String CREATE_TODO_LIST_TABLE = "CREATE TABLE TodoList ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "due LONG )";

        // create todolist  table
        db.execSQL(CREATE_TODO_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older todolist table if existed
        db.execSQL("DROP TABLE IF EXISTS TodoList");

        // create fresh todolist table
        this.onCreate(db);
    }

    /**
     * Add item to DB
     * @param listItem
     */
    public void addTodoItem(TodoListItem listItem){
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, listItem.get_title()); // get title
        values.put(KEY_DUE, listItem.get_dueDate()); // get due
        // insert
        long id = db.insert(TABLE_TODO_LIST, null, values);
        listItem.set_id(id);
        // close
        db.close();
    }

    /**
     * Delete item from DB
     * @param listItem
     */
    public void deleteTodoItem(TodoListItem listItem) {

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // delete
        db.delete(TABLE_TODO_LIST, KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(listItem.get_id())}); //selections args
        // close
        db.close();
    }

//    /**
//     * Prints all items in DB
//     * @return
//     */
//    public List<TodoListItem> getAllItems() {
//        List<TodoListItem> items = new LinkedList<TodoListItem>();
//
//        // 1. build the query
//        String query = "SELECT  * FROM " + TABLE_TODO_LIST;
//
//        // 2. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        // 3. go over each row, build item and add it to list
//        TodoListItem item = null;
//        if (cursor.moveToFirst()) {
//            do {
//                item = new TodoListItem(cursor.getString(1), cursor.getString(2));
//                item.set_id(Integer.parseInt(cursor.getString(0)));
//                // Add item to items
//                items.add(item);
//            } while (cursor.moveToNext());
//        }
//
//        // print all db
//        Log.d("getAllItems()", items.toString());
//
//        // return items
//        return items;
//    }
}
