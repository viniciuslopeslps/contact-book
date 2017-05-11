package com.example.vinicius.contactbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.vinicius.contactbook.dao.StudentDAO;
import com.example.vinicius.contactbook.model.Student;

import java.io.File;
import java.util.Date;

public class FormActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 567;
    private FormHelper formHelper;
    private String pathPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        this.formHelper = new FormHelper(this);
        //recuperando valor de outra activity pelo nome "student" (declarado em ContactListActivity)
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("student");

        if (student != null) {
            formHelper.buildForm(student);
        }

        Button photoButton = (Button) findViewById(R.id.form_photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pega uma foto do usuário
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathPhoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File photoFile = new File(pathPhoto);
                //para caminho de media sempre usar essa chave
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                //depois da activity vai ter um retorno
                startActivityForResult(intent, CAMERA_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //abre a foto
                formHelper.loadPhoto(pathPhoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //passa qual o menu que você quer "inflar" e qual objeto
        // obs: quem cria o menu é o android
        inflater.inflate(R.menu.menu_contact_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //quando o icone no menu for clicado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.form_menu_ok) {
            Student student = formHelper.getStudent();

            StudentDAO studentDAO = new StudentDAO(this);
            if (student.getId() == null) {
                studentDAO.insert(student);
            } else {
                studentDAO.update(student);
            }
            studentDAO.close();

            String message = "Aluno: " + student.getName() + " Salvo com sucesso!";
            Toast.makeText(FormActivity.this, message, Toast.LENGTH_SHORT).show();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
