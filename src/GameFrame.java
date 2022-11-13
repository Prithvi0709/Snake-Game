import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame()
    {
        this.add(new GamePanel());
        this.setTitle("Snake"); //Title of the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Closes the window when exit is pressed
        this.setResizable(false);   // Makes the window resizable
        this.pack();
        this.setVisible(true);  // Makes the window visible to the user
        this.setLocationRelativeTo(null);   // Sets the location of the window relative to a factor
    }

}
