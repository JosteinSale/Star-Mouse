package cutscenes.events;

import rendering.MyColor;

/** In Exploring/Flying: An event that triggers a fade out to black */
public record FadeEvent(String in_out, MyColor color, int speed, boolean standardFade) implements GeneralEvent {
}
