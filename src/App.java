import javax.swing.*;
import java.util.*; 

public class App{
    public static void main(String[] args)throws Exception{
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappybird = new FlappyBird();

        frame.add(flappybird);
        frame.pack();
        // ṭo avoid title included in defined height

        flappybird.requestFocus(); 
        frame.setVisible(true);
    }
}