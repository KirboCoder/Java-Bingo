package driver;
import java.util.Random;
import java.util.ArrayList;

public class Main {
	
    protected int board[][]=new int[5][5];
    protected boolean markers[][]= new boolean[5][5];
    protected String panels[][]= new String[5][5];
    protected ArrayList<Integer> balls;
    protected static int numberOfTurns = 0;
    
    public void Bingo(){
        balls = new ArrayList<Integer>();
        GenerateBoard();
        DrawNumber();
        
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
                if(panels[i][j] == "O") {
				panels[i][j]= ("#");
				}
                if(board[i][j]==balls.get(val)){
                    markers[i][j] = true;
                    panels[i][j] = ("O");
                    hadIt = true;
                    x=i;
                    y=j;
                    break;
                }
            }
            if(hadIt){break;}
        }
        System.out.println("Drew " + (balls.get(val)) + ". You did " + ((hadIt)?"":"not ")+"have it");
        printBoardStatus();
        balls.remove(val);
        
        if(hadIt && CheckWinCondition(x,y)){
            System.out.println("BINGO! YOU WON IN "+ numberOfTurns + " TURNS!");
        }
    }

    private void printBoardStatus(){
        System.out.println("-------------------------------");
        for(int i = 0; i < 5; i++){
            System.out.print("|");
            for(int j = 0; j < 5; j++){
                if(markers[i][j]){
                    String s = panels[i][j];
                    System.out.print("  "+s+"  |");
                    continue;
                }
                System.out.print(String.format("%3d  |", board[i][j]));
            }
            System.out.print("\n");
        }
        System.out.println("-------------------------------");
    }

}
