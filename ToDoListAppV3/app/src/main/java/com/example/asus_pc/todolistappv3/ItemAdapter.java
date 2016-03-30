package com.example.asus_pc.todolistappv3;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Asus-PC on 24/03/2016.
 */

/**
 * A class defining an adapter for the todolist
 */
public class ItemAdapter extends ArrayAdapter<TodoListItem> {

    public ItemAdapter(Context c, ArrayList<TodoListItem> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoListItemView itemView = (TodoListItemView)convertView;
        if (null == itemView)
            itemView = TodoListItemView.inflate(parent);
        itemView.setItem(getItem(position));
        try { // if date is due (or today) paint list item in red
            checkItemDate(itemView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return itemView;
    }

    /**
     * This function compare the given date with today date and color it accordingly
     * @param itemView itemView
     * @throws ParseException ParseException
     */
    private void checkItemDate(TodoListItemView itemView) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        TextView itemTitleView = itemView.get_titleView();
        TextView itemDateView = itemView.get_dueDateView();

        String itemDateString = itemDateView.getText().toString();
        Date userDate = sdf.parse(itemDateString);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        // comparing the given date with today
        int compareValue = todayWithZeroTime.compareTo(userDate);
        // if given date is passed we color list item in red
        if (compareValue > 0)
        {
            itemTitleView.setTextColor(Color.RED);
            itemDateView.setTextColor(Color.RED);
        }
        else {
            itemTitleView.setTextColor(Color.BLACK);
            itemDateView.setTextColor(Color.BLACK);
        }
    }
}
