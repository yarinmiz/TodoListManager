package com.example.asus_pc.todolistappv3;

/**
 * Created by Asus-PC on 24/03/2016.
 */

/**
 * Class for the todolist item for the usage of the adapter and the DB class
 */
public class TodoListItem {
    private static int counter = 1;
    private long _id;
    private String _title;
    private String _dueDate;

    public TodoListItem(String title, String dueDate){
        _title = title;
        _dueDate = dueDate;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_dueDate(String _dueDate) {
        this._dueDate = _dueDate;
    }

    public String get_dueDate() {
        return _dueDate;

    }
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Item [id=" + _id + ", title=" + _title + ", dueDate=" + _dueDate + "]";
    }
}