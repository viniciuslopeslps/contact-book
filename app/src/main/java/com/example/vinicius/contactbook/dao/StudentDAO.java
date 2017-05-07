package com.example.vinicius.contactbook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.vinicius.contactbook.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends SQLiteOpenHelper {

    public StudentDAO(Context context) {
        // ContactBook - nome do banco

        super(context, "ContactBook", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Students (id INTEGER PRIMARY KEY, name TEXT NOT NULL, address TEXT, phone TEXT, site TEXT, rate REAL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Students";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Student student) {
        //retorna uma referencia do banco de dados
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = getStudentData(student);

        db.insert("Students", null, values);
    }

    @NonNull
    private ContentValues getStudentData(Student student) {
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("address", student.getAddress());
        values.put("phone", student.getPhone());
        values.put("site", student.getSite());
        values.put("rate", student.getRate());
        return values;
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM STUDENTS";
        SQLiteDatabase db = getReadableDatabase();

        List<Student> students = new ArrayList<>();
        //1- sql da busca
        //2- parametros usados no sql
        Cursor cursor = db.rawQuery(sql, null);

        //é preciso usar o moveToNext pelo menos uma vez..
        // e ele retorna se é a ultima linha
        while (cursor.moveToNext()) {
            Student student = new Student();
            student.setId(cursor.getLong(cursor.getColumnIndex("id")));
            student.setName(cursor.getString(cursor.getColumnIndex("name")));
            student.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            student.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            student.setSite(cursor.getString(cursor.getColumnIndex("site")));
            student.setRate(cursor.getDouble(cursor.getColumnIndex("rate")));
            students.add(student);
        }
        cursor.close();
        return students;
    }

    public void delete(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {student.getId().toString()};
        db.delete("STUDENTS", "ID = ?", params);
    }

    public void update(Student student) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = getStudentData(student);
        String[] params = {student.getId().toString()};

        db.update("STUDENTS", values, "ID = ?", params);
    }
}
