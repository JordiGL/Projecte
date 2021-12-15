package controlador.gestor;

import android.widget.EditText;

import java.util.LinkedList;

public class GestorText {
    private static LinkedList<String> lifo;
    private static EditText comunicadorEditText;

    public static void initializeLifoText( EditText editText){
        lifo = new LinkedList<>();
        comunicadorEditText = editText;
    }

    public static LinkedList<String> getLifoText(){
        return lifo;
    }

    public static void refreshEditText(){
        comunicadorEditText.getText().clear();
        if(!lifo.isEmpty()){
            for(String text: lifo){
                comunicadorEditText.append(text);
            }
        }
    }

}
