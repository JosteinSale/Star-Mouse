package ui;

import static utils.Constants.UI.ITEM_MAX_LETTERS;
import static utils.HelpMethods.ChopStringIntoLines;

import java.util.ArrayList;

public class InventoryItem {
    private String itemName;
    private ArrayList<String> itemDescription;

    public InventoryItem(String name, String description) {
        this.itemName = name;
        this.itemDescription = ChopStringIntoLines(description, ITEM_MAX_LETTERS);
    }

    public String getName() {
        return itemName;
    }

    public ArrayList<String> getDescription() {
        return itemDescription;
    }

    public void setDescription(ArrayList<String> itemDescription) {
        this.itemDescription = itemDescription;
    }
}
