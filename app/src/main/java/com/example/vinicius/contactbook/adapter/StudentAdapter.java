package com.example.vinicius.contactbook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinicius.contactbook.ContactListActivity;
import com.example.vinicius.contactbook.R;
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
        Student student = students.get(position);
        //você coloca o layout que voce quer usar (inflar)
        LayoutInflater inflater = LayoutInflater.from(context);

        //view generica de uma lista que o android cria uma estancia
        if (convertView == null) {
            //"inflar" = Pegar o xml e transformar em algo concreto
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        //pegando nome
        TextView name = (TextView) convertView.findViewById(R.id.item_nome);
        name.setText(student.getName());


        TextView phote = (TextView) convertView.findViewById(R.id.item_telefone);
        phote.setText(student.getPhone());


        ImageView photo = (ImageView) convertView.findViewById(R.id.item_foto);
        String pathPhoto = student.getPathPhoto();
        if (pathPhoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathPhoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            photo.setImageBitmap(bitmapReduzido);
            photo.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return convertView;
    }
}
