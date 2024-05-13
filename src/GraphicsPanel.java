import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player1;
    private Player player2;
    private boolean[] pressedKeys;
    private ArrayList<Coin> coins;

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player1 = new Player("src/marioleft.png", "src/marioright.png");
        player2 = new Player("src/luigileft.png", "src/luigiright.png");
        coins = new ArrayList<>();
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
        g.drawImage(background, 0, 0, null);  // the order that things get "painted" matter; we put background down first
        g.drawImage(player1.getPlayerImage(), player1.getxCoord(), player1.getyCoord(), null);
        g.drawImage(player2.getPlayerImage(), player2.getxCoord(), player2.getyCoord(), null);
        // this loop does two things:  it draws each Coin that gets placed with mouse clicks,
        // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
        // the score goes up and the Coin is removed from the arraylist
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            g.drawImage(coin.getImage(), coin.getxCoord(), coin.getyCoord(), null); // draw Coin
            if (player1.playerRect().intersects(coin.coinRect())) { // check for collision
                player1.collectCoin();
                coins.remove(i);
                i--;
            }
            if (player2.playerRect().intersects(coin.coinRect())) { // check for collision
                player2.collectCoin();
                coins.remove(i);
                i--;
            }
        }

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Score: " + player1.getScore(), 20, 40);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Score: " + player2.getScore(), 220, 40);

        // player moves left (A)
        if (pressedKeys[65]) {
            player1.faceLeft();
            player1.moveLeft();
        }

        // player moves right (D)
        if (pressedKeys[68]) {
            player1.faceRight();
            player1.moveRight();
        }

        // player moves up (W)
        if (pressedKeys[87]) {
            player1.moveUp();
        }

        // player moves down (S)
        if (pressedKeys[83]) {
            player1.moveDown();
        }

        // player moves left (J)
        if (pressedKeys[74]) {
            player2.faceLeft();
            player2.moveLeft();
        }

        // player moves right (L)
        if (pressedKeys[76]) {
            player2.faceRight();
            player2.moveRight();
        }

        // player moves up (I)
        if (pressedKeys[90]) {
            player2.moveUp();
        }

        // player moves down (K)
        if (pressedKeys[75]) {
            player2.moveDown();
        }
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            Coin coin = new Coin(mouseClickLocation.x, mouseClickLocation.y);
            coins.add(coin);
        }
    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented
}
