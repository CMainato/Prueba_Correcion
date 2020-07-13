package com.example.prueba_correccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.prueba_correccion.R.id.ltxtView;
import static com.example.prueba_correccion.R.id.parent;

public class MainActivity extends AppCompatActivity {

    private List<Mascotas> listPerson = new ArrayList<Mascotas>();
    ArrayAdapter<Mascotas> arrayAdapterMascotas;

    EditText nombb, raza;
    ListView listVmascota;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Mascotas mascotasSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombb = findViewById(R.id.txtNombre);
        raza = findViewById(R.id.txtRaza);
        listVmascota = findViewById(R.id.ltxtView);

        listVmascota = findViewById(ltxtView);
        inicializarFirebase();
        listarDatos();

        listVmascota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mascotasSelected = (Mascotas) parent.getItemAtPosition(position);
                nombb.setText(mascotasSelected.getNombre());
                raza.setText(mascotasSelected.getRaza());

            }
        });

    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Mascotas p = objSnaptshot.getValue(Mascotas.class);
                    listPerson.add(p);

                    arrayAdapterMascotas = new ArrayAdapter<Mascotas>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listVmascota.setAdapter(arrayAdapterMascotas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nombb.getText().toString();
        String raza1 = raza.getText().toString();

        switch (item.getItemId()) {
            case R.id.icon_add: {


                Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                if (nombre.equals("") || raza1.equals("") ) {
                    validacion();
                } else {
                    Mascotas p = new Mascotas();

                    p.setId(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setRaza(raza1);

                    databaseReference.child("Persona").child(p.getId()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_brow: {
                Toast.makeText(this, "Buscar", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete: {

                Mascotas p = new Mascotas();
                p.setId(mascotasSelected.getId());
                databaseReference.child("Persona").child(p.getId()).removeValue();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_update: {

                Mascotas p = new Mascotas();
                p.setId(mascotasSelected.getId().toString().trim());
                p.setNombre(mascotasSelected.getNombre().toString().trim());
                p.setRaza(mascotasSelected.getRaza().toString().trim());
                databaseReference.child("Persona").child(p.getId()).setValue(p);
                Toast.makeText(this,"Actualizado",Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:
                break;
        }
        return true;
    }

    private void limpiarCajas() {
        nombb.setText("");
        raza.setText("");

    }

    private void validacion() {
        String nombre = nombb.getText().toString();
        String razaz = raza.getText().toString();

        if (nombre.equals("")) {
            nombb.setError("Required");
        } else if (razaz.equals("")) {
            raza.setError("Required");
        }

    }
}