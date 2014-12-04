package ReversiAI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

class Board{ 
    
    public class Point{
        int x, y;
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString(){
            return "["+x+", "+y+"]";
        }
        
        @Override
        public boolean equals(Object o){
            return o.hashCode()==this.hashCode();
        }

        @Override
        public int hashCode() {
            return Integer.parseInt(x+""+y);
        }
    }
    
    private char[][] board;
    int WScore, BScore, remaining;
    private Point nextMove;
    public char boardX[] = new char[]{'A','B','C','D','E','F','G','H'};
    int[][] assignedScores = new int[][]{
        {6,  -3, 4, 4, 4, 4, -3, 6},
        {-3, -4,-2,-3,-3,-2,-4, -3},
        { 4, -2, 3, 2, 2, 3, -2, 4},
        { 4, -1, 2, 0, 0, 2, -1, 4},
        { 4, -1, 2, 0, 0, 2, -1, 4},
        { 4, -2, 3, 2, 2, 3, -2, 4},
        {-3, -4,-2,-3,-3,-2,-4, -3},
        { 6, -3, 4, 4, 4, 4, -3, 6},
    }; 
    
    public Board(){
        board = new char[8][8];
        for(int i=0;i<8;++i) Arrays.fill(board[i], 0, 8, '_'); 
        
        board = new char[][]{
            {'_','_','_','_','_','_','_','_',},
            {'_','_','_','_','_','_','_','_',},
            {'_','_','_','_','_','_','_','_',},
            {'_','_','_','W','B','_','_','_',},
            {'_','_','_','B','W','_','_','_',},
            {'_','_','_','_','_','_','_','_',},
            {'_','_','_','_','_','_','_','_',},
            {'_','_','_','_','_','_','_','_',},
        };  
        
        
        
    } 
    
    private HashSet<Point> placeableLocationsFinder(char player, char opponent, HashSet<Point> placeablePositions){  
        for(int i=0;i<8;++i){
            for(int j=0;j<8;++j){
                if(board[i][j] == opponent){
                    int I = i, J = j;  
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1] == '_'){ 
                        i = i+1; j = j+1;
                        while(i<7 && j<7 && board[i][j] == opponent){i++;j++;}
                        if(i<=7 && j<=7 && board[i][j] == player) placeablePositions.add(new Point(I-1, J-1));
                    } 
                    i=I;j=J;
                    if(i-1>=0 && board[i-1][j] == '_'){
                        i = i+1;
                        while(i<7 && board[i][j] == opponent) i++;
                        if(i<=7 && board[i][j] == player) placeablePositions.add(new Point(I-1, J));
                    } 
                    i=I;
                    if(i-1>=0 && j+1<=7 && board[i-1][j+1] == '_'){
                        i = i+1; j = j-1;
                        while(i<7 && j>0 && board[i][j] == opponent){i++;j--;}
                        if(i<=7 && j>=0 && board[i][j] == player) placeablePositions.add(new Point(I-1, J+1));
                    }  
                    i=I;j=J;
                    if(j-1>=0 && board[i][j-1] == '_'){
                        j = j+1;
                        while(j<7 && board[i][j] == opponent)j++;
                        if(j<=7 && board[i][j] == player) placeablePositions.add(new Point(I, J-1));
                    }
                    j=J;
                    if(j+1<=7 && board[i][j+1] == '_'){
                        j=j-1;
                        while(j>0 && board[i][j] == opponent)j--;
                        if(j>=0 && board[i][j] == player) placeablePositions.add(new Point(I, J+1));
                    }
                    j=J;
                    if(i+1<=7 && j-1>=0 && board[i+1][j-1] == '_'){
                        i=i-1;j=j+1;
                        while(i>0 && j<7 && board[i][j] == opponent){i--;j++;}
                        if(i>=0 && j<=7 && board[i][j] == player) placeablePositions.add(new Point(I+1, J-1));
                    }
                    i=I;j=J;
                    if(i+1 <= 7 && board[i+1][j] == '_'){
                        i=i-1;
                        while(i>0 && board[i][j] == opponent) i--;
                        if(i>=0 && board[i][j] == player) placeablePositions.add(new Point(I+1, J));
                    }
                    i=I;
                    if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1] == '_'){
                        i=i-1;j=j-1;
                        while(i>0 && j>0 && board[i][j] == opponent){i--;j--;}
                        if(i>=0 && j>=0 && board[i][j] == player)placeablePositions.add(new Point(I+1, J+1));
                    }
                    i=I;j=J;
                    }
                } 
        }
        return placeablePositions;
    } 
         
    private void movePlacer(Point p, char player, char opponent, Stack<Point> movePlaced){ 
        int i = p.x, j = p.y;
        board[i][j] = player; 
        int I = i, J = j;  
        
        movePlaced.clear();
        movePlaced.push(new Point(i, j));
        if(i-1>=0 && j-1>=0 && board[i-1][j-1] == opponent){ 
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j] == opponent){i--;j--;}
            if(i>=0 && j>=0 && board[i][j] == player) {while(i!=I-1 && j!=J-1){board[++i][++j]=player;movePlaced.push(new Point(i, j));}}
        } 
        i=I;j=J; 
        if(i-1>=0 && board[i-1][j] == opponent){
            i = i-1;
            while(i>0 && board[i][j] == opponent) i--;
            if(i>=0 && board[i][j] == player) {while(i!=I-1){board[++i][j]=player;movePlaced.push(new Point(i, j));}}
        } 
        i=I; 
        if(i-1>=0 && j+1<=7 && board[i-1][j+1] == opponent){
            i = i-1; j = j+1;
            while(i>0 && j<7 && board[i][j] == opponent){i--;j++;}
            if(i>=0 && j<=7 && board[i][j] == player) {while(i!=I-1 && j!=J+1){board[++i][--j] = player;movePlaced.push(new Point(i, j));}}
        }   
        i=I;j=J;
        if(j-1>=0 && board[i][j-1] == opponent){
            j = j-1;
            while(j>0 && board[i][j] == opponent)j--;
            if(j>=0 && board[i][j] == player) {while(j!=J-1){board[i][++j] = player;movePlaced.push(new Point(i, j));}}
        }
        j=J; 
        if(j+1<=7 && board[i][j+1] == opponent){
            j=j+1;
            while(j<7 && board[i][j] == opponent)j++;
            if(j<=7 && board[i][j] == player) {while(j!=J+1){board[i][--j] = player;movePlaced.push(new Point(i, j));}}
        }
        j=J; 
        if(i+1<=7 && j-1>=0 && board[i+1][j-1] == opponent){ 
            i=i+1;j=j-1;
            while(i<7 && j>0 && board[i][j] == opponent){i++;j--;}
            if(i<=7 && j>=0 && board[i][j] == player) {while(i!=I+1 && j!=J-1){board[--i][++j] = player;movePlaced.push(new Point(i, j));}}
        }
        i=I;j=J; 
        if(i+1 <= 7 && board[i+1][j] == opponent){ 
            i=i+1;
            while(i<7 && board[i][j] == opponent) i++;
            if(i<=7 && board[i][j] == player) {while(i!=I+1){board[--i][j] = player;movePlaced.push(new Point(i, j));}}
        }
        i=I;

        if(i+1 <= 7 && j+1 <=7 && board[i+1][j+1] == opponent){
            i=i+1;j=j+1;
            while(i<7 && j<7 && board[i][j] == opponent){i++;j++;}
            if(i<=7 && j<=7 && board[i][j] == player)while(i!=I+1 && j!=J+1){board[--i][--j] = player;movePlaced.push(new Point(i, j));}}
    }  
    
     
    public void displayBoard(Board b){  
        System.out.print("\n  ");
        for(int i=0;i<8;++i)System.out.print(boardX[i]+" ");
        System.out.println();
        for(int i=0;i<8;++i){
            System.out.print((i+1)+" ");
            for(int j=0;j<8;++j)
                System.out.print(b.board[i][j]+" ");
            System.out.println();
        }
        System.out.println(); 
    }
    
    public void updateScores(){
        WScore = 0; BScore = 0; remaining = 0;
        for(int i=0;i<8;++i){
            for(int j=0;j<8;++j){
                if(board[i][j]=='W')WScore++;
                else if(board[i][j]=='B')BScore++;
                else remaining++;
            }
        }
    }
    
    public int gameResult(Set<Point> whitePlaceableLocations, Set<Point> blackPlaceableLocations){  
        updateScores();
        if(remaining == 0){
            if(WScore > BScore) return 1;
            else if(BScore > WScore) return -1;
            else return 0; //Draw
        }
        if(WScore==0 || BScore == 0){
            if(WScore > 0) return 1;
            else if(BScore > 0) return -1; 
        } 
        if(whitePlaceableLocations.isEmpty() && blackPlaceableLocations.isEmpty()){
            if(WScore > BScore) return 1;
            else if(BScore > WScore) return -1;
            else return 0; //Draw
        }
        return -2;
    } 
    
    public HashSet<Point> getPlaceableLocations(char player, char opponent){
        HashSet<Point> placeablePositions = new HashSet<>();
        return placeableLocationsFinder(player, opponent, placeablePositions);
    } 
     
    public void showPlaceableLocations(HashSet<Point> locations, char player, char opponent){
        for(Point p:locations)
            board[p.x][p.y]='*';
        displayBoard(this);
        for(Point p:locations)
            board[p.x][p.y]='_';
    }
    
    public Stack<Point> placeMove(Point p, char player, char opponent){
         Stack<Point> movePlaced = new Stack<>();
         movePlacer(p, player, opponent, movePlaced);
         return movePlaced;
    }
     
    public int coordinateX(char x){
        for(int i=0;i<8;++i)if(boardX[i]==Character.toLowerCase(x)||boardX[i]==Character.toUpperCase(x))return i;
        return -1; // Illegal move received
    }
    
    /*
        AI Code Let user play first - Is Black. AI is White
    */
    private double prepareNextMove(int depth, int maxDepth, char player, char opponent, HashSet<Point> locations){
        
        if(depth == maxDepth){
            int result = gameResult(getPlaceableLocations(player, opponent), getPlaceableLocations(opponent, player));
            if(result == 1)return 700;
            if(result == -1)return -700;
            if(result == 0) return 0; 

            int score=0;
            for(int i=0;i<8;++i)for(int j=0;j<8;++j){
                if(board[i][j]=='W')score+=assignedScores[i][j];
                else if(board[i][j]=='B')score-=assignedScores[i][j];
            }
            return score;
        }
         
        double WS = Integer.MIN_VALUE; 
        double BS = Integer.MAX_VALUE;
        
        if(locations.isEmpty()){return player=='W'?-500:500;}
        Iterator<Point> it = locations.iterator(); 
        Stack<Point> moveRecord;
        
        while(it.hasNext()){  
            Point p = it.next();

            Stack<Point> movePlaced = placeMove(p, player, opponent);
            moveRecord = new Stack();
            while(!movePlaced.isEmpty())moveRecord.push(movePlaced.pop());

            HashSet<Point> locationsAfterMove = getPlaceableLocations(player, opponent);
            HashSet<Point> locationsOpp = getPlaceableLocations(opponent, player);
            
            double multiplier = 1;
            if(isStable(p, player)) multiplier = 3.0f;
//            multiplier += frontierValue(moveRecord); 
            int playerMoves = locations.size(), opponentMoves = locationsOpp.size();
             
            if(playerMoves!=0 && opponentMoves!=0) multiplier += (playerMoves)/(playerMoves+opponentMoves);

            if(player == 'W'){  
                double currentScore = multiplier * prepareNextMove(depth+1, maxDepth, 'B', 'W', locationsAfterMove);
                WS = Math.max(currentScore, WS);
                if(depth==0){ 
                    System.out.println("Point: "+boardX[p.x]+(p.y+1)+"score: "+currentScore);
                    if(currentScore==WS)
                    nextMove = p; 
                }
            }else if(player == 'B'){ 
                BS = Math.min(-multiplier * prepareNextMove(depth+1, maxDepth, 'W', 'B', locationsOpp), BS);
            } 

            //Reset last move  
            p = moveRecord.pop();
            board[p.x][p.y] = '_'; 
            while(!moveRecord.isEmpty()){  p = moveRecord.pop();board[p.x][p.y] = opponent;} 
        } 
        return player=='W'?WS:BS; 
    } 
    
    //STABILITY
    public boolean isStable(Point p, char player){
        boolean stable1 = true, stable2 = true, stable3 = true, stable4 = true;
        for(int i=p.x;i>=0;--i){
            for(int j=p.y;j>=0;--j){
                if(board[i][j]!=player) stable1 = false;
            }
        }  
        if(stable1)return true;
        for(int i=p.x;i>=0;--i){
            for(int j=p.y;j<=7;++j){
                if(board[i][j]!=player) stable2 = false;
            }
        }
        if(stable2)return true;
        for(int i=p.x;i<=7;++i){
            for(int j=p.y;j>=0;j--){
                if(board[i][j]!=player) stable3 = false;
            }
        }  
        if(stable3)return true;
        for(int i=p.x;i<=7;++i){
            for(int j=p.y;j<=7;++j){
                if(board[i][j]!=player) stable4 = false;
            }
        } 
        return stable4;
    }
    
    //FRONTIER - Number of blanks neighboring newly flipped disks
//    public double frontierValue(Stack<Point> flippedCells){ 
//        double frontier = 0.9 * flippedCells.size();
//        for(Point p:flippedCells){ 
//            if(p.x!=0&&p.y!=0)  if(board[p.x-1][p.y-1]=='_') frontier-=0.1;
//            if(p.x!=0)          if(board[p.x-1][p.y]=='_')   frontier-=0.1;
//            if(p.x!=0&&p.y!=7)  if(board[p.x-1][p.y+1]=='_') frontier-=0.1;
//            if(p.y!=0)          if(board[p.x][p.y-1]=='_')   frontier-=0.1;
//            if(p.y!=7)          if(board[p.x][p.y+1]=='_')   frontier-=0.1;
//            if(p.x!=7&&p.y!=0)  if(board[p.x+1][p.y-1]=='_') frontier-=0.1;
//            if(p.x!=7)          if(board[p.x+1][p.y]=='_')   frontier-=0.1;
//            if(p.x!=7&&p.y!=7)  if(board[p.x+1][p.y+1]=='_') frontier-=0.1;
//        } 
//        return (frontier/(0.9*flippedCells.size()));
//    } 
    
    public Point getNextMove(int depth, char player, char opponent){
        nextMove = null;
        prepareNextMove(0, depth, player, opponent, getPlaceableLocations(player, opponent));
        return nextMove;
    }
} 


public class Reversi {
    public static void main(String[] args){
        Board b = new Board();  
        
        Scanner scan = new Scanner(System.in);
        Board.Point move = b.new Point(-1, -1); 
        System.out.println("Human : Black Computer : White");
        System.out.println("Black Moves first"); 
        
        int result;
        Boolean skip;
        String input;
        
        while(true){ 
            skip = false;
            
            HashSet<Board.Point> blackPlaceableLocations = b.getPlaceableLocations('B', 'W');
            HashSet<Board.Point> whitePlaceableLocations = b.getPlaceableLocations('W', 'B');

            b.showPlaceableLocations(blackPlaceableLocations, 'B', 'W'); 
            result = b.gameResult(whitePlaceableLocations, blackPlaceableLocations);
            
            if(result == 0){System.out.println("It is a draw.");break;}
            else if(result==1){System.out.println("White wins: "+b.WScore+":"+b.BScore);break;}
            else if(result==-1){System.out.println("Black wins: "+b.BScore+":"+b.WScore);break;}

            if(blackPlaceableLocations.isEmpty()){ 
                    System.out.println("Black needs to skip... Passing to white");
                    skip = true; 
            }

            if(!skip){
                System.out.println("Place move (Black): ");
                input = scan.next();
                move.y = b.coordinateX(input.charAt(0));
                move.x = (Integer.parseInt(input.charAt(1)+"")-1); 
                
                while(!blackPlaceableLocations.contains(move)){
                    System.out.println("Invalid move!\n\nPlace move (Black): ");
                    input = scan.next();
                    move.y = b.coordinateX(input.charAt(0)); 
                    move.x = Integer.parseInt(input.charAt(1)+"");  
                }
                b.placeMove(move, 'B', 'W');
                b.updateScores();
                System.out.println("\nBlack: "+b.BScore+" White: "+b.WScore);
            }
            skip = false;

            whitePlaceableLocations = b.getPlaceableLocations('W', 'B');
            blackPlaceableLocations = b.getPlaceableLocations('B', 'W');

            b.showPlaceableLocations(whitePlaceableLocations, 'W', 'B');
            result = b.gameResult(whitePlaceableLocations, blackPlaceableLocations);

            if(result==0){System.out.println("It is a draw.");break;} 
            else if(result==1){System.out.println("White wins: "+b.WScore+":"+b.BScore);break;}
            else if(result==-1){System.out.println("Black wins: "+b.BScore+":"+b.WScore);break;}

            if(whitePlaceableLocations.isEmpty()){ 
                    System.out.println("White needs to skip... Passing to Black");
                    skip = true; 
            }

            if(!skip){ 
            System.out.println("Computer is placing its move... ");
            move = b.getNextMove(5, 'W', 'B'); 
            System.out.println("Next best move is : "+b.boardX[move.y]+(move.x+1));
            b.placeMove(move, 'W', 'B');   
            b.updateScores();
            System.out.println("\nWhite: "+b.WScore+" Black: "+b.BScore);
            }
        }
    }
}
