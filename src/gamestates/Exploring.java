package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.exploring.InteractableObject;
import main.Game;
import utils.LoadSave;
import static utils.Constants.Exploring.*;
import static utils.HelpMethods.CanWalkHere;

public class Exploring extends State implements Statemethods {
    private BufferedImage bgImg;
    private BufferedImage collisionImg;

    private Rectangle2D.Float playerHitbox;
    private boolean leftIsPressed, upIsPressed, rightIsPressed, downIsPressed;
    private float playerSpeed = 4f;

    private boolean textBoxActive = false;
    private ArrayList<InteractableObject> objects;
    private int activeObjectIndex;

    public Exploring(Game game) {
        super(game);
                                        // These names need to be filled in automatically
        bgImg = LoadSave.getImageBackground(LoadSave.LEVEL1_AREA1_BG);
        collisionImg = LoadSave.getImageCollision(LoadSave.LEVEL1_AREA1_CL);   

        playerHitbox = new Rectangle2D.Float(480f, 510f, 45f, 18f);

        objects = new ArrayList<>();
        InteractableObject bed = new InteractableObject(
            new Rectangle2D.Float(267f, 429f, 155, 138f), 
            "My bed. Semi-comfortable."); 
        InteractableObject drawer = new InteractableObject(
            new Rectangle2D.Float(450f, 450f, 132f, 10f), 
            "A drawer with all my stuff. Clothes mostly, not much of interest.");  
        InteractableObject xbox = new InteractableObject(
            new Rectangle2D.Float(650f, 459f, 90f, 51f), 
            "I don't have time for that now...");
        //doorHitbox = new Rectangle2D.Float(729f, 531f, 54f, 69f);    
        objects.add(bed);
        objects.add(drawer);
        objects.add(xbox);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (textBoxActive) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                textBoxActive = false;   // bytter dette ut med getNextString senere
            }
            return;
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftIsPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upIsPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightIsPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downIsPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                checkHitboxes();
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downIsPressed = false;
        }
    }

    private void checkHitboxes() {
        for (int i = 0; i < objects.size(); i++) {
            if (playerHitbox.intersects(objects.get(i).getHitbox())) {
                textBoxActive = true;
                activeObjectIndex = i;
            }
        }
    }

    @Override
    public void update() {
        if (leftIsPressed) {
            if (CanWalkHere(playerHitbox, -playerSpeed, 0f, collisionImg)) {
                playerHitbox.x -= playerSpeed;
            }
        }
        if (upIsPressed) {
            if (CanWalkHere(playerHitbox, 0f, -playerSpeed, collisionImg)) {
                playerHitbox.y -= playerSpeed;
            }
        }
        if (rightIsPressed) {
            if (CanWalkHere(playerHitbox, playerSpeed, 0f, collisionImg)) {
                playerHitbox.x += playerSpeed;
            }
        }
        if (downIsPressed) {
            if (CanWalkHere(playerHitbox, 0f, playerSpeed, collisionImg)) {
                playerHitbox.y += playerSpeed;
            }
        }
    }


    @Override
    public void draw(Graphics g) {       // These names need to be filled in automatically
        g.drawImage(
            bgImg, 
            0, 0, 
            (int) (LEVEL1_AREA1_BG_WIDTH * Game.SCALE), 
            (int) (LEVEL1_AREA1_BG_HEIGHT * Game.SCALE), 
            null);
        drawHitboxes(g);
        if (textBoxActive) {
            objects.get(activeObjectIndex).drawTextBox(g);
        }
    }

    private void drawHitboxes(Graphics g) {
        // player 
        g.setColor(Color.RED);
        g.drawRect(
            (int) (playerHitbox.x * Game.SCALE),
            (int) (playerHitbox.y * Game.SCALE),
            (int) (playerHitbox.width * Game.SCALE),
            (int) (playerHitbox.height * Game.SCALE));
        
        // objects
        for (InteractableObject ob : objects) {
            ob.drawHitbox(g);
        }
    }

}
