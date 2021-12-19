package controlador.gestor;

import android.widget.EditText;

import java.util.LinkedList;

/**
 * Classe gestora del text introduit al TextEdit
 * @author Jordi Gómez Lozano
 */
public class GestorText {
    private static LinkedList<String> textList;
    private static EditText comunicadorEditText;

    public static void initializeTextList(EditText editText){
        textList = new LinkedList<>();
        comunicadorEditText = editText;
    }

    public static LinkedList<String> getList(){
        return textList;
    }

    /**
     * Mètode per a refrescar el TextEdit
     * @author Jordi Gómez Lozano
     */
    public static void refreshEditText(){
        comunicadorEditText.getText().clear();
        if(!textList.isEmpty()){
            for(String text: textList){
                comunicadorEditText.append(text);
            }
        }
    }

    /**
     * Mètode per a obtenir el text del TextEdit
     * @author Jordi Gómez Lozano
     */
    public static String getText(){
        return comunicadorEditText.getText().toString();
    }
}
