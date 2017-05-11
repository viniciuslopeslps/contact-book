package com.example.vinicius.contactbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.example.vinicius.contactbook.adapter.StudentAdapter;
import com.example.vinicius.contactbook.dao.StudentDAO;
import com.example.vinicius.contactbook.model.Student;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private ListView studentList;

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
        //ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, android.R.layout.list_content, students);
        StudentAdapter adapter = new StudentAdapter(this, students);
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
        //pegando o aluno que foi clicado
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Student student = (Student) studentList.getItemAtPosition(info.position);


        final StudentDAO studentDAO = new StudentDAO(this);
        super.onCreateContextMenu(menu, v, menuInfo);
        //criando os itens do menu
        MenuItem deleteItem = menu.add("Deletar");


        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                studentDAO.delete(student);
                studentDAO.close();
                String message = "Aluno: " + student.getName() + "Deletado com sucesso!";
                Toast.makeText(ContactListActivity.this, message, Toast.LENGTH_SHORT);
                loadStudents();
                return false;
            }
        });


        //inicio de acessar site
        // esse é um outro jeito de associar uma intent com uma opcao de menu de contexto
        // esse jeito é mandando para uma intent implicita, pq nao sabemos qual o browser que o android vai abrir
        MenuItem itemSite = menu.add("Visitar Site");
        Intent gotoBrowser = new Intent(Intent.ACTION_VIEW);

        String site = student.getSite();
        if (!site.startsWith("https://")) {
            site = "https://" + site;
        }
        gotoBrowser.setData(Uri.parse(site));
        itemSite.setIntent(gotoBrowser);
        //fim


        //inicio de envio de sms
        MenuItem itemSms = menu.add("Enviar sms");
        Intent goToSms = new Intent(Intent.ACTION_VIEW);
        //desse jeito o android procura a intent responsavel de acordo com o protocolo passado no set data
        gotoBrowser.setData(Uri.parse("sms:" + student.getPhone()));
        itemSms.setIntent(goToSms);
        //fim

        //inicio de visualizar no mapa
        MenuItem itemMap = menu.add("Ver no mapa");
        Intent goToMap = new Intent(Intent.ACTION_VIEW);
        //com isso o google maps vai procurar a cordenada pelo endereço passado
        //obs: o que muda é o protocolo
        goToMap.setData(Uri.parse("geo:0,0?q=" + student.getAddress()));
        itemMap.setIntent(goToMap);
        //fim


        //inicio de fazer ligação
        MenuItem itemCall = menu.add("Ligar");
        itemCall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //com isso o google maps vai procurar a cordenada pelo endereço passado
                //obs: o que muda é o protocolo
                int hasPermission = ActivityCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.CALL_PHONE);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    //request permission = pede a permissao pro usuario para ligacao, (algumas coisas como quando envolver pagamento e necessario)
                    ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{
                            //request code é udado no onRequestPermissionsResult ()123)
                            Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent goToCall = new Intent(Intent.ACTION_CALL);
                    goToCall.setData(Uri.parse("tel:" + student.getPhone()));

                    startActivity(goToCall);
                }
                return false;
            }
        });
        //fim
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
