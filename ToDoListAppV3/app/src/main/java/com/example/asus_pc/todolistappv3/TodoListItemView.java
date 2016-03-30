package com.example.asus_pc.todolistappv3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Asus-PC on 24/03/2016.
 */

/**
 * Class that assembles the view contains in a todolist item
 */
public class TodoListItemView extends RelativeLayout {
    private TextView _titleView;
    private TextView _dueDateView;

    public void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
        // set the views:
        _titleView = (TextView) findViewById(R.id.title);
        _dueDateView = (TextView) findViewById(R.id.date);
    }

    public TodoListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    public TodoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TodoListItemView(Context context) {
        super(context);
        init(context);
    }

    public TextView get_titleView() {
        return _titleView;
    }

    public void set_titleView(TextView _titleView) {
        this._titleView = _titleView;
    }

    public TextView get_dueDateView() {
        return _dueDateView;
    }

    public void set_dueDateView(TextView _dueDateView) {
        this._dueDateView = _dueDateView;
    }

    /**
     * An inflater for item view. Used in getView of todolist class
     * @param parent
     * @return
     */
    public static TodoListItemView inflate(ViewGroup parent) {
        TodoListItemView itemView = (TodoListItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return itemView;
    }

    /**
     * Update item view in accord to a todolist item data
     * @param item
     */
    public void setItem(TodoListItem item) {
        _titleView.setText(item.get_title());
        _dueDateView.setText(item.get_dueDate());
    }
}