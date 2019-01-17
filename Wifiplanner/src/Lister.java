import javax.swing.JComboBox;

public class Lister {
  public static void main(String[] argv) throws Exception {
    String[] items = { "item1", "item2" };
    JComboBox cb = new JComboBox(items);

    // Add an item to the start of the list
    cb.insertItemAt("item0", 0);
  }
}