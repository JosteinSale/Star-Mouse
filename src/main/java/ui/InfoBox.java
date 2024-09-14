package ui;

import java.util.ArrayList;

import static utils.Constants.UI.INFOBOX_MAX_LETTERS;;

public class InfoBox {
    public ArrayList<String> formattedStrings;

    public InfoBox() {
        this.formattedStrings = new ArrayList<>();
    }

    public void setText(String text) {
        this.formattedStrings = formatString(text);
    }

    private ArrayList<String> formatString(String text) {
        ArrayList<String> formattedString = new ArrayList<>();
        String[] words = text.split(" ");
        int letterCount = 0;
        String line = "";
        for (String word : words) {
            if ((letterCount + word.length()) > INFOBOX_MAX_LETTERS) {
                formattedString.add(line);
                line = word + " ";
                letterCount = 0;
            } else {
                line += (word + " ");
                letterCount += word.length();
                letterCount += 1; // for mellomrom
            }
        }
        formattedString.add(line);
        return formattedString;
    }

}
