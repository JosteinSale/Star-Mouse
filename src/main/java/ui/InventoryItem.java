package ui;

import static utils.Constants.UI.ITEM_MAX_LETTERS;

import java.util.ArrayList;

public class InventoryItem {
    private String itemName;
    private ArrayList<String> itemDescription;

    public InventoryItem(String name, String description) {
        this.itemName = name;
        this.itemDescription = formatString(description);
    }

    private ArrayList<String> formatString(String description) {
        ArrayList<String> formattedStrings = new ArrayList<>();
        ArrayList<Integer> breakPoints = new ArrayList<>();
        String[] words = description.split(" ");
        int letterCount = 0;
        int breakCount = 0;
        for (String word : words) {
            letterCount += word.length();
            breakCount += word.length();
            if ((letterCount) > ITEM_MAX_LETTERS) {
                breakCount -= (word.length() + 1); // Trekker fra et ord og et mellomrom
                breakPoints.add(breakCount);
                breakCount += word.length() + 1; // Legger til samme ord og mellomrom igjen

                letterCount = word.length(); // 'Nullsettes'
            }
            letterCount += 1; // +1 for mellomrom
            breakCount += 1;
        }
        breakPoints.add(description.length()); // Siste breakpoint

        int beginIndex = 0;
        for (Integer endIndex : breakPoints) {
            String line = description.substring(beginIndex, endIndex);
            formattedStrings.add(line);
            beginIndex += (endIndex + 1 - beginIndex);
        }
        return formattedStrings;
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
