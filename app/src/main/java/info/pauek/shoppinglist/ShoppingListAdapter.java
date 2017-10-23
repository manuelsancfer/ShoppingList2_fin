package info.pauek.shoppinglist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by DjManko on 23/10/17.
 */

public class ShoppingListAdapter extends ArrayAdapter<String> {
    public ShoppingListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

                //Controlar que pasa en la "pastillita"
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View result = convertView;     //reciclamos el convertview
        if(result == null){             //crear pastillita
            LayoutInflater inflater = LayoutInflater.from(getContext());   //inflador
            result = inflater.inflate(R.layout.shopping_item, null);
        }

        CheckBox shopping_item = (CheckBox) result.findViewById(R.id.shopping_item); //la busqueda
                                    // dentro del view que me ha devuelto el inflador(pastillita).
        String item_text = getItem(position);//sin arrayadapter<string> hay que poner delante (string)
        shopping_item.setText(item_text);       //escribo el item en checkbox

        return result;  //devuelvo la pastillita de la lista
    }
}
