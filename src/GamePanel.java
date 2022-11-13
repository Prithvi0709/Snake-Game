import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;    // SIze of the items in the game
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 50;    // Dictates the speed of the game

    // Stores the X and Y positions for all the body parts of the snake;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;  // Number of body parts currently present in the snake
    int applesEaten;    // Number of apples that the snake has eaten

    int appleX;     // X-position of the apple that spawns --> Done at random
    int appleY;     // Y-position of the apple that spawns --> Random position

    char direction = 'R'; // R -> Right  L -> Left  U -> Up  D -> Down (Initial Starting direction will be Right)

    boolean running = false;
    Timer timer;
    Random random;

    //Constructor
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // This constructor is called when the SnakeGame class is run. The start game method starts the game.
        startGame();
    }

    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        if(running)
        {
            // Generates a grid
//            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++)
//            {
//                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT );
//            }
//            for(int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++)
//            {
//                g.drawLine(0,i*UNIT_SIZE , SCREEN_WIDTH, i*UNIT_SIZE );
//            }

            //Draw the apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE,UNIT_SIZE);

            // Draw the snake
            for(int i = 0; i < bodyParts; i++)
            {

                if (i == 0)
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw the score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(""+applesEaten,UNIT_SIZE/2,UNIT_SIZE);
        }
        else
        {
            gameOver(g);
        }


    }

    // Generate a new Apple everytime an Apple is eaten
    public void newApple()
    {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        // We are multiplying by the UNIT_SIZE after the random number has been found.
        // This is done to find the location of the matrix in the grid.

        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;
    }

    public void move()
    {
        // The snake first moves in the required direction
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        // The value of the head is then updated
        switch(direction)
        {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }




    }

    public void checkApple()
    {
        if((x[0] == appleX) && y[0] == appleY)
        {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }


    public void checkCollisions()
    {
        //Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i]))
            {
                running = false;
                break;
            }

        }

        //Check if head touches left border
        if(x[0] < 0)
        {
            x[0] = SCREEN_WIDTH;
        }
        //Check if head touches right border
        if(x[0] > SCREEN_WIDTH)
        {
            x[0] = -UNIT_SIZE;
        }
        // Check if head touches top border
        if(y[0] < 0)
        {
            y[0] = SCREEN_HEIGHT;
        }
        //Check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT)
        {
            y[0] = -UNIT_SIZE;
        }

        if(!running)
        {
            timer.stop();
        }

    }

    public void gameOver(Graphics g)
    {
        String score = String.valueOf(applesEaten);
        // Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT - 3*SCREEN_HEIGHT/4);

        // Score Text
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2,SCREEN_HEIGHT - SCREEN_HEIGHT/3);
    }

    public class MyKeyAdapter extends KeyAdapter
    {

        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                // Case for when user presses Left Arrow
                case KeyEvent.VK_LEFT:
                    // The user must not be allowed to turn 180 degrees.
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;
                // Case for when user presses Right Arrow
                case KeyEvent.VK_RIGHT:
                    // The user must not be allowed to turn 180 degrees.
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;
                // Case for when the user presses the up arrow
                case KeyEvent.VK_UP:
                    // The user must not be allowed to turn 180 degrees.
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;
                // Case for when the user presses the down arrow
                case KeyEvent.VK_DOWN:
                    // The user must not be allowed to turn 180 degrees.
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

}
