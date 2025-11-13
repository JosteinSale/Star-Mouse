package ui;

import java.util.ArrayList;

import static utils.Constants.UI.INFOBOX_MAX_LETTERS;
import static utils.HelpMethods.ChopStringIntoLines;

public class InfoBox {
    public ArrayList<String> formattedStrings;

    public InfoBox() {
        this.formattedStrings = new ArrayList<>();
    }

    public void setText(String text) {
        this.formattedStrings = ChopStringIntoLines(text, INFOBOX_MAX_LETTERS);
    }

}
