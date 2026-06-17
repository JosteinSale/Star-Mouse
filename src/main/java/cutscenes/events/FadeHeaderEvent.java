package cutscenes.events;

import rendering.MyColor;

/**
 * In Flying: An event that fades in/out a specific header text at a specific
 * y-pos.
 */
public record FadeHeaderEvent(String fadeDir, MyColor color, int yPos, int fadeSpeed, String text)
      implements GeneralEvent {
}
