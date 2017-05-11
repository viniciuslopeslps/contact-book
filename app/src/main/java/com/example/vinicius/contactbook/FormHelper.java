package com.example.vinicius.contactbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.vinicius.contactbook.model.Student;

public class FormHelper {

    private EditText nameText;
    private EditText addressText;
    private EditText phoneText;
    private EditText siteText;
    private RatingBar rate;
    private ImageView photoView;
    private Student student;

    public FormHelper(FormActivity formActivity) {
        nameText = (EditText) formActivity.findViewById(R.id.form_name);
        addressText = (EditText) formActivity.findViewById(R.id.form_address);
        phoneText = (EditText) formActivity.findViewById(R.id.form_phone);
        siteText = (EditText) formActivity.findViewById(R.id.form_site);
        rate = (RatingBar) formActivity.findViewById(R.id.form_rate);
        photoView = (ImageView) formActivity.findViewById(R.id.formulario_foto);
        student = new Student();
    }

    public Student getStudent() {
        student.setName(nameText.getText().toString());
        student.setAddress(addressText.getText().toString());
        student.setSite(siteText.getText().toString());
        student.setPhone(phoneText.getText().toString());
        student.setRate(Double.valueOf(rate.getProgress()));
        student.setPathPhoto(photoView.getTag().toString());
        return student;
    }

    public void buildForm(Student student) {
        nameText.setText(student.getName());
        addressText.setText(student.getAddress());
        phoneText.setText(student.getPhone());
        siteText.setText(student.getSite());
        rate.setProgress(student.getRate().intValue());
        loadPhoto(student.getPathPhoto());
        this.student = student;
    }

    public void loadPhoto(String pathPhoto) {
        if (pathPhoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathPhoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            photoView.setImageBitmap(bitmapReduzido);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setTag(pathPhoto);
        }
    }
}
