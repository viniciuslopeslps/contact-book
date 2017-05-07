package com.example.vinicius.contactbook;

import android.widget.EditText;
import android.widget.RatingBar;

import com.example.vinicius.contactbook.model.Student;

public class FormHelper {

    private EditText nameText;
    private EditText addressText;
    private EditText phoneText;
    private EditText siteText;
    private RatingBar rate;
    private Student student;

    public FormHelper(FormActivity formActivity) {
        nameText = (EditText) formActivity.findViewById(R.id.form_name);
        addressText = (EditText) formActivity.findViewById(R.id.form_address);
        phoneText = (EditText) formActivity.findViewById(R.id.form_phone);
        siteText = (EditText) formActivity.findViewById(R.id.form_site);
        rate = (RatingBar) formActivity.findViewById(R.id.form_rate);
        student = new Student();
    }

    public Student getStudent() {
        student.setName(nameText.getText().toString());
        student.setAddress(addressText.getText().toString());
        student.setSite(siteText.getText().toString());
        student.setPhone(phoneText.getText().toString());
        student.setRate(Double.valueOf(rate.getProgress()));
        return student;
    }

    public void buildForm(Student student) {
        nameText.setText(student.getName());
        addressText.setText(student.getAddress());
        phoneText.setText(student.getPhone());
        siteText.setText(student.getSite());
        rate.setProgress(student.getRate().intValue());
        this.student = student;
    }
}
