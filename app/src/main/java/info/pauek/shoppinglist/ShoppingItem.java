package info.pauek.shoppinglist;

/**
 * Created by DjManko on 24/10/17.
 */

public class ShoppingItem {
    private String text;    //el texto de pastilla
    private Boolean check;  //el check"          "(si esta marcado o no)

    public ShoppingItem(String text) {
        this.text = text;   //this.text es para diferenciar del private Strint text.
    }

    public ShoppingItem(String text, Boolean check) {
        this.text = text;
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
