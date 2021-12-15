package controlador.gestor;

import android.widget.EditText;

import java.util.LinkedList;

public class GestorText {
    private static LinkedList<String> textList;
    private static EditText comunicadorEditText;

    public static void initializeTextList(EditText editText){
        textList = new LinkedList<>();
        comunicadorEditText = editText;
    }

    public static LinkedList<String> getTextList(){
        return textList;
    }

    public static void refreshEditText(){
        comunicadorEditText.getText().clear();
        if(!textList.isEmpty()){
            for(String text: textList){
                comunicadorEditText.append(text);
            }
        }
    }
}
