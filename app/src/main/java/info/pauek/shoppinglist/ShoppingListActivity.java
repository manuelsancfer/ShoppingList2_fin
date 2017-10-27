package info.pauek.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<ShoppingItem> itemList;
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    private static final String FILENAME = "shopping_list.txt"; //nombre del fichero que leeremos
    // y devuelve un tipo objeto, el contexto lo coge de la zona interna de la aplicacio
    private static final int MAX_BYTES = 8000;              //Tamaño que sabemos que nunca superará

    private void writeItemlist(){   //para guardar en una lista de texto

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE); //abre fichero

            for (int i = 0; i < itemList.size(); i++){  //para pasar por todos los items
                ShoppingItem it = itemList.get(i);      //el item que cogemos
                String line = String.format("%s;%b\n", it.getText(), it.getCheck());

                fos.write(line.getBytes());             //grabar la línea de un fichero
            }
            fos.close();                                //cerrar el fichero

        } catch (FileNotFoundException e) {
            Log.e("Manu", "writeItemlist: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("Manu", "writeItemList: IOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
    }

    private void readItemList() {                        //para leer el fichero
        itemList = new ArrayList<>();                    //aquí se rellenará la lista

        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);//Recibe una tabla de BY.  Nread  es el num de bytes léidos

            String content = new String (buffer, 0, nread); //construir con constructor 0 es
            // el offset(inicio), nread la longitud, el string que creará el contenido del fichero
            String[] lines = content.split("\n");   //extraer las líneas del fichero y dividir

            for (String line : lines) {//para extraer las líneas del string, foreach pasa por lineas
                String[] parts = line.split(";");   //; separa string del bool parts [0] es
                // el nombre, parts[1] el bool(texto).
                itemList.add(new ShoppingItem(parts[0], parts[1].equals("true")));
                //añadir items a la lista, será cierto cuando p[1] sea true(checked)
            }
            fis.close();

        } catch (FileNotFoundException e) {
            Log.i("manu", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.e("Manu", "writeItemList: IOException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {   //Para no guardar cada vez que creamos un ítem
        super.onStop();
        writeItemlist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = (ListView) findViewById(R.id.list);
        btn_add = (Button) findViewById(R.id.btn_add);
        edit_item = (EditText) findViewById(R.id.edit_item);

        readItemList();

        adapter = new ShoppingListAdapter(
                this,
                android.R.layout.simple_list_item_1,
                itemList
        );

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem();
                return true;
            }
        });

        list.setAdapter(adapter);
//para enterarnos cuando clican un elemento
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override           // posicion que han clicado = pos
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                itemList.get(pos).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });
    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String fmt = getResources().getString(R.string.confirm_message);
        builder.setMessage(String.format(fmt,itemList.get(pos).getText()));
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty()) {
            itemList.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
        }
        //al añadir item que el scroll baje para que puedas verlo:
        list.smoothScrollToPosition(itemList.size()-1); //última pos size()-1
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){  //objeto menu que es para rellenar
        MenuInflater inflater = getMenuInflater();  //inflador de menu
        inflater.inflate(R.menu.options, menu);     //infla el menu con este recurso
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){    //para que los click funcionen en el menu
        switch (item.getItemId()){
            case R.id.clear_checked:
                clearChecked();                             //limpiar todos los seleccionados
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    private void clearChecked() {                           //limpiar todos los seleccionados
        int i = 0;
        while (i < itemList.size()){                //while para que no bajen los elementos borrados
            if(itemList.get(i).getCheck()){                 //Borrar si esta marcado
                itemList.remove(i);
            }
            else {i++;}
        }
        adapter.notifyDataSetChanged();
    }

}
