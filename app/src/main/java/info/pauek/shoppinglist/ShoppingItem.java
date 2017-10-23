package info.pauek.shoppinglist;

/**
 * Created by DjManko on 24/10/17.
 */

public class ShoppingItem {
    private String text;    //el texto de pastilla
    private Boolean checked;  //el check"          "(si esta marcado o no)

    public ShoppingItem(String text) {
        this.text = text;   //this.text es para diferenciar del private Strint text.
        this.checked=false;
    }

    public ShoppingItem(String text, Boolean check) {
        this.text = text;
        this.checked = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCheck() {
        return checked;
    }

    public void setCheck(Boolean check) {
        this.checked = check;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
