import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;


public class Panel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;

    // the bigger the delay value, the slower the game and vice versa...
    static final int DELAY = 75;

    // create two arrays that hold the coordinates of the snake body
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    // length of body 
    int bodyParts = 5;
    // nums of food eaten
    int foodEaten;

    // coorinates of the food location
    int foodX;
    int foodY;

    // initial direction and movement....
    char direction = 'R';
    boolean running = false;
    
    Timer timer;
    Random random;

    Panel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintGameComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {

                // draw lines to mimick pixels....
                for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                    g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                    g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
                }
                g.setColor(Color.red);
                g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i<bodyParts; i++) {
                if(i==0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + foodEaten, (SCREEN_WIDTH - metrics.  stringWidth("Score:" + foodEaten))/2 , g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newFood() {
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move() {
        // shift all the coordinates of the array by one spot
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // controls ....
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollision() {
        // check if the snake bite itself....
        for(int i = bodyParts; i>0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // chech if the the snake bite on the left boundary
        if(x[0] < 0) {
            running = false;
        }
        // chech if the the snake bite on the right boundary
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // chech if the the snake bite on the top boundary
        if(y[0] < 0) {
            running = false;
        }
        // chech if the the snake bite on the left boundary
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score:" + foodEaten, (SCREEN_WIDTH -metrics.  stringWidth("Score:" + foodEaten))/2 , g.getFont().getSize());
        // Game Over  
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2 , SCREEN_HEIGHT/2);
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R' ) {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L' ) {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D' ) {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U' ) {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
        
    }
    
}
