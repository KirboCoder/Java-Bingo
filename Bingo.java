package driver;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
public class Bingo{
    JFrame window;
    JButton button;
    JPanel bingoPanel;
    protected int board[][]=new int[5][5];
    protected boolean markers[][]=new boolean[5][5];
    protected JLabel panels[][]=new JLabel[5][5];
    protected ArrayList<Integer> balls;
    protected static int numberOfTurns = 0;
    public Bingo(){
        balls = new ArrayList<Integer>();
        GenerateBoard();
        window = new JFrame();
        window.setLayout(null);
        window.setResizable(false);
        setupInteractibles();
        setupJFrame();
    }

    private void GenerateBoard(){
        Random rand = new Random();
        for(int j = 0; j < 5; j++){
            int range = ((j+1) * 15);
            for(int i = 0; i < 5; i++){
                int val = (rand.nextInt(15)+range)-14;
                while(checkRepeatNumber(j, val)){
                    val = (rand.nextInt(15)+range)-14;
                }
                board[i][j] = val;
                markers[i][j] = false;
            }
        }
        for(int i = 0; i < 75; i++){
            balls.add(i+1);
        }
    }

    private boolean checkRepeatNumber(int col, int number){
        for(int i = 0;i < 5; i++){
            if(board[i][col]==number){
                return true;
            }
        }
        return false;
    }

    private void setupJFrame(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 650);
        window.setTitle("Java BINGO!");
        window.setVisible(true);
    }
    private void setupInteractibles(){
        bingoPanel = new JPanel();
        bingoPanel.setSize(500, 500);
        bingoPanel.setLocation(0,0);
        bingoPanel.setBackground(Color.white);
        GridLayout layout = new GridLayout();
        layout.setRows(5);
        layout.setColumns(5);
        bingoPanel.setLayout(layout);
        window.add(bingoPanel);

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                panels[i][j] = new JLabel();
                panels[i][j].setText(""+board[i][j]);
                panels[i][j].setVerticalAlignment(SwingConstants.CENTER);
                panels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                bingoPanel.add(panels[i][j]);
            }
        }

        button = new JButton();
        button.setText("Draw!");
        button.setLocation(150, 500);
        button.setSize(200, 100);
        button.setBackground(Color.white);
        button.addActionListener(e -> DrawNumber());
        window.add(button);
    }

    private boolean CheckWinCondition(int row, int col){
        boolean horizontalWin = true, verticalWin = true, diagonalWinLeft = true, diagonalWinRight = true;
        int j = 4;
        for(int i = 0; i < 5; i++){
            if(!markers[row][i]){verticalWin = false;}
            if(!markers[i][col]){horizontalWin = false;}
            if(!markers[i][i]){diagonalWinLeft = false;}
            if(!markers[j][i]){diagonalWinRight = false;}
            j--;
        }
        boolean cornerWin = (markers[0][0] && markers[4][0] && markers[0][4] && markers[4][4] && markers[2][2]);
        return horizontalWin || verticalWin || diagonalWinLeft || diagonalWinRight || cornerWin;
    }

    private void DrawNumber(){
        numberOfTurns++;
        Random rand = new Random();
        int val = rand.nextInt(balls.size());
        boolean hadIt = false;
        int x=0,y=0;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(panels[i][j].getText().contains("O")){panels[i][j].setText("#");}
                if(board[i][j]==balls.get(val)){
                    markers[i][j] = true;
                    panels[i][j].setText("O");
                    TransparentPanel p = new TransparentPanel();
                    //p.setOpaque(false);
                    p.setBackground(new Color(0, 255, 0, 150));
                    panels[i][j].setLayout(null);
                    p.setSize(50,50);
                    p.setLocation(25, 25);
                    panels[i][j].add(p);
                    panels[i][j].setBackground(Color.green);
                    hadIt = true;
                    x=i;y=j;
                    break;
                }
            }
            if(hadIt){break;}
        }
        System.out.println("Drew " + (balls.get(val)) + ". You did " + ((hadIt)?"":"not ")+"have it");
        printBoardStatus();
        JFrame popup = new JFrame();
        JOptionPane.showMessageDialog(popup, "Drew " + (balls.get(val)) + ". You did " + ((hadIt)?"":"not ")+"have it");
        balls.remove(val);
        if(hadIt && CheckWinCondition(x,y)){
            JFrame WIN = new JFrame();
            int option = JOptionPane.showConfirmDialog(WIN, "BINGO! YOU WON IN "+ numberOfTurns + " TURNS!\nDo You wish to play again?");
            switch(option){
                case 0:
                resetGame();
                break;
                case 1:
                System.exit(0);
                break;
                default:
                System.exit(0);
                break;
            }

        }
    }
    private void resetGame(){
        numberOfTurns = 0;
        window.setVisible(false);
        window.removeAll();
        balls = new ArrayList<Integer>();
        GenerateBoard();
        window = new JFrame();
        window.setLayout(null);
        setupInteractibles();
        setupJFrame();
        window.setResizable(false);
    }
    private void printBoardStatus(){
        System.out.println("-------------------------------");
        for(int i = 0; i < 5; i++){
            System.out.print("|");
            for(int j = 0; j < 5; j++){
                if(markers[i][j]){
                    String s = panels[i][j].getText();
                    System.out.print("  "+s+"  |");
                    continue;
                }
                System.out.print(String.format("%3d  |", board[i][j]));
            }
            System.out.print("\n");
        }
        System.out.println("-------------------------------");
    }
    private class TransparentPanel extends JPanel {
        {
            setOpaque(false);
        }
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            Rectangle r = g.getClipBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            super.paintComponent(g);
        }
    }
}