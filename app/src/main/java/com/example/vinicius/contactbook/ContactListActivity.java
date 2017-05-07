package com.example.vinicius.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinicius.contactbook.dao.StudentDAO;
import com.example.vinicius.contactbook.model.Student;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private ListView studentList;

    //todo:parei 3:10
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //R de res (Resources)
        setContentView(R.layout.activity_contact_list);

        //student list está dentro de activity_contact_list e é um id de uma lista
        studentList = (ListView) findViewById(R.id.studentList);


        //1- lista inteira
        //2- item clicado da lista
        //3- posicao do item
        //4- id do item
        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> fullList, View item, int position, long id) {
                Student student = (Student) studentList.getItemAtPosition(position);
                Intent goToForm = new Intent(ContactListActivity.this, FormActivity.class);
                //manda um dado de uma activity para outra
                goToForm.putExtra("student", student);
                startActivity(goToForm);
            }
        });


        //mudando de tela
        //1- Pega o botao
        Button newStudent = (Button) findViewById(R.id.form_newStudent);
        // 2- Cria um evento que escuta o click do botao
        newStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3-  Vc coloca qual a sua intenção, ou seja, nossa intenção é mudar de activity(View)
                //vc diz qual activity voce está e qual vc quer que o android estancie pra voce
                Intent goToForm = new Intent(ContactListActivity.this, FormActivity.class);
                startActivity(goToForm);
            }
        });

        //menu de contexto é são opções quando uma pessoa clica em um item na lista
        registerForContextMenu(studentList);
    }

    private void loadStudents() {
        StudentDAO studentDAO = new StudentDAO(this);

        List<Student> students = studentDAO.findAll();

        //ArrayAdapter converte dados da classe em views, ou seja, torna isso disponivel na view
        //primeiro argumento = a classe de referencia, nesse caso = ContactListActivity
        //segundo = tipo da view para exibir o dado (se vc tiver o seu passar a referencia do seu)
        //terceiro = o valor que vc quer colocar lá
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, students);
        studentList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    //menu de contexto é um menu de opcao quando o usuario clicar na lista
    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        final StudentDAO studentDAO = new StudentDAO(this);
        super.onCreateContextMenu(menu, v, menuInfo);
        //criando os itens do menu
        MenuItem deleteItem = menu.add("Deletar");

        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Student student = (Student) studentList.getItemAtPosition(info.position);
                studentDAO.delete(student);
                studentDAO.close();
                String message = "Aluno: " + student.getName() + "Deletado com sucesso!";
                Toast.makeText(ContactListActivity.this, message, Toast.LENGTH_SHORT);
                loadStudents();
                return false;
            }
        });
    }


}
