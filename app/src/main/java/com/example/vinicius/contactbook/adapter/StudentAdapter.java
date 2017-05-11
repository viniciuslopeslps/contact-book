package com.example.vinicius.contactbook.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vinicius.contactbook.ContactListActivity;
import com.example.vinicius.contactbook.model.Student;

import java.util.List;


//responsavel por mapear objeto em uma view, um adaptador objeto xml
//adapter é uma classe generica
public class StudentAdapter extends BaseAdapter {
    private final Context context;
    private List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
        this.students = students;
        this.context = context;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return students.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        Student student = students.get(position);
        textView.setText(student.toString());
        return textView;
    }
}
