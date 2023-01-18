import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.ArrayList;
public class DiamondRush {
    private int grid[][];
    private int updateGrid[][];
    public int turnsAmount;
    private int randomPatch[];
    private int randomPatchPointer;
    private int turn;
    private int playerOneWheat = 5;
    private int playerOneLumber ;
    private int playerOneRubys;
    private int playerOnePearls;
    private int playerOneSapphire;
    private int playerOneStone = 10;
    private int playerTwoWheat= 5;
    private int playerTwoStone = 10;
    private int playerOneCities[][];
    private int playerOneCitiesAmount = 1;
    private int playerOneCitiesToPlace;
    private int playerTwoCities[][];
    private int playerTwoCitiesAmount = 1;
    private int playerTwoCitiesToPlace;
    private int playerOneWorkers;
    private int playerTwoWorkers;
    private int playerOneSoldiers;
    private int playerTwoSoldiers;
    private int playerTwoLumber;
    private int playerTwoRubys;
    private int playerTwoPearls;
    private int playerTwoSapphire;
    private int playerOneNodeTracker[][];
    private int playerOneWorkerNodeTracker[][];
    private int playerTwoNodeTracker[][];
    private int playerTwoWorkerNodeTracker[][];
    private boolean gameOver;
    private ArrayList<String> text = new ArrayList<>();
    private ArrayList<String> textBorder = new ArrayList<>();
    private ArrayList<String> kingOneInventory = new ArrayList<String>();
    private ArrayList<String> kingTwoInventory = new ArrayList<String>();
    private Terminal terminal;
    private Screen screen;
    private TextGraphics textGraphics;
    private ConwaysLife display;
    private int PlayerOneKing[];
    private int PlayerTwoKing[];
    private int playerOnePieces[][];
    private int playerTwoPieces[][];
    private int playerOneKingMoves;
    private int playerTwoKingMoves;
    private int p1kingstep;
    private int p2kingstep;
    private boolean p1KingGarrisoned = false;
    private boolean p2KingGarrisoned = false;
    private int knightLastMoveOne[];
    private int knightLastMoveTwo[];
    private int pieceMovesOne = 3;
    private int pieceMovesTwo = 3;
    private boolean asked;




    DiamondRush (Terminal terminal, TextGraphics textGraphics, Screen screen, ConwaysLife display){
        this.terminal = terminal;
        this.textGraphics = textGraphics;
        this.screen = screen;
        this.display = display;
        this.grid = new int[35][200];
        this.playerOneNodeTracker = new int[36][6];
        // Y : 36 possible nodes
        //X: 1) direction 1-right 2- down 3-left 4- up 2)y cordinate 3) x cordinate 4) city origin 5) worker or miner stationed there, 1- worker 2-miner 6) original direction
        this.playerOneWorkerNodeTracker = new int[36][2];
        this.playerTwoNodeTracker = new int[36][6];
        this.playerTwoWorkerNodeTracker = new int[36][2];
        this.turnsAmount = 1;
        this.turn = 1;
        int[] newrandomPatch ={1,2,3,4,2,3,4,1,2,3,1,1,3,2,2,4,1,4,2,3,4,1,3,2,4,4,2,};
        this.PlayerOneKing = new int[2];
        this.PlayerTwoKing = new int[2];
        this.PlayerOneKing[0] = 3;
        this.PlayerOneKing[1] = 3;

        this.knightLastMoveOne = new int[3];
        this.knightLastMoveTwo = new int[3];

        this.PlayerTwoKing[0] = 30;
        this.PlayerTwoKing[1] = 3;
        this.playerOneKingMoves = 16;
        this.playerTwoKingMoves = 16;
        this.p1kingstep = 0;
        this.p2kingstep = 0;

        // 1 is up, 2 is right, 3 is down, 4 is left
        this.randomPatch = newrandomPatch;
        this.randomPatchPointer = 0;
        //in the 2d array that is the player and two cities, the first value of each row represents if there is a city, the next 4 values represent
        //if a road has been been starting in each possible direction, each city can have 4 roads, one on the right, left, top and bottom of the city.
        // if the value of the first row is one, that means theres a city, if the next value is 1 that means there is a leftside road, if the next value
        // is one that means theres a top road, and ETC.
        this.playerOneCities = new int[9][6];
        this.playerOneCities[0][0] = 1;
        this.playerTwoCities = new int[9][6];
        // 9 possible cities
        // 1) city number, 2) road right 3) road down 4) road left 5) road Up 6) settlements builing. 1 - barracks , 2- grainary, 3- query 4- lumbermill 5- stable 6- fortress
        this.playerTwoCities[0][0] = 1;
        this.playerOneWorkers = 1;
        this.playerTwoWorkers = 1;
        this.playerOnePieces = new int[50][6];
        // 50 possible pieces
        // 1) piece type, 2) xVal on grid 3) yVal on grid 4) deployed 0- not deployed 1- deployed
        this.playerTwoPieces = new int[50][6];




        //randomly fills map with grass
        int grass = 60 + (int)(Math.random() * ((80 - 60) + 1));
        for (int i = 0; i < grass; i++){
            int x = 2 + (int)(Math.random() * ((31 - 2) + 1));
            int y = 2 + (int)(Math.random() * ((197 - 2) + 1));
            grid[x][y]= 22;
            int patchSize = 2 + (int)(Math.random() * ((8 - 2) + 1));
            for (int z = 0; z < patchSize; z ++){
                int whichWay = randomPatch[randomPatchPointer];
                if (whichWay == 1){if (grid[x][y-1] != 25 ||grid[x][y-1] != 26 ){grid[x][y-1]= 22; y--; }}
                if (whichWay == 2){if (grid[x+1][y] !=25 ||grid[x+1][y] != 26 ){grid[x][y-1]= 22; x++; }}
                if (whichWay == 3){if (grid[x][y+1] != 25 ||grid[x][y+1] != 26 ){grid[x][y-1]= 22; y++; }}
                if (whichWay == 4){if (grid[x-1][y] != 25 ||grid[x-1][y] != 26 ){grid[x][y-1]= 22; x--; }}
                if (randomPatchPointer < 26){
                    this.randomPatchPointer++;
                }
                else {this.randomPatchPointer = 0;}
            }
        }


        int ruby = 12;
        for (int i = 0; i < ruby; i++){
            int x = 2 + (int)(Math.random() * ((16 - 2) + 1));
            int y = 50 + (int)(Math.random() * ((140 - 50) + 1));
            grid[x][y]=42;
        }
        ruby = 12;
        for (int i = 0; i < ruby; i++){
            int x = 17 + (int)(Math.random() * ((31 - 17) + 1));
            int y = 50 + (int)(Math.random() * ((140 - 50) + 1));
            grid[x][y]=42;
        }

        int pearl = 8;
        for (int i = 0; i < pearl; i++){
            int x = 2 + (int)(Math.random() * ((16 - 2) + 1));
            int y = 90 + (int)(Math.random() * ((170 - 90) + 1));
            grid[x][y]=43;
        }
        pearl = 8;
        for (int i = 0; i < pearl; i++){
            int x = 17 + (int)(Math.random() * ((31 - 17) + 1));
            int y = 90 + (int)(Math.random() * ((170 - 90) + 1));
            grid[x][y]=43;
        }

        int saphire = 5;
        for (int i = 0; i < saphire; i++){
            int x = 2 + (int)(Math.random() * ((16 - 2) + 1));
            int y = 120 + (int)(Math.random() * ((175 - 120) + 1));
            grid[x][y]=44;
        }
        saphire = 5;
        for (int i = 0; i < saphire; i++){
            int x = 17 + (int)(Math.random() * ((31 - 17) + 1));
            int y = 120 + (int)(Math.random() * ((175 - 120) + 1));
            grid[x][y]=44;
        }





        //randomly fills map with tree
        int tree = 60 + (int)(Math.random() * ((75 - 60) + 1));
        for (int i = 0; i < tree; i++){
            int x = 2 + (int)(Math.random() * ((16 - 2) + 1));
            int y = 2 + (int)(Math.random() * ((198 - 2) + 1));
            grid[x][y]= 23;
        }
        int tree2 = 60 + (int)(Math.random() * ((75 - 60) + 1));
        for (int i = 0; i < tree2; i++){
            int x = 17 + (int)(Math.random() * ((32 - 17) + 1));
            int y = 2 + (int)(Math.random() * ((198 - 2) + 1));
            grid[x][y]= 23;
        }
        //Wheat randomizing
        int top = 1;
        int lengthIncriment = 8;
        int LI2 = lengthIncriment + 8;

        for (int i = 0; i < 20; i++){
            top = top * -1;
            int x = 0;
            int y = 0;
            lengthIncriment = lengthIncriment + 8;
            LI2 = LI2 + 8;
            if (top == 1){ x = 4 + (int)(Math.random() * ((14 - 4) + 1));}
            else { x = 18 + (int)(Math.random() * ((30 - 18) + 1));}

            y = lengthIncriment + (int)(Math.random() * ((LI2 - lengthIncriment) + 1));



            grid[x][y]= 22;
            int patchSize = 15 + (int)(Math.random() * ((20 - 15) + 1));
            for (int z = 0; z < patchSize; z ++){
                int whichWay = randomPatch[randomPatchPointer];
                if (whichWay == 1){if (i % 3 == 1 || i % 4 == 0 ){grid[x][y-1]= 19; y--; } else{grid[x][y-1]= 21; y--;} }
                if (whichWay == 2){if (i % 3 == 1 || i % 4 == 0){grid[x+1][y]= 19; x++; } else{grid[x+1][y]= 21; x++;} }
                if (whichWay == 3){if (i % 3 == 1 || i % 4 == 0){grid[x][y+1]= 19; y++; } else{grid[x][y+1]= 21; y++;} }
                if (whichWay == 4){if (i % 3 == 1 || i % 4 == 0){grid[x-1][y]= 19; x--; } else{grid[x-1][y]= 21; x--;} }
                if (randomPatchPointer < 26){
                    this.randomPatchPointer++;
                }
                else {this.randomPatchPointer = 0;}
            }

        }
        //Wheat randomizing
        for (int x = 0;x < 34; x++){
            for (int y = 0;y < 200; y++){
                if (grid[x][y] == 19){
                    if (grid[x+1][y] != 19 ){ grid[x+1][y] = 28;}
                    if (grid[x-1][y] != 19 ){ grid[x-1][y] = 28;}
                    if (grid[x][y+1] != 19 ){ grid[x][y+1] = 28;}
                    if (grid[x][y-1] != 19 ){ grid[x][y-1] = 28;}
                }
                if (grid[x][y] == 21){
                    if (grid[x+1][y] != 21 ){ grid[x+1][y] = 28;}
                    if (grid[x-1][y] != 21 ){ grid[x-1][y] = 28;}
                    if (grid[x][y+1] != 21 ){ grid[x][y+1] = 28;}
                    if (grid[x][y-1] != 21 ){ grid[x][y-1] = 28;}
                }
            }

        }
        //prints the borders of the map
        for (int x = 0;x < 34; x++){
            for (int y = 0;y < 200; y++){
                if (x == 0 || x == 32|| x == 33|| x == 1){
                    grid[x][y]= 26;
                }
                if (y == 0 || y == 199|| y == 198|| y == 1){
                    grid[x][y]= 25;

                }
            }
        }
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 19; j++)
            {

                grid[i+11][j+177] = 0;
            }
        }


        //**********Corners*************
        grid[0][0] = 38;
        grid[0][1] = 26;
        grid[1][1] = 38;
        grid[0][199] = 39;
        grid[0][198] = 26;
        grid[1][198] = 39;
        grid[33][0] = 37;
        grid[33][1] = 26;
        grid[32][1] = 37;
        grid[33][199] = 40;
        grid[33][198] = 26;
        grid[32][198] = 40;

        //*********starting settlements**********
        grid[5][8] = 1;
        grid[28][8] = 10;

        grid[3][3] = 45;
        grid[30][3] = 46;

        //**********Labrynth ***********
        grid[11][196] = 39;
        grid[12][196] = 25;
        grid[13][196] = 25;
        grid[14][196] = 25;
        grid[15][196] = 25;
        grid[16][196] = 25;
        grid[17][196] = 25;
        grid[18][196] = 25;
        grid[19][196] = 25;
        grid[20][196] = 25;
        grid[21][196] = 25;
        grid[22][196] = 25;
        grid[23][196] = 40;
        grid[11][177] = 38;
        grid[12][177] = 25;
        grid[13][177] = 25;
        grid[14][177] = 25;
        grid[15][177] = 25;
        grid[16][177] = 25;
        grid[17][177] = 0;
        grid[18][177] = 25;
        grid[19][177] = 25;
        grid[20][177] = 25;
        grid[21][177] = 25;
        grid[22][177] = 25;
        grid[23][177] = 37;

        grid[23][178] = 26;
        grid[23][179] = 26;
        grid[23][180] = 26;
        grid[23][181] = 26;
        grid[23][182] = 26;
        grid[23][183] = 26;
        grid[23][184] = 26;
        grid[23][185] = 26;
        grid[23][186] = 0;
        grid[23][187] = 0;
        grid[23][188] = 26;
        grid[23][189] = 26;
        grid[23][190] = 26;
        grid[23][191] = 26;
        grid[23][192] = 26;
        grid[23][193] = 26;
        grid[23][194] = 26;
        grid[23][195] = 26;

        grid[11][178] = 26;
        grid[11][179] = 26;
        grid[11][180] = 26;
        grid[11][181] = 26;
        grid[11][182] = 26;
        grid[11][183] = 26;
        grid[11][184] = 26;
        grid[11][185] = 26;
        grid[11][186] = 0;
        grid[11][187] = 0;
        grid[11][188] = 26;
        grid[11][189] = 26;
        grid[11][190] = 26;
        grid[11][191] = 26;
        grid[11][192] = 26;
        grid[11][193] = 26;
        grid[11][194] = 26;
        grid[11][195] = 26;


        grid[13][180] = 38;
        grid[14][180] = 25;
        grid[15][180] = 25;
        grid[16][180] = 25;
        grid[17][180] = 25;
        grid[18][180] = 25;
        grid[19][180] = 25;
        grid[20][180] = 25;
        grid[21][180] = 37;

        grid[13][193] = 39;
        grid[14][193] = 0;
        grid[15][193] = 25;
        grid[16][193] = 25;
        grid[17][193] = 0;
        grid[18][193] = 25;
        grid[19][193] = 25;
        grid[20][193] = 0;
        grid[21][193] = 40;

        grid[21][192] = 26;
        grid[21][191] = 26;
        grid[21][190] = 26;
        grid[21][189] = 26;
        grid[21][188] = 26;
        grid[21][187] = 26;
        grid[21][186] = 26;
        grid[21][185] = 26;
        grid[21][184] = 26;
        grid[21][183] = 26;
        grid[21][182] = 26;
        grid[21][181] = 26;

        grid[13][192] = 26;
        grid[13][191] = 26;
        grid[13][190] = 26;
        grid[13][189] = 26;
        grid[13][188] = 26;
        grid[13][187] = 26;
        grid[13][186] = 26;
        grid[13][185] = 26;
        grid[13][184] = 26;
        grid[13][183] = 26;
        grid[13][182] = 26;
        grid[13][181] = 26;


        grid[15][183] = 38;
        grid[16][183] = 25;
        grid[17][183] = 0;
        grid[18][183] = 25;
        grid[19][183] = 37;

        grid[15][190] = 39;
        grid[16][190] = 25;
        grid[17][190] = 25;
        grid[18][190] = 25;
        grid[19][190] = 40;

        grid[19][184] = 26;
        grid[19][185] = 26;
        grid[19][186] = 0;
        grid[19][187] = 0;
        grid[19][188] = 26;
        grid[19][189] = 26;

        grid[15][184] = 26;
        grid[15][185] = 26;
        grid[15][186] = 0;
        grid[15][187] = 0;
        grid[15][188] = 26;
        grid[15][189] = 26;



        //Diamond
        grid[17][186] = 20;






    }

    public void PrintGrid(int[][] map){
        System.out.println("******Turn Number: "+ this.turnsAmount + " ********");
        for (int i = 0;i < 34; i++){
            System.out.println();
            for (int x = 0;x < 200; x++){
                if (map[i][x] == 0){System.out.print(" ");}//Prints empty area
                if (map[i][x] == 1){System.out.print("\u001B[34m" + "A"+ "\033[0m");}
                if (map[i][x] == 2){System.out.print("\u001B[34m" +"B"+ "\033[0m");}
                if (map[i][x] == 3){System.out.print("\u001B[34m" +"C"+ "\033[0m");}
                if (map[i][x] == 4){System.out.print("\u001B[34m" +"Å"+ "\033[0m");}
                if (map[i][x] == 5){System.out.print("\u001B[34m" +"ß"+ "\033[0m");}
                if (map[i][x] == 6){System.out.print("\u001B[34m" +"Ç"+ "\033[0m");}
                if (map[i][x] == 7){System.out.print("\u001B[34m" +"Ä"+ "\033[0m");}
                if (map[i][x] == 8){System.out.print("\u001B[34m" +"฿"+ "\033[0m");}
                if (map[i][x] == 9){System.out.print("\u001B[34m" +"¢"+ "\033[0m");}
                if (map[i][x] == 10){System.out.print("\u001B[31m" + "X"+ "\033[0m");}
                if (map[i][x] == 11){System.out.print("\u001B[31m" +"Y"+ "\033[0m");}
                if (map[i][x] == 12){System.out.print("\u001B[31m" +"Z"+ "\033[0m");}
                if (map[i][x] == 13){System.out.print("\u001B[31m" +"⌘"+ "\033[0m");}
                if (map[i][x] == 14){System.out.print("\u001B[31m" +"¥"+ "\033[0m");}
                if (map[i][x] == 15){System.out.print("\u001B[31m" +"ζ"+ "\033[0m");}
                if (map[i][x] == 16){System.out.print("\u001B[31m" +"✥"+ "\033[0m");}
                if (map[i][x] == 17){System.out.print("\u001B[31m" +"ϒ"+ "\033[0m");}
                if (map[i][x] == 18){System.out.print("\u001B[31m" +"ϟ"+ "\033[0m");}
                if (map[i][x] == 19){System.out.print("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
                if (map[i][x] == 20){System.out.print("\u001B[36m" + "♦" + "\033[0m");}//prints Diamiond
                if (map[i][x] == 21){System.out.print( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
                if (map[i][x] == 22){System.out.print("~");}//prints grass
                if (map[i][x] == 23){System.out.print("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
                if (map[i][x] == 24){System.out.print("[");}//prints vertical line
                if (map[i][x] == 25){System.out.print("║");}//Prints vertical wall
                if (map[i][x] == 26){System.out.print("═");}//prints horizantal wall
                if (map[i][x] == 27){System.out.print("]");}//prints horizantal line
                if (map[i][x] == 28){System.out.print("|");}//prints horizantal line
                if (map[i][x] == 29){System.out.print("·");}//prints soilder
                if (map[i][x] == 30){System.out.print("☭");}//prints worker
                if (map[i][x] == 31){System.out.print("\uD83D\uDC19");}//prints monster
                if (map[i][x] == 32){System.out.print("⛏");}//prints soilder
                if (map[i][x] == 33){System.out.print("•");}//prints playerTwoRoad
                if (map[i][x] == 34){System.out.print("≡");}//prints playerOneRoad
                if (map[i][x] == 35){System.out.print("♖");}//prints Tower
                if (map[i][x] == 36){System.out.print("░");}//prints Reaped land
                if (map[i][x] == 37){System.out.print("╚");}//prints corner
                if (map[i][x] == 38){System.out.print("╔");}//prints corner
                if (map[i][x] == 39){System.out.print("╗");}//prints corner
                if (map[i][x] == 40){System.out.print("╝");}//prints corner
                if (map[i][x] == 41){System.out.print("▌");}//prints Reaped land
                if (map[i][x] == 42){System.out.print("▿");}//prints ruby
                if (map[i][x] == 45){System.out.print("♚");}//prints p1 king
                if (map[i][x] == 46){System.out.print("♔");}//prints p2 king
            }
        }
    }
    public void AddMaterial()
    {
        this.turnsAmount ++;
        this.playerOneStone += 5;
        this.playerTwoStone += 5;
        this.playerOneKingMoves = 16;
        this.playerTwoKingMoves = 16;
        this.pieceMovesOne = 3;
        this.pieceMovesTwo = 3;

    }
    //Change turn basicaly means main menu, doesn't actually change the turn
    public void ChangeTurn(){
        Scanner scan = new Scanner(System.in);
        //*********************************************Player 1 ********************************************* Done
        if(this.turn == 1){
            emptyText();
            emptyBorder();
            PrintGrid(grid);
            borderConcactinate("♣¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯♥");
            stringConcactinate("Player 1 Turn:" + this.turnsAmount + PrintCustomSpaces(0, this.turnsAmount));
            borderConcactinate("│                                                                                      ");
            stringConcactinate("◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◇▩◆▩◇│");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Wheat:" + this.playerOneWheat +  "   Lumber:"+ this.playerOneLumber);
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Stone:" + this.playerOneStone + "   Rubies:"+ this.playerOneRubys);
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Miners:" + this.playerOneSoldiers + "  Pearls:"+ this.playerOnePearls);
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Workers:" + this.playerOneWorkers + " Sapphires:"+ this.playerOneSapphire);
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Settlements:" + this.playerOneCitiesToPlace + PrintCustomSpaces(6, this.playerOneCitiesToPlace));
            borderConcactinate("│                                                                                      ");
            stringConcactinate("◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◇▩◆▩◇│");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("Actions:                                                                                           │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("1) Start new road                                                                                  │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("2) Continue road                                                                                   │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("3) Buy units                                                                                       │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("4) Manage units                                                                                    │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("5) Manage Settlements                                                                                         │");
            borderConcactinate("│                                                                                      ");
            stringConcactinate("◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◇▩◆▩◇│");
            borderConcactinate("│                                                                                      ");
            borderConcactinate("✦¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯♠");
            borderConcactinate("│                                                                                        ");
            borderConcactinate("♥¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯♠");
            stringConcactinate("¯");
            stringConcactinate(" ");
            stringConcactinate("¯");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◇▩◆▩◇│");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("6) End Turn                                                                                        │");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            borderConcactinate("│                                                                                     ");
            borderConcactinate("│                                                                                     ");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("");
            stringConcactinate("");
            borderConcactinate("│                                                                                     ");
            stringConcactinate("7) tutorial and settings                                                                             │");
            stringConcactinate("◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◆▩◇▩◇▩◆▩◇│");
            borderConcactinate("♠¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯♠");
            mainMenu();

        }
        //*********************************************Player 2 ********************************************* Done
        if(this.turn == -1){
            PrintGrid(grid);

            emptyText();
            emptyBorder();
            System.out.println();
            borderConcactinate("☪•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••✯");
            stringConcactinate("Player 2 Turn:" + this.turnsAmount + PrintCustomSpaces(0, this.turnsAmount));
            stringConcactinate("⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⨍▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡•");
            borderConcactinate("•                                            ");
            borderConcactinate("•                                                                            ");
            stringConcactinate("Wheat:" + this.playerTwoWheat + "   Lumber:"+ this.playerTwoLumber);
            borderConcactinate("•                                                                            ");
            stringConcactinate("Stone:" + this.playerTwoStone + "   Rubies:"+ this.playerTwoRubys);
            borderConcactinate("•                                                                            ");
            stringConcactinate("Miners:" + this.playerTwoSoldiers+ "  Pearls:"+ this.playerTwoPearls);
            borderConcactinate("•                                                                            ");
            stringConcactinate("Workers:" + this.playerTwoWorkers + " Sapphires:"+ this.playerTwoSapphire);
            borderConcactinate("•                                                                            ");
            stringConcactinate("Settlements:" + this.playerTwoCitiesToPlace + PrintCustomSpaces(6, this.playerTwoCitiesToPlace));
            borderConcactinate("•                                                                            ");
            stringConcactinate("⨍▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⨍▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡•");
            borderConcactinate("•                                                                            ");
            stringConcactinate("•Actions:                                                                                           •");
            borderConcactinate("•                                                                            ");
            stringConcactinate("1) Start new road                                                                                  •");
            borderConcactinate("•                                                                            ");
            stringConcactinate("2) Continue road                                                                                   •");
            borderConcactinate("•                                                                            ");
            stringConcactinate("3) Buy units                                                                                       •");
            borderConcactinate("•                                                                            ");
            stringConcactinate("4) Manage units                                                                                    •");
            borderConcactinate("•                                                                            ");
            stringConcactinate("5) Manage Settlements                                                                                        •");
            borderConcactinate("•                                                                            ");
            borderConcactinate("•⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⨍▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡•");
            borderConcactinate("✯•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••☪");
            borderConcactinate("•                                                                     " );
            borderConcactinate("☪••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••✯");
            stringConcactinate("");
            stringConcactinate("•");
            stringConcactinate(" ");
            stringConcactinate("•");
            stringConcactinate("⨍▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⨍▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡•");
            borderConcactinate("•                                                                        " );
            stringConcactinate("6) End Turn                                                                                        ");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            borderConcactinate("•                                                                        " );
            borderConcactinate("•                                                                        " );
            borderConcactinate("•                                                                        " );
            borderConcactinate("•                                                                        " );
            stringConcactinate("");
            stringConcactinate("7) tutorial and settings                                                                             ");
            stringConcactinate("•⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⨍▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡▾≈≈≈≈≈≈▴⅀▾≈≈≈≈≈≈▴⟠▾≈≈≈≈≈≈▴◦▾≈≈≈≈≈≈▴✡•");
            borderConcactinate("✯•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••☪");
           mainMenu();


        }


    }
    public void ViewData(){
        System.out.println("Player one Pieces");
        for (int i = 0; i < this.playerOnePieces.length; i++) {
            for (int x = 0; x < 6; x ++){
                System.out.print(this.playerOnePieces[i][x] + " ");
            }
            System.out.println();
        }
        System.out.println("Player one Cities Array");
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 6; x ++){
                System.out.print(this.playerOneCities[i][x] + " ");
            }
            System.out.println();
        }
        System.out.println("Player one Nodes Array");
        for (int i = 0; i < 36; i++) {
            for (int x = 0; x < 5; x ++){
                System.out.print(this.playerOneNodeTracker[i][x] + " ");
            }
            System.out.println();
        }
        System.out.println("Player two Cities Array");
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 6; x ++){
                System.out.print(this.playerTwoCities[i][x] + " ");
            }
            System.out.println();
        }
        System.out.println("Player two Nodes Array");
        for (int i = 0; i < 36; i++) {
            for (int x = 0; x < 5; x ++){
                System.out.print(this.playerTwoNodeTracker[i][x] + " ");
            }
            System.out.println();
        }
        System.out.println("Player two Pieces");
        for (int i = 0; i < this.playerTwoPieces.length; i++) {
            for (int x = 0; x < 6; x ++){
                System.out.print(this.playerTwoPieces[i][x] + " ");
            }
            System.out.println();
        }
    }
    //This method starts a new road on a settlement
    public void StartRoad(){
        //*********************************************Player 1 ********************************************* Done
        if (this.turn == 1) {
            emptyText();
            stringConcactinate("Choose a settlement");
            stringConcactinate(" to place a new road.");
            int list = 0;
            for (int i = 0; i < 9; i++) {
                if (this.playerOneCities[i][0] > 0){
                    list ++;
                    stringConcactinate(i + 1 + ") Settlement " + printSpecialCharacter(this.playerOneCities[i][0]));
                    printSpecialCharacter(this.playerOneCities[i][0]);


                }
                else {i = 10;}
            }
            System.out.println("0) back");
            stringConcactinate("0) back");
            //try {}
            int settlement = trycatch(false,2,list + 1,0, false, null, 0);
            int[] cordinates = FindNode(settlement);
            if (settlement == 0){return;}
            else {if (cordinates[0] + cordinates[1] == 0){
                stringConcactinate("Thats not a valid answer");
                return;
            }
            else {
                emptyText();
                int[] roadTracker = new int[5];
                int list2 = 0;
                System.out.println("Do you want to build... ");
                stringConcactinate("Do you want to build... ");
                if (this.playerOneCities[settlement - 1][1] == 0){list2 ++; stringConcactinate(list2 +") Right"); roadTracker[list2] = 1;}
                if (this.playerOneCities[settlement - 1][2] == 0){list2 ++;  stringConcactinate(list2 +") Down"); roadTracker[list2] = 2;}
                if (this.playerOneCities[settlement - 1][3] == 0){list2 ++;  stringConcactinate(list2 +") Left"); roadTracker[list2] = 3;}
                if (this.playerOneCities[settlement - 1][4] == 0){list2 ++; stringConcactinate(list2 +") Up"); roadTracker[list2] = 4;}
                stringConcactinate("0) back");
                int direction = trycatch(false,1,list2+ 1,0, false, null, 0);
                if(direction > 4){System.out.println("Invalid Number"); stringConcactinate("Invalid Number"); return; }
                //if (this.playerOneCities[settlement - 1][1] == 1 && direction == 1){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
               // if (this.playerOneCities[settlement - 1][2] == 1 && direction == 2){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
               // if (this.playerOneCities[settlement - 1][3] == 1 && direction == 3){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                //if (this.playerOneCities[settlement - 1][4] == 1 && direction == 4){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                if (direction== 0){return;}
                else {
                    emptyText();
                    stringConcactinate("How much road do you ");
                    stringConcactinate("want to build?");
                    stringConcactinate("(1 road cost 1 stone)");
                    int length = trycatch(false,0,0,0, false, null, 0);
                    this.playerOneCities[settlement - 1][roadTracker[direction]] = 1;

                    int node = 0;
                    for (int j = 0; j < 36; j++){
                        if (this.playerOneNodeTracker[j][0]== 0)
                        {
                            node = j;
                            j = 36;
                        }}
                    this.playerOneNodeTracker[node][3] = settlement;
                    this.playerOneNodeTracker[node][5] = roadTracker[direction];
                    BuildRoad(cordinates[0], cordinates[1], length, roadTracker[direction],settlement, node);

                }
            }
            }
        }
        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1) {

            emptyText();
            stringConcactinate("Choose a settlement");
            stringConcactinate(" to place a new road.");
            int list = 0;
            int[] roadTracker = new int[5];
            for (int i = 0; i < 9; i++) {
                if (this.playerTwoCities[i][0] > 0){

                    printSpecialCharacter(this.playerTwoCities[i][0] + 9);
                    stringConcactinate(i + 1 + ") Settlement " + printSpecialCharacter(this.playerTwoCities[i][0] + 9));
                    list ++;

                }
                else {i = 10;}
            }
            System.out.println("0) back");
            stringConcactinate("0) back");
            int settlement = trycatch(false,2,list + 1,0, false, null, 0);
            int[] cordinates = FindNode(settlement + 9);
            if (settlement == 0){return;}
            else {if (cordinates[0] + cordinates[1] == 0){
                System.out.println("Thats not a valid answer");
                stringConcactinate("Thats not a valid answer");
                StartRoad();
            }
            else {
                emptyText();
                int list2 = 0;
                stringConcactinate("Do you want to build... ");
                if (this.playerTwoCities[settlement - 1][1] == 0){list2++; stringConcactinate(list2 +") Right");roadTracker[list2] = 1;}
                if (this.playerTwoCities[settlement - 1][2] == 0){list2++;  stringConcactinate(list2 +") Down");roadTracker[list2] = 2;}
                if (this.playerTwoCities[settlement - 1][3] == 0){list2++;  stringConcactinate(list2 +") Left");roadTracker[list2] = 3;}
                if (this.playerTwoCities[settlement - 1][4] == 0){list2++;  stringConcactinate(list2 + ") Up");roadTracker[list2] = 4;}
                System.out.println("0) back");
                int direction = trycatch(false,1,list2 + 1,0, false, null, 0);
                if(direction > 4){return;}
                //if (this.playerTwoCities[settlement - 1][1] == 1 && direction == 1){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                //if (this.playerTwoCities[settlement - 1][2] == 1 && direction == 2){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                //if (this.playerTwoCities[settlement - 1][3] == 1 && direction == 3){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                //if (this.playerTwoCities[settlement - 1][4] == 1 && direction == 4){System.out.println("Invalid Number");ChangeTurn();stringConcactinate("Invalid Number");}
                if (direction == 0){return;}
                else {
                    emptyText();
                    stringConcactinate("How much road do you ");
                    stringConcactinate("want to build?");
                    stringConcactinate("(1 road cost 1 stone)");
                    int length = trycatch(false,0,0,0, false, null, 0);
                    this.playerTwoCities[settlement - 1][roadTracker[direction]] = 1;
                    int node = 0;
                    for (int j = 0; j < 36; j++){
                        if (this.playerTwoNodeTracker[j][0]== 0)
                        {
                            node = j;
                            j = 36;
                        }}
                    this.playerTwoNodeTracker[node][3]= settlement;
                    this.playerTwoNodeTracker[node][5] = roadTracker[direction];
                    BuildRoad(cordinates[0], cordinates[1], length, roadTracker[direction],settlement, node);

                }
            }
            }
        }
    }


    //This method uses the cordinates of a city in connjuntion with a direction and length to build a new road attached to a settlement.
    public void BuildRoad(int xPos, int yPos, int length, int direction,int settlement, int node){
        if (length == 0){return;}
        int roadLength = 1;
        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1){
            for (int i = 0;length > i; i++) {
                if (this.playerOneStone > 0)
                {if (direction == 1){
                    if (this.grid[xPos][yPos+ roadLength] == 0 || this.grid[xPos][yPos+ roadLength] == 22
                            || this.grid[xPos][yPos+ roadLength] == 23|| this.grid[xPos][yPos+ roadLength] == 34)
                    {
                        this.grid[xPos][yPos + roadLength] = 34;
                        roadLength++;
                        this.playerOneStone --;
                    }
                }
                    if (direction == 2){
                        if (this.grid[xPos+ roadLength][yPos] == 0 || this.grid[xPos+ roadLength][yPos] == 22
                                || this.grid[xPos+ roadLength][yPos] == 23|| this.grid[xPos+ roadLength][yPos] == 34) {
                            this.grid[xPos + roadLength][yPos] = 34;
                            roadLength++;
                            this.playerOneStone --;
                        }
                    }
                    if (direction == 3){
                        if(this.grid[xPos][yPos- roadLength] == 0 || this.grid[xPos][yPos- roadLength] == 22
                                || this.grid[xPos][yPos- roadLength] == 23 || this.grid[xPos][yPos- roadLength] == 34) {
                            this.grid[xPos][yPos - roadLength] = 34;
                            roadLength++;
                            this.playerOneStone --;
                        }
                    }
                    if (direction == 4){
                        if (this.grid[xPos- roadLength][yPos] == 0 || this.grid[xPos- roadLength][yPos] == 22
                                || this.grid[xPos-roadLength][yPos] == 23|| this.grid[xPos-roadLength][yPos] == 34) {
                            this.grid[xPos - roadLength][yPos] = 34;
                            roadLength++;
                            this.playerOneStone --;
                        }
                    }
                }

            }
            roadLength = roadLength - 1;
            if (direction == 1){this.playerOneNodeTracker[node][0]=1; this.playerOneNodeTracker[node][1] = xPos; this.playerOneNodeTracker[node][2] = yPos + roadLength;}
            if (direction == 2){this.playerOneNodeTracker[node][0]=2; this.playerOneNodeTracker[node][1] = xPos + roadLength; this.playerOneNodeTracker[node][2] = yPos;}
            if (direction == 3){this.playerOneNodeTracker[node][0]=3; this.playerOneNodeTracker[node][1] = xPos; this.playerOneNodeTracker[node][2] = yPos - roadLength;}
            if (direction == 4){this.playerOneNodeTracker[node][0]=4; this.playerOneNodeTracker[node][1] = xPos - roadLength; this.playerOneNodeTracker[node][2] = yPos;}

        }
        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1){
            for (int i = 0;length > i; i++) {
                if (this.playerTwoStone > 0)
                {if (direction == 1){
                    if (this.grid[xPos][yPos+ roadLength] == 0 || this.grid[xPos][yPos+ roadLength] == 22
                            || this.grid[xPos][yPos+ roadLength] == 23|| this.grid[xPos][yPos+ roadLength] == 33)
                    {
                        this.grid[xPos][yPos + roadLength] = 33;
                        roadLength++;
                        this.playerTwoStone --;
                    }
                }
                    if (direction == 2){
                        if (this.grid[xPos+ roadLength][yPos] == 0 || this.grid[xPos+ roadLength][yPos] == 22
                                || this.grid[xPos+ roadLength][yPos] == 23|| this.grid[xPos+ roadLength][yPos] == 33) {
                            this.grid[xPos + roadLength][yPos] = 33;
                            roadLength++;
                            this.playerTwoStone --;
                        }
                    }
                    if (direction == 3){
                        if(this.grid[xPos][yPos- roadLength] == 0 || this.grid[xPos][yPos- roadLength] == 22
                                || this.grid[xPos][yPos- roadLength] == 23 || this.grid[xPos][yPos- roadLength] == 33) {
                            this.grid[xPos][yPos - roadLength] = 33;
                            roadLength++;
                            this.playerTwoStone --;
                        }
                    }
                    if (direction == 4){
                        if (this.grid[xPos- roadLength][yPos] == 0 || this.grid[xPos- roadLength][yPos] == 22
                                || this.grid[xPos-roadLength][yPos] == 23|| this.grid[xPos-roadLength][yPos] == 33) {
                            this.grid[xPos - roadLength][yPos] = 33;
                            roadLength++;
                            this.playerTwoStone --;
                        }
                    }
                }

            }
            roadLength = roadLength - 1;
            if (direction == 1){this.playerTwoNodeTracker[node][0]=1; this.playerTwoNodeTracker[node][1] = xPos; this.playerTwoNodeTracker[node][2] = yPos + roadLength;}
            if (direction == 2){this.playerTwoNodeTracker[node][0]=2; this.playerTwoNodeTracker[node][1] = xPos + roadLength; this.playerTwoNodeTracker[node][2] = yPos;}
            if (direction == 3){this.playerTwoNodeTracker[node][0]=3; this.playerTwoNodeTracker[node][1] = xPos; this.playerTwoNodeTracker[node][2] = yPos - roadLength;}
            if (direction == 4){this.playerTwoNodeTracker[node][0]=4; this.playerTwoNodeTracker[node][1] = xPos - roadLength; this.playerTwoNodeTracker[node][2] = yPos;}

        }
    }
    //This method initiates the actions of the players units
    public void UnitActions()
    {
        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1)
        {
            for (int i = 0; i < 36; i++)
            {
                //worker
                if (this.playerOneNodeTracker[i][4] == 1 ){
                    if (this.playerOneWorkerNodeTracker[i][0] == 0){this.playerOneWorkerNodeTracker[i][0] =this.playerOneNodeTracker[i][1];
                    this.playerOneWorkerNodeTracker[i][1] =this.playerOneNodeTracker[i][2];}
                    int mineral = Mine(i,this.playerOneWorkerNodeTracker[i][0],this.playerOneWorkerNodeTracker[i][1], 0);
                    if (mineral == 1){this.playerOneStone = this.playerOneStone + 5;}
                    if (mineral == 2){this.playerOneWheat = this.playerOneWheat + 2;}
                    if (mineral == 3){Mine(i,this.playerOneWorkerNodeTracker[i][0],this.playerOneWorkerNodeTracker[i][1], 0);}
                    System.out.println(mineral);

                }
                //soilder
                if (this.playerOneNodeTracker[i][4] == 2 ){
                    attack(i, this.playerOneNodeTracker[i][1],this.playerOneNodeTracker[i][2]);
                }

            }
        }
        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1)
        {
            for (int i = 0; i < 36; i++)
            {
                //worker
                if (this.playerTwoNodeTracker[i][4] == 1 ){
                    if (this.playerTwoWorkerNodeTracker[i][0] == 0){this.playerTwoWorkerNodeTracker[i][0] =this.playerTwoNodeTracker[i][1];
                    this.playerTwoWorkerNodeTracker[i][1] =this.playerTwoNodeTracker[i][2];}
                    int mineral = Mine(i,this.playerTwoWorkerNodeTracker[i][0],this.playerTwoWorkerNodeTracker[i][1], 0);
                    if (mineral == 1){this.playerTwoStone = this.playerTwoStone + 5;}
                    if (mineral == 2){this.playerTwoWheat = this.playerTwoWheat + 2;}
                    if (mineral == 3){Mine(i,this.playerTwoWorkerNodeTracker[i][0],this.playerTwoWorkerNodeTracker[i][1], 0);}
                    System.out.println(mineral);

                }
                //soilder
                if (this.playerTwoNodeTracker[i][4] == 2 ){
                    attack(i, this.playerTwoNodeTracker[i][1],this.playerTwoNodeTracker[i][2]);
                }

            }
        }
    }
    public void attack(int node, int xPos, int yPos)
    {


        if (this.turn == 1) {

            if (this.grid[xPos + 1][yPos - 1] == 33){this.grid[xPos + 1][yPos - 1] = 0;}
            if (this.grid[xPos + 1][yPos] == 33){this.grid[xPos + 1][yPos] = 0;}
            if (this.grid[xPos + 1][yPos + 1] == 33){this.grid[xPos + 1][yPos +1] = 0;}

            if (this.grid[xPos][yPos - 1] == 33){this.grid[xPos][yPos-1] = 0;}
            if (this.grid[xPos][yPos + 1] == 33){this.grid[xPos][yPos+1] = 0;}

            if (this.grid[xPos - 1][yPos - 1] == 33){this.grid[xPos - 1][yPos - 1] = 0;}
            if (this.grid[xPos - 1][yPos] == 33){this.grid[xPos - 1][yPos] = 0;}
            if (this.grid[xPos - 1][yPos + 1] == 33){this.grid[xPos - 1][yPos + 1] = 0;}

        }
        if (this.turn == -1) {

            if (this.grid[xPos + 1][yPos - 1] == 34){this.grid[xPos + 1][yPos - 1] = 0;}
            if (this.grid[xPos + 1][yPos] == 34){this.grid[xPos + 1][yPos] = 0;}
            if (this.grid[xPos + 1][yPos + 1] == 34){this.grid[xPos + 1][yPos + 1] = 0;}

            if (this.grid[xPos][yPos - 1] == 34){this.grid[xPos][yPos - 1] = 0;}
            if (this.grid[xPos][yPos + 1] == 34){this.grid[xPos][yPos + 1] = 0;}

            if (this.grid[xPos - 1][yPos - 1] == 34){this.grid[xPos - 1][yPos - 1] = 0;}
            if (this.grid[xPos - 1][yPos] == 34){this.grid[xPos - 1][yPos] = 0;}
            if (this.grid[xPos - 1][yPos + 1] == 34){this.grid[xPos - 1][yPos + 1] = 0;}

        }

    }


    // returns 0 if nothing found, returns 3 for horizantal line, returns 1 for stone, returns 23 for wheat.
    public int Mine(int node, int xPos, int yPos, int recursion)
    {

        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1)
        {

            if (this.grid[xPos + 1][yPos - 1] == 19){this.grid[xPos + 1][yPos - 1] = 36; this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1;return 1;}
            if (this.grid[xPos + 1][yPos] == 19){this.grid[xPos + 1][yPos] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos + 1][yPos + 1] == 19){this.grid[xPos + 1][yPos + 1]= 36;this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 1;}
            if (this.grid[xPos][yPos - 1] == 19){this.grid[xPos][yPos - 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 1;}
            if (this.grid[xPos][yPos] == 19){this.grid[xPos][yPos]= 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos][yPos + 1] == 19){this.grid[xPos][yPos + 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 1;}
            if (this.grid[xPos - 1][yPos - 1] == 19){this.grid[xPos - 1][yPos - 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 1;}
            if (this.grid[xPos - 1][yPos] == 19){this.grid[xPos - 1][yPos] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos - 1][yPos + 1] == 19){this.grid[xPos - 1][yPos + 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 1;}

            if (this.grid[xPos + 1][yPos - 1] == 21){this.grid[xPos + 1][yPos - 1] = 36; this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1;return 2;}
            if (this.grid[xPos + 1][yPos] == 21){this.grid[xPos + 1][yPos] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos + 1][yPos + 1] == 21){this.grid[xPos + 1][yPos + 1]= 36;this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 2;}
            if (this.grid[xPos][yPos - 1] == 21){this.grid[xPos][yPos - 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 2;}
            if (this.grid[xPos][yPos] == 21){this.grid[xPos][yPos]= 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos][yPos + 1] == 21){this.grid[xPos][yPos + 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 2;}
            if (this.grid[xPos - 1][yPos - 1] == 21){this.grid[xPos - 1][yPos - 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 2;}
            if (this.grid[xPos - 1][yPos] == 21){this.grid[xPos - 1][yPos] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos - 1][yPos + 1] == 21){this.grid[xPos - 1][yPos + 1] = 36;this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 2;}

            if (this.grid[xPos + 1][yPos - 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos + 1][yPos] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos + 1][yPos + 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos + 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1;return 3;}
            if (this.grid[xPos][yPos - 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos][yPos] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos][yPos + 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 3;}
            if (this.grid[xPos - 1][yPos - 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos - 1][yPos] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos - 1][yPos + 1] == 28){this.playerOneWorkerNodeTracker[node][0] = xPos - 1; this.playerOneWorkerNodeTracker[node][1] = yPos + 1; return 3;}


        }

        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1)
        {
            if (this.grid[xPos + 1][yPos - 1] == 19){this.grid[xPos + 1][yPos - 1] = 36; this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1;return 1;}
            if (this.grid[xPos + 1][yPos] == 19){this.grid[xPos + 1][yPos] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos + 1][yPos + 1] == 19){this.grid[xPos + 1][yPos + 1]= 36;this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 1;}
            if (this.grid[xPos][yPos - 1] == 19){this.grid[xPos][yPos - 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 1;}
            if (this.grid[xPos][yPos] == 19){this.grid[xPos][yPos]= 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos][yPos + 1] == 19){this.grid[xPos][yPos + 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 1;}
            if (this.grid[xPos - 1][yPos - 1] == 19){this.grid[xPos - 1][yPos - 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 1;}
            if (this.grid[xPos - 1][yPos] == 19){this.grid[xPos - 1][yPos] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 1;}
            if (this.grid[xPos - 1][yPos + 1] == 19){this.grid[xPos - 1][yPos + 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 1;}

            if (this.grid[xPos + 1][yPos - 1] == 21){this.grid[xPos + 1][yPos - 1] = 36; this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1;return 2;}
            if (this.grid[xPos + 1][yPos] == 21){this.grid[xPos + 1][yPos] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos + 1][yPos + 1] == 21){this.grid[xPos + 1][yPos + 1]= 36;this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 2;}
            if (this.grid[xPos][yPos - 1] == 21){this.grid[xPos][yPos - 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 2;}
            if (this.grid[xPos][yPos] == 21){this.grid[xPos][yPos]= 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos][yPos + 1] == 21){this.grid[xPos][yPos + 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 2;}
            if (this.grid[xPos - 1][yPos - 1] == 21){this.grid[xPos - 1][yPos - 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 2;}
            if (this.grid[xPos - 1][yPos] == 21){this.grid[xPos - 1][yPos] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 2;}
            if (this.grid[xPos - 1][yPos + 1] == 21){this.grid[xPos - 1][yPos + 1] = 36;this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 2;}



            if (this.grid[xPos + 1][yPos - 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos + 1][yPos] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos + 1][yPos + 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos + 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1;return 3;}
            if (this.grid[xPos][yPos - 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos][yPos] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos][yPos + 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 3;}
            if (this.grid[xPos - 1][yPos - 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos - 1; return 3;}
            if (this.grid[xPos - 1][yPos] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos; return 3;}
            if (this.grid[xPos - 1][yPos + 1] == 28){this.playerTwoWorkerNodeTracker[node][0] = xPos - 1; this.playerTwoWorkerNodeTracker[node][1] = yPos + 1; return 3;}

        }

        return 0;

    }
    public void ContinueRoad(){
        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1)
        {
            this.display.emptyText(textGraphics);
            emptyText();
            int[] nodeTracker = new int[36];
            stringConcactinate("Choose a Road ");
            stringConcactinate("to Continue....");
            int list= 1;
            for (int i = 0; i < 36; i++)
            {
                if (this.playerOneNodeTracker[i][0] > 0 && this.playerOneNodeTracker[i][4] == 0){

                    System.out.print(list + ") Settlement "); printSpecialCharacter(this.playerOneNodeTracker[i][3]);
                    stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerOneNodeTracker[i][3]) +  printSpecialCharacter(this.playerOneNodeTracker[i][5] + 18));
                    printSpecialCharacter(this.playerOneNodeTracker[i][0] + 18);
                    System.out.println();
                    list = list + 1;
                    nodeTracker[list - 1] = i;
                }
            }
            stringConcactinate("0) Back");

            int node = trycatch(false,2,list,0, false, null, 0);
            if (node == 0){return;}
            if (node > list){System.out.println("Invalid Answer"); ContinueRoad();  stringConcactinate("Invalid Answer");}
            else {
                emptyText();
                int list2 = 0;
                int[] roadTracker = new int[4];
                stringConcactinate("Do you want to build... ");
                if (this.playerOneNodeTracker[nodeTracker[node]][0] !=3){ list2 ++; stringConcactinate("1) Right"); roadTracker[list2] = 1; }
                if (this.playerOneNodeTracker[nodeTracker[node]][0] !=4){list2 ++; stringConcactinate("2) Down"); roadTracker[list2] = 2;}
                if (this.playerOneNodeTracker[nodeTracker[node]][0] !=1){list2 ++; stringConcactinate("3) Left"); roadTracker[list2] = 3;}
                if (this.playerOneNodeTracker[nodeTracker[node]][0] !=2){list2 ++; stringConcactinate("4) Up"); roadTracker[list2] = 4;}
                stringConcactinate("0) Back");
                int direction = trycatch(false,1,list2 + 1,0, false, null, 0);
                if(direction > 4){System.out.println("Invalid Number"); ContinueRoad(); stringConcactinate("Invalid Number");}
                if (direction == 0){return;}
                else {
                    emptyText();
                    stringConcactinate("How much road do you ");
                    stringConcactinate("want to build?");
                    stringConcactinate("(1 road cost 1 stone)");
                    int length = trycatch(false,0,0,0, false, null, 0);
                    BuildRoad(this.playerOneNodeTracker[nodeTracker[node]][1], this.playerOneNodeTracker[nodeTracker[node]][2], length, roadTracker[direction] ,this.playerOneNodeTracker[node][3], nodeTracker[node]);

                }

            }
        }
        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1)
        {
            this.display.emptyText(textGraphics);
            int[] nodeTracker = new int[36];
            emptyText();
            stringConcactinate("Choose a Road ");
            stringConcactinate("to Continue....");
            int list= 1;
            for (int i = 0; i < 36; i++)
            {
                if (this.playerTwoNodeTracker[i][0] > 0 && this.playerTwoNodeTracker[i][4] == 0){

                    System.out.print(list + ") Settlement "); printSpecialCharacter(this.playerTwoNodeTracker[i][3]+9);
                    stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerTwoNodeTracker[i][3]+9) + printSpecialCharacter(this.playerTwoNodeTracker[i][5] + 18));
                    System.out.println();
                    list = list + 1;
                    nodeTracker[list - 1] = i;
                }
            }
            stringConcactinate("0) Back");

            int node = trycatch(false,2,list + 1,0, false, null, 0);
            if (node == 0){return;}
            if (node > list){stringConcactinate("Invalid Answer"); ContinueRoad();}
            else {
                emptyText();
                int list2 = 0;
                int[] roadTracker = new int[4];
                stringConcactinate("Do you want to build... ");
                if (this.playerTwoNodeTracker[nodeTracker[node]][0] !=3){list2++; stringConcactinate("1) Right"); roadTracker[list2] = 1;}
                if (this.playerTwoNodeTracker[nodeTracker[node]][0] !=4){list2++; stringConcactinate("2) Down"); roadTracker[list2] = 2;}
                if (this.playerTwoNodeTracker[nodeTracker[node]][0] !=1){list2++; stringConcactinate("3) Left"); roadTracker[list2] = 3;}
                if (this.playerTwoNodeTracker[nodeTracker[node]][0] !=2){list2++; stringConcactinate("4) Up"); roadTracker[list2] = 4;}
                stringConcactinate("0) Back");
                int direction = trycatch(false,1,list2+1,0, false, null, 0);
                if(direction > 4){stringConcactinate("Invalid Number"); ContinueRoad(); }
                if (direction == 0){return;}
                else {
                    emptyText();
                    stringConcactinate("How much road do you ");
                    stringConcactinate("want to build?");
                    stringConcactinate("(1 road cost 1 stone)");
                    int length = trycatch(false,0,0,0, false, null, 0);
                    BuildRoad(this.playerTwoNodeTracker[nodeTracker[node]][1], this.playerTwoNodeTracker[nodeTracker[node]][2], length, roadTracker[direction] ,this.playerTwoNodeTracker[node][3], nodeTracker[node]);
                }
            }
        }
    }
    public void BuyMenu()
    {
        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1)
        {
            emptyText();
            stringConcactinate("1) Civil Units ☭");
            stringConcactinate("2) Army Units ♞");
            stringConcactinate("0) Back");
            int civilOrArmy = trycatch(false,0,3,0, false, null, 0);
            if (civilOrArmy == 1)
            {
                //this.display.emptyText(textGraphics);
                emptyText();
                stringConcactinate("1) Worker:☭");
                stringConcactinate("   3 Wheat");
                stringConcactinate("   3 Lumber");

                stringConcactinate("2) RoadBuster:⛏");
                stringConcactinate("   5 Wheat");
                stringConcactinate("   5 Stone");

                stringConcactinate("3) Settlement: A-C");
                stringConcactinate("   5 Lumber 5 Wheat");
                stringConcactinate("   10 Stone");
                stringConcactinate("0) Back");
                int menu = trycatch(false,-2,4,2, false, null, 0);
                if (menu == 0){return;}
                if (menu == 1){
                    if (canAfford(0,3,0, 3,0,0)) {
                        this.playerOneWorkers ++;
                        this.playerOneWheat -= 3;
                        this.playerOneLumber -= 3;
                        this.display.customInput(this.textGraphics, this.screen, 1);


                    }
                    else { this.display.customInput(this.textGraphics, this.screen, 4);}
                }
                if (menu == 2){
                    if (canAfford(5,5,0, 0,0,0)) {
                        this.playerOneSoldiers ++;
                        this.playerOneStone -= 5;
                        this.playerOneWheat -= 5;
                        this.display.customInput(this.textGraphics, this.screen, 2);

                    }
                    else { this.display.customInput(this.textGraphics, this.screen, 4);}
                }
                if (menu == 3){
                    if (canAfford(10,5,0, 5,0,0) && this.playerOneCitiesToPlace == 0){
                        this.playerOneWheat -= 5;
                        this.playerOneLumber -= 5;
                        this.playerOneStone -= 10;
                        this.playerOneCitiesAmount = this.playerOneCitiesAmount + 1;
                        this.playerOneCitiesToPlace = this.playerOneCitiesToPlace + 1;
                        for (int i = 0; i < 9; i++)
                        {
                            if (this.playerOneCities[i][0] == 0){this.playerOneCities[i][0] = this.playerOneCitiesAmount; i = 9;}
                        }
                        this.display.customInput(this.textGraphics, this.screen, 3);

                    }
                    else{ this.display.customInput(this.textGraphics, this.screen, 4);}
                    if (this.playerOneCitiesToPlace > 1){this.display.customInput(this.textGraphics, this.screen, 5);}

                }

                if (menu > 3){ return;}
            }
            if (civilOrArmy == 2)
            {
                emptyText();
                stringConcactinate("1)♟Pawn: " );
                stringConcactinate("  4 wheat 6 wood");

                stringConcactinate("2)♞Knight:");
                stringConcactinate("  6 wheat  2 ruby");

                stringConcactinate("3)♝Bishop:");
                stringConcactinate("  9 wood 2 ruby");

                stringConcactinate("4)♜Rook: ");
                stringConcactinate("  20 stone  3 pearl");

                stringConcactinate("5)♛Queen:" );
                stringConcactinate("  20 lumber 2 sapphire");

                stringConcactinate( "0) Back");

                int piecePurchase = trycatch(false,-1,6,1, false, null, 0);
                if (piecePurchase == 0){ return;}
                if (piecePurchase == 1){ if(canAfford(0,4,0,6,0,0)){this.playerOneWheat -= 4; this.playerOneLumber -= 6; addPiece(1,1);
                    this.display.customInput(this.textGraphics, this.screen, 6);}
                else {this.display.customInput(this.textGraphics, this.screen, 4);} }
                if (piecePurchase == 2){ if(canAfford(0,5,2,0,0,0)){this.playerOneWheat -= 6; this.playerOneRubys -= 2;addPiece(2,1);
                    this.display.customInput(this.textGraphics, this.screen, 7);}
                else {this.display.customInput(this.textGraphics, this.screen, 4);}}
                if (piecePurchase == 3){ if(canAfford(0,0,2,5,0,0)){this.playerOneLumber -= 9; this.playerOneRubys -= 2;addPiece(3,1);
                    this.display.customInput(this.textGraphics, this.screen, 8);}
                else {this.display.customInput(this.textGraphics, this.screen, 4);}}
                if (piecePurchase == 4){ if(canAfford(20,0,0,0,2,0)){this.playerOneStone -= 20; this.playerOnePearls -= 2;addPiece(4,1);
                    this.display.customInput(this.textGraphics, this.screen, 9);}
                else {this.display.customInput(this.textGraphics, this.screen, 4);}}
                if (piecePurchase == 5){ if(canAfford(0,0,0,20,0,2)){this.playerOneLumber -= 20;this.playerOneSapphire -= 2;addPiece(5,1);
                    this.display.customInput(this.textGraphics, this.screen, 10);}
                else {this.display.customInput(this.textGraphics, this.screen, 4);}}


            }

        }
        //*********************************************Player 2 ********************************************* done
        if (this.turn == -1)
        {
            emptyText();
            stringConcactinate("1) Civil Units ☭");
            stringConcactinate("2) Army Units ♞");
            stringConcactinate("0) Back");
            int civilOrArmy = trycatch(false,0,3,0, false, null, 0);
            if (civilOrArmy == 1)
            {
                this.display.emptyText(textGraphics);
                emptyText();
                stringConcactinate("1) Worker: ☭");
                stringConcactinate("   3 Wheat");
                stringConcactinate("   3 Lumber");

                stringConcactinate("2) RoadBuster: ⛏");
                stringConcactinate("   5 Wheat");
                stringConcactinate("   5 Stone");

                stringConcactinate("3) Settlement: X-Z");
                stringConcactinate("   5 Lumber 5 Wheat");
                stringConcactinate("   10 Stone");
                stringConcactinate("0) Back");
                int menu = trycatch(false,-2,4, 2, false, null, 0);
                if (menu == 0){return;}
                if (menu == 1){
                    if (canAfford(0,3,0, 3,0,0)) {
                        this.playerTwoWorkers ++;
                        this.playerTwoWheat -= 3;
                        this.playerTwoLumber -= 3;
                        this.display.customInput(this.textGraphics, this.screen, 1);


                    }
                    else { this.display.customInput(this.textGraphics, this.screen, 4);}
                }
                if (menu == 2){
                    if (canAfford(5,5,0, 0,0,0)) {
                        this.playerTwoSoldiers ++;
                        this.playerTwoStone -= 5;
                        this.playerTwoWheat -= 5;
                        this.display.customInput(this.textGraphics, this.screen, 2);

                    }
                    else { this.display.customInput(this.textGraphics, this.screen, 4);}
                }
                if (menu == 3){
                    if (canAfford(10,5,0, 5,0,0) && this.playerTwoCitiesToPlace == 0){
                        this.playerTwoWheat -= 5;
                        this.playerTwoLumber -= 5;
                        this.playerTwoStone -= 10;
                        this.playerTwoCitiesAmount = this.playerTwoCitiesAmount + 1;
                        this.playerTwoCitiesToPlace = this.playerTwoCitiesToPlace + 1;
                        for (int i = 0; i < 9; i++)
                        {
                            if (this.playerTwoCities[i][0] == 0){this.playerTwoCities[i][0] = this.playerTwoCitiesAmount; i = 9;}
                        }
                        this.display.customInput(this.textGraphics, this.screen, 3);

                    }
                    else{ this.display.customInput(this.textGraphics, this.screen, 4);}
                    if (this.playerTwoCitiesToPlace > 1){this.display.customInput(this.textGraphics, this.screen, 5);}

                }

                if (menu > 3){ return;}
            }
            if (civilOrArmy == 2)
            {
                emptyText();
                stringConcactinate("1)♟Pawn: " );
                stringConcactinate("  4 wheat 6 wood");

                stringConcactinate("2)♞Knight:");
                stringConcactinate("  6 wheat  2 ruby");

                stringConcactinate("3)♝Bishop:");
                stringConcactinate("  9 wood 2 ruby");

                stringConcactinate("4)♜Rook: ");
                stringConcactinate("  20 stone  2 pearl");

                stringConcactinate("5)♛Queen:" );
                stringConcactinate("  20 lumber 2 sapphire");

                stringConcactinate( "0) Back");


                int piecePurchase = trycatch(false,-1,6, 1, false, null, 0);
                if (piecePurchase == 0){ return;}
                if (piecePurchase == 1){ if(canAfford(0,4,0,6,0,0)){this.playerTwoWheat -= 4; this.playerTwoLumber -= 6; addPiece(1,2);
                    this.display.customInput(this.textGraphics, this.screen, 6);}
                 }
                if (piecePurchase == 2){ if(canAfford(0,6,2,0,0,0)){this.playerTwoWheat -= 6; this.playerTwoRubys -= 2;addPiece(2,2);
                    this.display.customInput(this.textGraphics, this.screen, 7);}
                }
                if (piecePurchase == 3){ if(canAfford(0,0,2,9,0,0)){this.playerTwoLumber -= 9; this.playerTwoRubys -= 2;addPiece(3,2);
                    this.display.customInput(this.textGraphics, this.screen, 8);}
                }
                if (piecePurchase == 4){ if(canAfford(20,0,0,0,2,0)){this.playerTwoStone -= 20; this.playerTwoPearls -= 2;addPiece(4,2);
                    this.display.customInput(this.textGraphics, this.screen, 9);}
                }
                if (piecePurchase == 5){ if(canAfford(0,0,0,20,0,2)){this.playerTwoLumber -= 20; this.playerTwoSapphire -= 2;addPiece(5,2);
                    this.display.customInput(this.textGraphics, this.screen, 10);}
                }
            }
        }
    }
    public boolean canAfford (int stone, int wheat, int ruby, int trees, int pearls, int sapphire)
    {
        boolean afford = true;
        if (this.turn == 1)
        {
            if (stone > this.playerOneStone) {afford =false;}
            if (wheat > this.playerOneWheat) {afford =false;}
            if (ruby > this.playerOneRubys) {afford =false;}
            if (trees > this.playerOneLumber) {afford =false;}
            if (pearls > this.playerOnePearls) {afford =false;}
            if (sapphire > this.playerOneSapphire) {afford =false;}


        }
        if (this.turn == -1)
        {
            if (stone > this.playerTwoStone) {afford =false;}
            if (wheat > this.playerTwoWheat) {afford =false;}
            if (ruby > this.playerTwoRubys) {afford =false;}
            if (trees > this.playerTwoLumber) {afford =false;}
            if (pearls > this.playerTwoPearls) {afford =false;}
            if (sapphire > this.playerTwoSapphire) {afford =false;}


        }
        if (!afford){
            this.display.customInput(this.textGraphics, this.screen, 4);
        }
        return afford;
    }
    public void addPiece(int pieceCode, int player)
    {
        if (player == 1)
        {
            for (int i = 0; i < 50; i++)
            {
                if(this.playerOnePieces[i][0] == 0)
                {
                    this.playerOnePieces[i][0] = pieceCode;
                    i = 51;
                }
            }
        }
        if (player == 2)
        {
            for (int i = 0; i < 50; i++)
            {
                if(this.playerTwoPieces[i][0] == 0)
                {
                    this.playerTwoPieces[i][0] = pieceCode;
                    i = 51;
                }
            }
        }
    }
    // stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerOneNodeTracker[i][3]) +  printSpecialCharacter(this.playerOneNodeTracker[i][5] + 18));
    public void ManageUnits()
    {
        //*********************************************Player 1 ********************************************* done
        if (this.turn == 1)
        {

            int[] nodeTracker = new int[36];
            int[][] jumpTracker = new int[36][36];
            this.display.emptyText(textGraphics);
            emptyText();
            stringConcactinate("Do you want to...");
            stringConcactinate("1) Place civil units");
            stringConcactinate("2) Remove civil units");
            stringConcactinate("3) Deploy army pieces");
            stringConcactinate("4) Move army pieces");
            stringConcactinate("0) Back ");
            int place = trycatch(false,1,5,0, false, null, 0);
            int list = 1;
            if (place == 0){return;}
            if (place > 4){this.display.customInput(this.textGraphics,this.screen, 1);}
            if (place == 1)
            {
                emptyText();
                stringConcactinate("Area's available ");
                stringConcactinate("to place units...");
                for (int i = 0; i < 36; i++)
                {
                    if (this.playerOneNodeTracker[i][0] > 0 && this.playerOneNodeTracker[i][4] == 0){

                        stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerOneNodeTracker[i][3]) + printSpecialCharacter(this.playerOneNodeTracker[i][5] + 18));
                        jumpTracker[list -1][0] = this.playerOneNodeTracker[i][1];
                        jumpTracker[list -1][1] = this.playerOneNodeTracker[i][2];
                        list = list + 1;
                        nodeTracker[list - 1] = i;
                    }
                }
                stringConcactinate("0) Back ");
                int road = trycatch(false,2,list,0, true, jumpTracker, 1);
                if (road == 0){ChangeTurn();}
                if (road > list){stringConcactinate("Invalid Answer"); ChangeTurn();}
                if (road < list)
                {
                    int list2 = 0;
                    emptyText();
                    int[] microTrack = new int[4];
                    int[][] jumpTracker2 = new int[1][2];
                    jumpTracker2[0][0] = this.playerOneNodeTracker[nodeTracker[road]][1];
                    jumpTracker2[0][1] = this.playerOneNodeTracker[nodeTracker[road]][2];
                    stringConcactinate("What do you ");
                    stringConcactinate("want to place?");
                    if (this.playerOneWorkers > 0){ list2++; stringConcactinate(list2 +") Worker☭"); microTrack[list2] = 1;}
                    if (this.playerOneSoldiers > 0){list2++; stringConcactinate(list2 +") RoadBuster⛏"); microTrack[list2] = 2;}
                    if (this.playerOneCitiesToPlace > 0){list2++; stringConcactinate(list2 +") Settlement A-C"); microTrack[list2] = 3;}
                    stringConcactinate("0) Back");
                    int placement = trycatch(false,2,list2 + 1,0, true, jumpTracker2, 99);
                    //int placement = trycatch(false,2,list2 + 1,0, false, null, 0);
                    if (placement == 0){return;}
                    if (placement > 3){return;}
                    if (microTrack[placement] == 1){this.playerOneWorkers--; this.playerOneNodeTracker[nodeTracker[road]][4] = 1;
                        this.grid[this.playerOneNodeTracker[nodeTracker[road]][1]][this.playerOneNodeTracker[nodeTracker[road]][2]] = 57;  ChangeTurn();


                    }

                    if (microTrack[placement]  == 2){this.playerOneSoldiers--; this.playerOneNodeTracker[nodeTracker[road]][4] = 2;
                        this.grid[this.playerOneNodeTracker[nodeTracker[road]][1]][this.playerOneNodeTracker[nodeTracker[road]][2]] = 58;  ChangeTurn();

                    }

                    if (microTrack[placement]  == 3){this.playerOneCitiesToPlace--; this.playerOneNodeTracker[nodeTracker[road]][4] = -1;
                        this.grid[this.playerOneNodeTracker[nodeTracker[road]][1]][this.playerOneNodeTracker[nodeTracker[road]][2]] = this.playerOneCitiesAmount;
                        if (this.playerOneNodeTracker[nodeTracker[road]][0] == 1){playerOneCities[road][3] = 1;}
                        if (this.playerOneNodeTracker[nodeTracker[road]][0] == 2){playerOneCities[road][4] = 1;}
                        if (this.playerOneNodeTracker[nodeTracker[road]][0] == 3){playerOneCities[road][1] = 1;}
                        if (this.playerOneNodeTracker[nodeTracker[road]][0] == 4){playerOneCities[road][2] = 1;}


                    }

                }
            }
            if (place == 2)
            {
                list = 0;
                emptyText();
                stringConcactinate("Area's where units");
                stringConcactinate("are placed...");
                for (int i = 0; i < 36; i++)
                {
                    if (this.playerOneNodeTracker[i][0] > 0 && this.playerOneNodeTracker[i][4] > 0){

                        stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerOneNodeTracker[i][3]) +
                                printSpecialCharacter(this.playerOneNodeTracker[i][5] + 18) +  printSpecialCharacter(this.playerOneNodeTracker[i][4] + 22));
                        System.out.println();
                        list = list + 1;
                        nodeTracker[list - 1] = i;
                    }
                }
                stringConcactinate("0) Back ");
                int unit = trycatch(false,2,list,0, false, null, 0);
                if (unit == 0){return;}
                if (unit > list){return;}
                if (unit > 0)
                {
                    if (this.playerOneNodeTracker[nodeTracker[unit]][4] == 1){
                        this.playerOneWorkers++;
                        this.playerOneNodeTracker[nodeTracker[unit]][4] = 0;
                        this.grid[this.playerOneNodeTracker[nodeTracker[unit]][1]][this.playerOneNodeTracker[nodeTracker[unit]][2]] = 34;
                        this.playerOneWorkerNodeTracker[nodeTracker[unit]][0] = 0;
                    }
                    if (this.playerOneNodeTracker[nodeTracker[unit]][4] == 2){
                        this.playerOneSoldiers++;
                        this.playerOneNodeTracker[nodeTracker[unit]][4] = 0;
                        this.grid[this.playerOneNodeTracker[nodeTracker[unit]][1]][this.playerOneNodeTracker[nodeTracker[unit]][2]] = 34;}
                }
            }
            if (place == 3){
                int[] barracksCities;
                barracksCities = new int[9];
                emptyText();
                stringConcactinate("Choose settlement");
                stringConcactinate("to place pieces");
                stringConcactinate("(Barracks Required)");

                int newList = 0;
                for (int i = 0;  i < 9; i++)
                {
                    if ( this.playerOneCities[i][5] == 1)// looking for barracks
                    {
                        newList ++;
                        stringConcactinate(newList + ") Settlement " + printSpecialCharacter(i + 1));
                        barracksCities[newList] = i;
                    }
                }
                stringConcactinate("0) Back");
                int city = trycatch(false,3,newList + 1, 0, false, null, 0);
                int[] cord = FindNode(barracksCities[city] + 1);
                int pawn = 0;
                int knight = 0;
                int bishop = 0;
                int rook = 0;
                int queen = 0;
                int total = 0;
                int[] pieceList;
                pieceList = new int[30];
                for (int i = 0; i < this.playerOnePieces.length; i++)
                {

                        if (this.playerOnePieces[i][0] == 1 && this.playerOnePieces[i][3] == 0) {pawn ++;}
                        if (this.playerOnePieces[i][0] == 2 && this.playerOnePieces[i][3] == 0) {knight ++;}
                        if (this.playerOnePieces[i][0] == 3 && this.playerOnePieces[i][3] == 0) {bishop ++;}
                        if (this.playerOnePieces[i][0] == 4 && this.playerOnePieces[i][3] == 0) {rook ++;}
                        if (this.playerOnePieces[i][0] == 5 && this.playerOnePieces[i][3] == 0) {queen ++;}

                }
                if (city == 0 || city > newList){
                    return;}
                emptyText();
                stringConcactinate("Choose which piece");
                stringConcactinate("to place");
                if (pawn > 0){ total ++; stringConcactinate(  total + "): ♟ " + pawn); pieceList[total] = 1;}
                if (knight > 0){total ++; stringConcactinate(  total +"): ♞ " + knight);pieceList[total] = 2;}
                if (bishop > 0){total ++; stringConcactinate( total +"): ♝ " + bishop);pieceList[total] = 3;}
                if (rook > 0){total ++; stringConcactinate( total +"): ♜ " + rook);pieceList[total] = 4;}
                if (queen > 0){total ++; stringConcactinate( total +"): ♛ " + queen);pieceList[total] = 5;}
                stringConcactinate("0) Back");
                int piece = trycatch(false,2,total + 1,0, false, null, 0);
                if (pawn > 0 && pieceList[piece] == 1){ deployPiece(cord[0], cord[1],1);}
                if (knight > 0 && pieceList[piece] == 2){deployPiece(cord[0], cord[1],2);}
                if (bishop > 0 && pieceList[piece] == 3){deployPiece(cord[0], cord[1],3);}
                if (rook > 0 && pieceList[piece]== 4){deployPiece(cord[0], cord[1],4);}
                if (queen > 0 && pieceList[piece] == 5){deployPiece(cord[0], cord[1],5);}
                if (piece == 0 || piece > 5){
                    return;}
            }
            if (place == 4)
            {
                 int[] placedPieces;
                 placedPieces = new int[50];

                 emptyText();
                 list = 1;
                if (this.pieceMovesOne < 1 && this.playerOneKingMoves < 1){
                    this.display.customInput(this.textGraphics,this.screen, 15);
                    return;
                }
                if (this.pieceMovesOne < 1 && this.playerOneKingMoves > 1){activeKing(); return;}

                 stringConcactinate("Choose piece to move");
                 stringConcactinate("Moves: " + this.pieceMovesOne);
                 stringConcactinate("1) ♚");
                 for (int i = 0; i < this.playerOnePieces.length; i++)
                 {
                    if (this.playerOnePieces[i][3] == 1)
                    {
                        list ++;
                        if (this.playerOnePieces[i][0] == 1)
                        {
                            stringConcactinate(list + "): ♟ " + "x:" + this.playerOnePieces[i][1] + "y:" + this.playerOnePieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerOnePieces[i][0] == 2)
                        {
                            stringConcactinate(list + "): ♞ " + "x:" + this.playerOnePieces[i][1] + "y:" + this.playerOnePieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerOnePieces[i][0] == 3)
                        {
                            stringConcactinate(list + "): ♝ " + "x:" + this.playerOnePieces[i][1] + "y:" + this.playerOnePieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerOnePieces[i][0] == 4)
                        {
                            stringConcactinate(list + "): ♜ " + "x:" + this.playerOnePieces[i][1] + "y:" + this.playerOnePieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerOnePieces[i][0] == 5)
                        {
                            stringConcactinate(list + "): ♛ " + "x:" + this.playerOnePieces[i][1] + "y:" + this.playerOnePieces[i][2] );
                            placedPieces[list] = i;
                        }
                    }
                }
                stringConcactinate("0) Back");
                int piece = trycatch(false,2,list + 1,0, false, null, 0);
                if (piece == 0)
                {
                    return;
                }
                if (piece == 1)
                {
                    if (this.playerOneKingMoves < 1) {
                        this.display.customInput(this.textGraphics, this.screen,15);
                        return;
                    }
                    else {activeKing();}
                }
                 if (piece > list){return;}
                 if (piece > 1)
                 {
                     //pieceDirection(piece - 2);
                     pieceDirection(placedPieces[piece]);
                 }
            }
        }

        //*********************************************Player 2 *********************************************
        if (this.turn == -1)
        {
            this.display.emptyText(textGraphics);
            emptyText();
            int[] nodeTracker2 = new int[36];
            stringConcactinate("Do you want to...");
            stringConcactinate("1) Place civil units");
            stringConcactinate("2) Remove civil units");
            stringConcactinate("3) Deploy army pieces");
            stringConcactinate("4) Move army pieces");
            stringConcactinate("0) Back ");
            int place = trycatch(false,1,5, 0, false, null, 0);
            int list = 1;
            if (place == 0){return;}
            if (place > 4){this.display.customInput(this.textGraphics,this.screen, 1);}
            if (place == 1)
            {
                emptyText();
                stringConcactinate("Area's available ");
                stringConcactinate("to place units...");
                for (int i = 0; i < 36; i++)
                {
                    if (this.playerTwoNodeTracker[i][0] > 0 && this.playerTwoNodeTracker[i][4] == 0){

                        stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerTwoNodeTracker[i][3]+9) +
                                printSpecialCharacter(this.playerTwoNodeTracker[i][5] + 18));

                        System.out.println();
                        list = list + 1;
                        nodeTracker2[list - 1] = i;
                    }
                }
                stringConcactinate("0) Back ");
                int road = trycatch(false,2,list, 0, false, null, 0);
                if (road == 0){return;}
                if (road > list){stringConcactinate("Invalid Answer"); ChangeTurn();}
                if (road < list)
                {
                    int[] microTrack = new int[4];
                    emptyText();
                    stringConcactinate("What do you ");
                    stringConcactinate("want to place?");
                    int list2 = 0;
                    if (this.playerTwoWorkers > 0){ list2++; stringConcactinate(  list2 + ") Worker☭"); microTrack[list2] = 1;}
                    if (this.playerTwoSoldiers > 0){list2++; stringConcactinate(list2 +") RoadBuster⛏"); microTrack[list2] = 2;}
                    if (this.playerTwoCitiesToPlace > 0){list2++; stringConcactinate(list2 +") Settlement X-Z"); microTrack[list2] = 3;}
                    stringConcactinate("0) Back");
                    int placement = trycatch(false,2,list2 + 1, 0, false, null, 0);
                    if (placement == 0){return;}
                    if (placement > 3){return;}

                    if (microTrack[placement] == 1){this.playerTwoWorkers--; this.playerTwoNodeTracker[nodeTracker2[road]][4] = 1;
                        this.grid[this.playerTwoNodeTracker[nodeTracker2[road]][1]][this.playerTwoNodeTracker[nodeTracker2[road]][2]] = 30;

                    }

                    if (microTrack[placement] == 2){this.playerTwoSoldiers--; this.playerTwoNodeTracker[nodeTracker2[road]][4] = 2;
                        this.grid[this.playerTwoNodeTracker[nodeTracker2[road]][1]][this.playerTwoNodeTracker[nodeTracker2[road]][2]] = 32;

                    }

                    if (microTrack[placement] == 3){this.playerTwoCitiesToPlace--; this.playerTwoNodeTracker[nodeTracker2[road]][4] = -1;
                        this.grid[this.playerTwoNodeTracker[nodeTracker2[road]][1]][this.playerTwoNodeTracker[nodeTracker2[road]][2]] = this.playerTwoCitiesAmount + 9;
                        if (this.playerTwoNodeTracker[nodeTracker2[road]][0] == 1){playerTwoCities[road][3] = 1;}
                        if (this.playerTwoNodeTracker[nodeTracker2[road]][0] == 2){playerTwoCities[road][4] = 1;}
                        if (this.playerTwoNodeTracker[nodeTracker2[road]][0] == 3){playerTwoCities[road][1] = 1;}
                        if (this.playerTwoNodeTracker[nodeTracker2[road]][0] == 4){playerTwoCities[road][2] = 1;}

                    }
                }
            }
            if (place == 2)
            {
                emptyText();
                stringConcactinate("Area's where units");
                stringConcactinate("are placed...");
                for (int i = 0; i < 36; i++)
                {
                    if (this.playerTwoNodeTracker[i][0] > 0 && this.playerTwoNodeTracker[i][4] > 0){

                        stringConcactinate(list + ") Settlement " + printSpecialCharacter(this.playerTwoNodeTracker[i][3]+9) +
                                printSpecialCharacter(this.playerTwoNodeTracker[i][5] + 18) + printSpecialCharacter(this.playerTwoNodeTracker[i][4] + 22));
                        System.out.println();
                        list = list + 1;
                        nodeTracker2[list - 1] = i;
                    }
                }
                stringConcactinate("0) Back ");
                int unit = trycatch(false,2,list, 0, false, null, 0);
                if (unit == 0){return;}
                if (unit > list){stringConcactinate("Invalid Answer"); ManageUnits();}
                if (unit > 0)
                {
                    if (this.playerTwoNodeTracker[nodeTracker2[unit]][4] == 1){
                        this.playerTwoWorkers++;
                        this.playerTwoNodeTracker[nodeTracker2[unit]][4] = 0;
                        this.grid[this.playerTwoNodeTracker[nodeTracker2[unit]][1]][this.playerTwoNodeTracker[nodeTracker2[unit]][2]] = 33;
                        this.playerTwoWorkerNodeTracker[nodeTracker2[unit]][0] = 0;
                    }
                    if (this.playerTwoNodeTracker[nodeTracker2[unit]][4] == 2){
                        this.playerTwoSoldiers++;
                        this.playerTwoNodeTracker[nodeTracker2[unit]][4] = 0;
                        this.grid[this.playerTwoNodeTracker[nodeTracker2[unit]][1]][this.playerTwoNodeTracker[nodeTracker2[unit]][2]] = 33;}

                }
            }
            if (place == 3){
                int[] barracksCities;
                barracksCities = new int[9];
                emptyText();
                stringConcactinate("Choose settlement");
                stringConcactinate("to place pieces");
                stringConcactinate("(Barracks Required)");

                int newList = 0;
                for (int i = 0;  i < 9; i++)
                {
                    if ( this.playerTwoCities[i][5] == 1)// looking for barracks
                    {
                        newList ++;
                        stringConcactinate(newList + ") Settlement " + printSpecialCharacter(i + 10));
                        barracksCities[newList] = i;
                    }
                }
                stringConcactinate("0) Back");
                int city = trycatch(false,3,newList + 1, 0, false, null, 0);
                if (city == 0 || city > newList){
                    return;}
                int[] cord = FindNode(barracksCities[city] + 10);
                int pawn = 0;
                int knight = 0;
                int bishop = 0;
                int rook = 0;
                int queen = 0;
                int total = 0;
                int[] pieceList;
                pieceList = new int[30];
                for (int i = 0; i < this.playerTwoPieces.length; i++)
                {

                    if (this.playerTwoPieces[i][0] == 1 && this.playerTwoPieces[i][3] == 0) {pawn ++;}
                    if (this.playerTwoPieces[i][0] == 2 && this.playerTwoPieces[i][3] == 0) {knight ++;}
                    if (this.playerTwoPieces[i][0] == 3 && this.playerTwoPieces[i][3] == 0) {bishop ++;}
                    if (this.playerTwoPieces[i][0] == 4 && this.playerTwoPieces[i][3] == 0) {rook ++;}
                    if (this.playerTwoPieces[i][0] == 5 && this.playerTwoPieces[i][3] == 0) {queen ++;}

                }
                emptyText();
                stringConcactinate("Choose which piece");
                stringConcactinate("to place");
                if (pawn > 0){ total ++; stringConcactinate(  total + "): ♟ " + pawn); pieceList[total] = 1;}
                if (knight > 0){total ++; stringConcactinate(  total +"): ♞ " + knight);pieceList[total] = 2;}
                if (bishop > 0){total ++; stringConcactinate( total +"): ♝ " + bishop);pieceList[total] = 3;}
                if (rook > 0){total ++; stringConcactinate( total +"): ♜ " + rook);pieceList[total] = 4;}
                if (queen > 0){total ++; stringConcactinate( total +"): ♛ " + queen);pieceList[total] = 5;}
                stringConcactinate("0) Back");
                int piece = trycatch(false,2,total + 1,0, false, null, 0);

                if (pawn > 0 && pieceList[piece] == 1){ deployPiece(cord[0], cord[1],1);}
                if (knight > 0 && pieceList[piece] == 2){deployPiece(cord[0], cord[1],2);}
                if (bishop > 0 && pieceList[piece] == 3){deployPiece(cord[0], cord[1],3);}
                if (rook > 0 && pieceList[piece]== 4){deployPiece(cord[0], cord[1],4);}
                if (queen > 0 && pieceList[piece] == 5){deployPiece(cord[0], cord[1],5);}
                if (piece == 0 || piece > 5){
                    return;}
            }
            if (place == 4)
            {
                int[] placedPieces;
                placedPieces = new int[50];
                emptyText();
                list = 1;
                if (this.pieceMovesTwo < 1 && this.playerTwoKingMoves < 1){
                    this.display.customInput(this.textGraphics,this.screen, 15);
                    return;
                }
                if (this.pieceMovesTwo < 1 && this.playerTwoKingMoves > 1){activeKing(); return;}
                stringConcactinate("Choose piece to move");
                stringConcactinate("Moves: " + this.pieceMovesTwo);
                stringConcactinate("1) ♚");
                for (int i = 0; i < this.playerTwoPieces.length; i++)
                {
                    if (this.playerTwoPieces[i][3] == 1)
                    {
                        list ++;
                        if (this.playerTwoPieces[i][0] == 1)
                        {
                            stringConcactinate(list + "): ♟ " + "x:" + this.playerTwoPieces[i][1] + "y:" + this.playerTwoPieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerTwoPieces[i][0] == 2)
                        {
                            stringConcactinate(list + "): ♞ " + "x:" + this.playerTwoPieces[i][1] + "y:" + this.playerTwoPieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerTwoPieces[i][0] == 3)
                        {
                            stringConcactinate(list + "): ♝ " + "x:" + this.playerTwoPieces[i][1] + "y:" + this.playerTwoPieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerTwoPieces[i][0] == 4)
                        {
                            stringConcactinate(list + "): ♜ " + "x:" + this.playerTwoPieces[i][1] + "y:" + this.playerTwoPieces[i][2] );
                            placedPieces[list] = i;
                        }
                        if (this.playerTwoPieces[i][0] == 5)
                        {
                            stringConcactinate(list + "): ♛ " + "x:" + this.playerTwoPieces[i][1] + "y:" + this.playerTwoPieces[i][2] );
                            placedPieces[list] = i;
                        }
                    }
                }
                stringConcactinate("0) Back");
                int piece = trycatch(false,2,list + 1,0, false, null, 0);
                if (piece == 0)
                {
                    ManageUnits();
                }
                if (piece == 1)
                {
                    if (this.playerTwoKingMoves < 1) {
                        this.display.customInput(this.textGraphics, this.screen,15);
                        return;
                    }
                    else {activeKing();}
                }
                if (piece > list){return;}
                if (piece > 1)
                {

                    //pieceDirection(piece - 2);
                    pieceDirection(placedPieces[piece]);
                }
            }
        }
    }
    public void activeKing()
    {
        if (this.turn == 1) {
            if (this.playerOneKingMoves < 1) {
                //this.display.customInput(this.textGraphics, this.screen,15);
                if (this.turnsAmount > 1){return;}
                else {ChangeTurn();}

            }
            try {
                emptyText();
                String moveBar = "";
                stringConcactinate("Kings Movement:");
                for (int i = 0; i < this.playerOneKingMoves; i ++)
                {
                    moveBar += "▓";
                }
                stringConcactinate(moveBar);
                stringConcactinate("");
                stringConcactinate("King Inventory: ");
                String inv = "";
                for (int i = 0; i < this.kingOneInventory.size(); i++)
                {
                    inv += this.kingOneInventory.get(i);
                    inv += " ";
                }
                stringConcactinate(inv);
                stringConcactinate("");
                stringConcactinate("Press Enter to exit");
                refresh();
                int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                if (direction == 4) {
                    int canMove = checkSquare(this.PlayerOneKing[0], this.PlayerOneKing[1] - 1,true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1] - 1] = 45;
                        this.PlayerOneKing[1] -= 1;
                        this.playerOneKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1] - 1] = 45;
                        this.PlayerOneKing[1] -= 1;
                        this.playerOneKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3) {

                        this.PlayerOneKing[1] -= 1;
                        this.playerOneKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = 45;
                        activeKing();
                    }
                }
                if (direction == 3) {
                    int canMove = checkSquare(this.PlayerOneKing[0] + 1, this.PlayerOneKing[1],true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerOneKing[0] + 1][this.PlayerOneKing[1]] = 45;
                        this.PlayerOneKing[0] += 1;
                        this.playerOneKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerOneKing[0] + 1][this.PlayerOneKing[1]] = 45;
                        this.PlayerOneKing[0] += 1;
                        this.playerOneKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3) {

                        this.PlayerOneKing[0] += 1;
                        this.playerOneKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = 45;
                        activeKing();
                    }
                }
                if (direction == 2) {
                    int canMove = checkSquare(this.PlayerOneKing[0], this.PlayerOneKing[1] + 1,true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1] + 1] = 45;
                        this.PlayerOneKing[1] += 1;
                        this.playerOneKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1] + 1] = 45;
                        this.PlayerOneKing[1] += 1;
                        this.playerOneKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3)
                    {
                        this.PlayerOneKing[1] += 1;
                        this.playerOneKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = 45;
                        activeKing();
                    }
                }
                if (direction == 1) {
                    int canMove = checkSquare(this.PlayerOneKing[0] - 1, this.PlayerOneKing[1],true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerOneKing[0] - 1][this.PlayerOneKing[1]] = 45;
                        this.PlayerOneKing[0] -= 1;
                        this.playerOneKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerOneKing[0] - 1][this.PlayerOneKing[1]] = 45;
                        this.PlayerOneKing[0] -= 1;
                        this.playerOneKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3)
                    {
                        this.PlayerOneKing[0] -= 1;
                        this.playerOneKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = 45;
                        activeKing();
                    }
                }
                if (direction ==0 && !this.p1KingGarrisoned)
                {
                    this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = 45;
                }

            } catch (Exception e) {

            }
        }

        if (this.turn == -1) {
            if (this.playerTwoKingMoves < 1) {
                //this.display.customInput(this.textGraphics, this.screen,15);
                if (this.turnsAmount > 1){return;}
                else {ChangeTurn();}
            }
            try {
                emptyText();
                String moveBar = "";
                stringConcactinate("Kings Movement:");
                for (int i = 0; i < this.playerTwoKingMoves; i ++)
                {
                    moveBar += "▓";
                }
                stringConcactinate(moveBar);
                stringConcactinate("");
                stringConcactinate("King Inventory: ");
                String inv = "";
                for (int i = 0; i < this.kingTwoInventory.size(); i++)
                {
                    inv += this.kingTwoInventory.get(i);
                    inv += " ";
                }
                stringConcactinate(inv);
                stringConcactinate("");
                stringConcactinate("Press Enter to exit");
                refresh();
                int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = this.p2kingstep;
                if (direction == 4) {
                    int canMove = checkSquare(this.PlayerTwoKing[0], this.PlayerTwoKing[1] - 1,true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1] - 1] = 46;
                        this.PlayerTwoKing[1] -= 1;
                        this.playerTwoKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1] - 1] = 46;
                        this.PlayerTwoKing[1] -= 1;
                        this.playerTwoKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3) {

                        this.PlayerTwoKing[1] -= 1;
                        this.playerTwoKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = 46;
                        activeKing();
                    }
                }
                if (direction == 3) {
                    int canMove = checkSquare(this.PlayerTwoKing[0] + 1, this.PlayerTwoKing[1],true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerTwoKing[0] + 1][this.PlayerTwoKing[1]] = 46;
                        this.PlayerTwoKing[0] += 1;
                        this.playerTwoKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerTwoKing[0] + 1][this.PlayerTwoKing[1]] = 46;
                        this.PlayerTwoKing[0] += 1;
                        this.playerTwoKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3) {

                        this.PlayerTwoKing[0] += 1;
                        this.playerTwoKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = 46;
                        activeKing();
                    }
                }
                if (direction == 2) {
                    int canMove = checkSquare(this.PlayerTwoKing[0], this.PlayerTwoKing[1] + 1,true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1] + 1] = 46;
                        this.PlayerTwoKing[1] += 1;
                        this.playerTwoKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1] + 1] = 46;
                        this.PlayerTwoKing[1] += 1;
                        this.playerTwoKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3)
                    {
                        this.PlayerTwoKing[1] += 1;
                        this.playerTwoKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = 46;
                        activeKing();
                    }
                }
                if (direction == 1) {
                    int canMove = checkSquare(this.PlayerTwoKing[0] - 1, this.PlayerTwoKing[1],true,0);
                    if (canMove == 1) {
                        this.grid[this.PlayerTwoKing[0] - 1][this.PlayerTwoKing[1]] = 46;
                        this.PlayerTwoKing[0] -= 1;
                        this.playerTwoKingMoves -= 2;
                        activeKing();
                    }
                    if (canMove == 2) {
                        this.grid[this.PlayerTwoKing[0] - 1][this.PlayerTwoKing[1]] = 46;
                        this.PlayerTwoKing[0] -= 1;
                        this.playerTwoKingMoves -= 1;
                        activeKing();
                    }
                    if (canMove == 3)
                    {
                        this.PlayerTwoKing[0] -= 1;
                        this.playerTwoKingMoves -= 1;
                        GarrisonKing();
                        activeKing();
                    }
                    if (canMove == 0) {
                        this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = 46;
                        activeKing();
                    }
                }
                if (direction ==0 && !this.p2KingGarrisoned)
                {
                    this.grid[this.PlayerTwoKing[0]][this.PlayerTwoKing[1]] = 46;
                }

            } catch (Exception e) {

            }
        }
    }
    public void GarrisonKing()
    {
        if (this.turn == 1)
        {
            String material = "";
            for (int i = 0; i < this.kingOneInventory.size(); i++)
            {
                material = this.kingOneInventory.get(i);
                if (material == "Δ"){this.playerOneLumber += 3; this.display.customInput(this.textGraphics, this.screen, 11);
                }
                if (material == "▿"){this.playerOneRubys += 1; this.display.customInput(this.textGraphics, this.screen, 12);
                }
                if (material == "°"){this.playerOnePearls += 1;this.display.customInput(this.textGraphics, this.screen, 13);
                }
                if (material == "✦"){this.playerOneSapphire += 1;this.display.customInput(this.textGraphics, this.screen, 14);
                }
                if (material == "♦"){this.display.winAnimationOne(this.textGraphics,this.screen);
                }
            }
            this.kingOneInventory.clear();
        }
        if (this.turn == -1)
        {
            String material = "";
            for (int i = 0; i < this.kingTwoInventory.size(); i++)
            {
                material = this.kingTwoInventory.get(i);
                if (material == "Δ"){this.playerTwoLumber += 3; this.display.customInput(this.textGraphics, this.screen, 11);
                }
                if (material == "▿"){this.playerTwoRubys += 1; this.display.customInput(this.textGraphics, this.screen, 12);
                }
                if (material == "°"){this.playerTwoPearls += 1;this.display.customInput(this.textGraphics, this.screen, 13);
                }
                if (material == "✦"){this.playerTwoSapphire += 1;this.display.customInput(this.textGraphics, this.screen, 14);
                }
                if (material == "♦"){this.display.winAnimationTwo(this.textGraphics,this.screen);
                }
            }
            this.kingTwoInventory.clear();
        }

    }
    public void pieceDirection(int piece)
    {
        int pieceType = 0;
        if (this.turn == 1){ pieceType = this.playerOnePieces[piece][0];}
        if (this.turn == -1){ pieceType = this.playerTwoPieces[piece][0];}
        if (pieceType == 1)
        {
            activePiece(piece, 0);
        }
        if (pieceType == 2)
        {
           activePiece(piece, 0);
        }
        if (pieceType == 3)
        {
            emptyText();
            stringConcactinate("Bishop:");
            stringConcactinate("");
            stringConcactinate("Do you want to move ");
            stringConcactinate("1) 45 degrees ↙↗");
            stringConcactinate("2) 135 degrees ↖↘");
            stringConcactinate("0) back ");
            int direction = trycatch(false,3,3,0, false, null, 0);
            if (direction > 2 || direction == 0)
            {
                WrongInput();
            }
            else { activePiece(piece, direction);}
        }
        if (pieceType == 4)
        {
            emptyText();
            stringConcactinate("Rook:");
            stringConcactinate("");
            stringConcactinate("Do you want to move ");
            stringConcactinate("1) Vertically  ⇕");
            stringConcactinate("2) Horizontally  ⇐⇒");
            stringConcactinate("0) back ");
            int direction = trycatch(false,3,3,0, false, null, 0);
            if (direction > 2|| direction == 0)
            {
                WrongInput();
            }
            else { activePiece(piece, direction);}
        }
        if (pieceType == 5)
        {
            emptyText();
            stringConcactinate("Queen:");
            stringConcactinate("");
            stringConcactinate("Do you want to move ");
            stringConcactinate("1) Vertically  ⇕");
            stringConcactinate("2) Horizontally  ⇐⇒");
            stringConcactinate("3) 45 degrees ↙↗");
            stringConcactinate("4) 135 degrees ↖↘");
            stringConcactinate("0) back ");
            int direction = trycatch(false,3,5,0, false, null, 0);
            if (direction > 4|| direction == 0)
            {
                WrongInput();
            }
            else { activePiece(piece, direction);}
        }
    }

    public void activePiece(int piece, int dimension)
    {
        if (this.turn == 1) {
            int pieceType = this.playerOnePieces[piece][0];
            //if (this.playerOneWheat < 1) {
              //  return;
           // }
            if (pieceType == 1) {
                this.display.customInput(this.textGraphics, this.screen,15);
                if (this.playerOnePieces[piece][5] > 7) {
                    this.pieceMovesOne --;
                    return;
                }
                try {
                    emptyText();
                    String moveBar = "";
                    stringConcactinate("Pawns Movement:");
                    for (int i = this.playerOnePieces[piece][5] * 2; i < 16; i++) {
                        moveBar += "▓";
                    }
                    stringConcactinate(moveBar);
                    stringConcactinate("");
                    stringConcactinate("Press Enter to exit");
                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = this.playerOnePieces[piece][4];
                    if (direction == 4) {
                        int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] - 1, false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] - 1] = 47;
                            this.playerOnePieces[piece][2] -= 1;
                            this.playerOnePieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] - 1] = 47;
                            this.playerOnePieces[piece][2] -= 1;
                            this.playerOnePieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 47;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 3) {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2], false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerOnePieces[piece][1]+1][this.playerOnePieces[piece][2]] = 47;
                            this.playerOnePieces[piece][1] += 1;
                            this.playerOnePieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerOnePieces[piece][1]+1][this.playerOnePieces[piece][2]] = 47;
                            this.playerOnePieces[piece][1] += 1;
                            this.playerOnePieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 47;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 2) {
                        int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] + 1, false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]+ 1] = 47;
                            this.playerOnePieces[piece][2] += 1;
                            this.playerOnePieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]+ 1] = 47;
                            this.playerOnePieces[piece][2] += 1;
                            this.playerOnePieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 47;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 1) {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2], false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerOnePieces[piece][1]-1][this.playerOnePieces[piece][2]] = 47;
                            this.playerOnePieces[piece][1] -= 1;
                            this.playerOnePieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerOnePieces[piece][1]-1][this.playerOnePieces[piece][2]] = 47;
                            this.playerOnePieces[piece][1] -= 1;
                            this.playerOnePieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 47;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 0)
                    {
                        this.pieceMovesOne --;
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 47;
                        removePiece(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2], piece);
                    }
                } catch (Exception e) {

                }
            }
            if (pieceType == 2)
            {
                if (this.playerOnePieces[piece][5] > 2)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Knight:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int octagon = dimension;
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    if (direction == 1 || direction == 4){octagon ++;}
                    if (direction == 3 || direction == 2){octagon --;}
                    if (direction == 0){

                        this.playerOnePieces[piece][1] = this.knightLastMoveOne[0];
                        this.playerOnePieces[piece][2] = this.knightLastMoveOne[1];
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 49;
                        this.playerOnePieces[piece][5] ++;
                        this.pieceMovesOne --;
                        removePiece(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2], piece);
                        return;
                    }
                    this.grid[ this.knightLastMoveOne[0]][this.knightLastMoveOne[1]] =  this.knightLastMoveOne[2];
                    if (direction == 5 ){
                        this.display.visualizeMovement(this.knightLastMoveOne[0],this.knightLastMoveOne[1], this.playerOnePieces[piece][0], this.grid, this.textGraphics, this.screen, this.turn);
                        //this.grid[this.knightLastMoveOne[0]][this.knightLastMoveOne[1]] = 49;
                    }
                    if (octagon > 8){octagon = 1;}
                    if (octagon < 1){octagon = 8;}
                    if (octagon == 1)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] - 2, this.playerOnePieces[piece][2] - 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] - 2][this.playerOnePieces[piece][2] - 1] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] - 2;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] - 1;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];


                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 2)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] - 2, this.playerOnePieces[piece][2] + 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] - 2][this.playerOnePieces[piece][2] + 1] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] - 2;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] + 1;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];

                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 3)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] + 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] + 2] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] - 1;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] + 2;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];

                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 4)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] + 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] + 2] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] + 1;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] + 2;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];

                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }

                    if (octagon == 6)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] + 2, this.playerOnePieces[piece][2] - 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] + 2][this.playerOnePieces[piece][2] - 1] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] + 2;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] - 1;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];

                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {

                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 5)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] + 2, this.playerOnePieces[piece][2] + 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] + 2][this.playerOnePieces[piece][2] + 1] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] + 2;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] + 1;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }

                    if (octagon == 8)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] - 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] - 2] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] - 1;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] - 2;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 7)
                    {
                        int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] - 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] - 2] = 49;
                            this.knightLastMoveOne[0] = this.playerOnePieces[piece][1] + 1;
                            this.knightLastMoveOne[1] = this.playerOnePieces[piece][2] - 2;
                            this.knightLastMoveOne[2] = this.playerOnePieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                }catch (Exception e)
                {}
            }
            if (pieceType == 3) {
                if (this.playerOnePieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Bishop:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = this.playerOnePieces[piece][4];
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    if (direction == 5){this.display.visualizeMovement(this.playerOnePieces[piece][1],this.playerOnePieces[piece][2], this.playerOnePieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                    activePiece(piece,dimension);
                    }

                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4 || direction == 3) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] - 1] = 51;
                                this.playerOnePieces[piece][1] -= 1;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 1) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] + 1] = 51;
                                this.playerOnePieces[piece][2] += 1;
                                this.playerOnePieces[piece][1] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                                activePiece(piece,dimension);

                            }
                        }


                    }
                    if (dimension == 1)
                    {
                        if (direction == 3 || direction == 4) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] - 1] = 51;
                                this.playerOnePieces[piece][1] += 1;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1 || direction == 2) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] + 1] = 51;
                                this.playerOnePieces[piece][1] -= 1;
                                this.playerOnePieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                                activePiece(piece,dimension);


                            }
                        }

                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 51;
                        this.pieceMovesOne --;
                        this.playerOnePieces[piece][5] += 1;
                        removePiece(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2], piece);
                    }

                } catch (Exception e) {

                }
            }

            if (pieceType == 4) {
                if (this.playerOnePieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Rook:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = this.playerOnePieces[piece][4];
                    if (direction == 5){this.display.visualizeMovement(this.playerOnePieces[piece][1],this.playerOnePieces[piece][2], this.playerOnePieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                        activePiece(piece,dimension);
                    }
                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] - 1] = 53;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] + 1] = 53;
                                this.playerOnePieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 1 || direction == 3)
                        {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                            activePiece(piece,dimension);
                        }
                    }
                    if (dimension == 1)
                    {
                        if (direction == 3) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2]] = 53;
                                this.playerOnePieces[piece][1] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                                activePiece(piece,dimension);
                            }
                        }
                        if (direction == 1) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2]] = 53;
                                this.playerOnePieces[piece][1] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 4)
                        {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                            activePiece(piece,dimension);

                        }
                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 53;
                        this.playerOnePieces[piece][5] += 1;
                        this.pieceMovesOne --;
                        removePiece(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2], piece);
                    }

                } catch (Exception e) {

                }
            }
            if (pieceType == 5) {
                if (this.playerOnePieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {

                    emptyText();

                    stringConcactinate("Queen:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = this.playerOnePieces[piece][4];
                    if (direction == 5){this.display.visualizeMovement(this.playerOnePieces[piece][1],this.playerOnePieces[piece][2], this.playerOnePieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                        activePiece(piece,dimension);
                    }
                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] - 1] = 55;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2] + 1] = 55;
                                this.playerOnePieces[piece][2] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 1 || direction == 3)
                        {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                            activePiece(piece,dimension);
                        }


                    }
                    if (dimension == 1)
                    {
                        if (direction == 3) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2]] = 55;
                                this.playerOnePieces[piece][1] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2]] = 55;
                                this.playerOnePieces[piece][1] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);


                            }
                        }
                        if (direction == 2 || direction == 4)
                        {
                            this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                            activePiece(piece,dimension);

                        }

                    }

                    if (dimension == 4)
                    {
                        if (direction == 4 || direction == 3) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] - 1] = 55;
                                this.playerOnePieces[piece][1] -= 1;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 1) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] + 1] = 55;
                                this.playerOnePieces[piece][2] += 1;
                                this.playerOnePieces[piece][1] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }

                    }
                    if (dimension == 3)
                    {
                        if (direction == 3 || direction == 4) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] + 1, this.playerOnePieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] + 1][this.playerOnePieces[piece][2] - 1] = 55;
                                this.playerOnePieces[piece][1] += 1;
                                this.playerOnePieces[piece][2] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1 || direction == 2) {
                            int canMove = checkSquare(this.playerOnePieces[piece][1] - 1, this.playerOnePieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerOnePieces[piece][1] - 1][this.playerOnePieces[piece][2] + 1] = 55;
                                this.playerOnePieces[piece][1] -= 1;
                                this.playerOnePieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                                activePiece(piece,dimension);


                            }
                        }

                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = 55;
                        this.pieceMovesOne --;
                        this.playerOnePieces[piece][5] += 1;
                        removePiece(this.playerOnePieces[piece][1], this.playerOnePieces[piece][2], piece);
                    }


                } catch (Exception e) {

                }
            }
        }
        if (this.turn == -1) {
            int pieceType = this.playerTwoPieces[piece][0];
            //if (this.playerOneWheat < 1) {
            //  return;
            // }
            if (pieceType == 1) {
                if (this.playerTwoPieces[piece][5] > 7) {
                    this.pieceMovesTwo --;
                    return;
                }
                try {
                    emptyText();
                    String moveBar = "";
                    stringConcactinate("Pawns Movement:");
                    for (int i = this.playerTwoPieces[piece][5] * 2; i < 16; i++) {
                        moveBar += "▓";
                    }
                    stringConcactinate(moveBar);
                    stringConcactinate("");
                    stringConcactinate("Press Enter to exit");
                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = this.playerTwoPieces[piece][4];
                    if (direction == 4) {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] - 1, false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] - 1] = 48;
                            this.playerTwoPieces[piece][2] -= 1;
                            this.playerTwoPieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] - 1] = 48;
                            this.playerTwoPieces[piece][2] -= 1;
                            this.playerTwoPieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 48;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 3) {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2], false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerTwoPieces[piece][1]+1][this.playerTwoPieces[piece][2]] = 48;
                            this.playerTwoPieces[piece][1] += 1;
                            this.playerTwoPieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerTwoPieces[piece][1]+1][this.playerTwoPieces[piece][2]] = 48;
                            this.playerTwoPieces[piece][1] += 1;
                            this.playerTwoPieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 48;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 2) {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] + 1, false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]+ 1] = 48;
                            this.playerTwoPieces[piece][2] += 1;
                            this.playerTwoPieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]+ 1] = 48;
                            this.playerTwoPieces[piece][2] += 1;
                            this.playerTwoPieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 48;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 1) {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2], false, piece);
                        if (canMove == 1) {
                            this.grid[this.playerTwoPieces[piece][1]-1][this.playerTwoPieces[piece][2]] = 48;
                            this.playerTwoPieces[piece][1] -= 1;
                            this.playerTwoPieces[piece][5] += 2;
                            activePiece(piece,0);
                        }
                        if (canMove == 2) {
                            this.grid[this.playerTwoPieces[piece][1]-1][this.playerTwoPieces[piece][2]] = 48;
                            this.playerTwoPieces[piece][1] -= 1;
                            this.playerTwoPieces[piece][5] += 1;
                            activePiece(piece,0);
                        }
                        if (canMove == 0) {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 48;
                            activePiece(piece,0);
                        }
                    }
                    if (direction == 0)
                    {
                        this.pieceMovesTwo --;
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 48;
                        removePiece(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2], piece);
                    }
                } catch (Exception e) {

                }
            }
            if (pieceType == 2)
            {
                if (this.playerTwoPieces[piece][5] > 2)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Knight:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int octagon = dimension;
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    if (direction == 1 || direction == 2){octagon ++;}
                    if (direction == 3 || direction == 4){octagon --;}
                    if (direction == 0){

                        //this.grid[this.playerOnePieces[piece][1]][this.playerOnePieces[piece][2]] = this.playerOnePieces[piece][4];
                        this.playerTwoPieces[piece][1] = this.knightLastMoveTwo[0];
                        this.playerTwoPieces[piece][2] = this.knightLastMoveTwo[1];
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 50;
                        this.playerTwoPieces[piece][5] ++;
                        this.pieceMovesTwo --;
                        removePiece(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2], piece);

                        return;


                    }
                    this.grid[ this.knightLastMoveTwo[0]][this.knightLastMoveTwo[1]] =  this.knightLastMoveTwo[2];
                    if (direction == 5){this.display.visualizeMovement(this.knightLastMoveTwo[0],this.knightLastMoveTwo[1], this.playerTwoPieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);

                    }
                    if (octagon > 8){octagon = 1;}
                    if (octagon < 1){octagon = 8;}
                    if (octagon == 1)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] - 2, this.playerTwoPieces[piece][2] - 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] - 2][this.playerTwoPieces[piece][2] - 1] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] - 2;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] - 1;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];

                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 2)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] - 2, this.playerTwoPieces[piece][2] + 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] - 2][this.playerTwoPieces[piece][2] + 1] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] - 2;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] + 1;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 3)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] + 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] + 2] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] - 1;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] + 2;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 4)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] + 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] + 2] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] + 1;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] + 2;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }

                    if (octagon == 6)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] + 2, this.playerTwoPieces[piece][2] - 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] + 2][this.playerTwoPieces[piece][2] - 1] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] + 2;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] - 1;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {

                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 5)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] + 2, this.playerTwoPieces[piece][2] + 1, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] + 2][this.playerTwoPieces[piece][2] + 1] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] + 2;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] + 1;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {
                            activePiece(piece, octagon);
                        }
                    }

                    if (octagon == 8)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] - 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] - 2] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] - 1;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] - 2;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {

                            activePiece(piece, octagon);
                        }
                    }
                    if (octagon == 7)
                    {
                        int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] - 2, false, piece);
                        if (canMove == 1)
                        {
                            this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] - 2] = 50;
                            this.knightLastMoveTwo[0] = this.playerTwoPieces[piece][1] + 1;
                            this.knightLastMoveTwo[1] = this.playerTwoPieces[piece][2] - 2;
                            this.knightLastMoveTwo[2] = this.playerTwoPieces[piece][4];
                            activePiece(piece, octagon);
                        }
                        if (canMove == 0)
                        {

                            activePiece(piece, octagon);
                        }
                    }
                }catch (Exception e)
                {}
            }
            if (pieceType == 3) {
                if (this.playerTwoPieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Bishop:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = this.playerTwoPieces[piece][4];
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    if (direction == 5){this.display.visualizeMovement(this.playerTwoPieces[piece][1],this.playerTwoPieces[piece][2], this.playerTwoPieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                        activePiece(piece,dimension);
                    }

                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4 || direction == 3) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] - 1] = 52;
                                this.playerTwoPieces[piece][1] -= 1;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 1) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] + 1] = 52;
                                this.playerTwoPieces[piece][2] += 1;
                                this.playerTwoPieces[piece][1] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                                activePiece(piece,dimension);

                            }
                        }


                    }
                    if (dimension == 1)
                    {
                        if (direction == 3 || direction == 4) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] - 1] = 52;
                                this.playerTwoPieces[piece][1] += 1;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1 || direction == 2) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] + 1] = 52;
                                this.playerTwoPieces[piece][1] -= 1;
                                this.playerTwoPieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                                activePiece(piece,dimension);


                            }
                        }

                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 52;
                        this.pieceMovesTwo --;
                        this.playerTwoPieces[piece][5] += 1;
                        removePiece(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2], piece);
                    }

                } catch (Exception e) {

                }
            }

            if (pieceType == 4) {
                if (this.playerTwoPieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {
                    emptyText();

                    stringConcactinate("Rook:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = this.playerTwoPieces[piece][4];
                    if (direction == 5){this.display.visualizeMovement(this.playerTwoPieces[piece][1],this.playerTwoPieces[piece][2], this.playerTwoPieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                        activePiece(piece,dimension);
                    }
                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] - 1] = 54;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] + 1] = 54;
                                this.playerTwoPieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 1 || direction == 3)
                        {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                            activePiece(piece,dimension);
                        }
                    }
                    if (dimension == 1)
                    {
                        if (direction == 3) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2]] = 54;
                                this.playerTwoPieces[piece][1] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                                activePiece(piece,dimension);
                            }
                        }
                        if (direction == 1) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2]] = 54;
                                this.playerTwoPieces[piece][1] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 4)
                        {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                            activePiece(piece,dimension);

                        }
                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 54;
                        this.playerTwoPieces[piece][5] += 1;
                        this.pieceMovesTwo --;
                        removePiece(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2], piece);
                    }

                } catch (Exception e) {

                }
            }
            if (pieceType == 5) {
                if (this.playerTwoPieces[piece][5] > 1)
                {
                    this.display.customInput(this.textGraphics, this.screen,15);
                    return;
                }
                try {

                    emptyText();

                    stringConcactinate("Queen:");
                    stringConcactinate("");
                    stringConcactinate("Press F1 to ");
                    stringConcactinate("Visualize moves ");
                    stringConcactinate("");
                    stringConcactinate("Press Enter to ");
                    stringConcactinate("Finalize Move");

                    refresh();
                    int direction = this.display.movePiece(this.terminal, this.textGraphics, this.screen);
                    this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = this.playerTwoPieces[piece][4];
                    if (direction == 5){this.display.visualizeMovement(this.playerTwoPieces[piece][1],this.playerTwoPieces[piece][2], this.playerTwoPieces[piece][0], this.grid, this.textGraphics, this.screen,this.turn);
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                        activePiece(piece,dimension);
                    }
                    //this.grid[this.PlayerOneKing[0]][this.PlayerOneKing[1]] = this.p1kingstep;
                    if (dimension == 2)
                    {
                        if (direction == 4) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] - 1] = 56;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2] + 1] = 56;
                                this.playerTwoPieces[piece][2] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 1 || direction == 3)
                        {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                            activePiece(piece,dimension);
                        }


                    }
                    if (dimension == 1)
                    {
                        if (direction == 3) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2]] = 56;
                                this.playerTwoPieces[piece][1] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2], false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2]] = 56;
                                this.playerTwoPieces[piece][1] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);


                            }
                        }
                        if (direction == 2 || direction == 4)
                        {
                            this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                            activePiece(piece,dimension);

                        }

                    }

                    if (dimension == 4)
                    {
                        if (direction == 4 || direction == 3) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] - 1] = 56;
                                this.playerTwoPieces[piece][1] -= 1;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece, dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }
                        if (direction == 2 || direction == 1) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] + 1] = 56;
                                this.playerTwoPieces[piece][2] += 1;
                                this.playerTwoPieces[piece][1] += 1;
                                activePiece(piece,dimension);
                                ;
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }

                    }
                    if (dimension == 3)
                    {
                        if (direction == 3 || direction == 4) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] + 1, this.playerTwoPieces[piece][2] - 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] + 1][this.playerTwoPieces[piece][2] - 1] = 56;
                                this.playerTwoPieces[piece][1] += 1;
                                this.playerTwoPieces[piece][2] -= 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);

                            }
                        }

                        if (direction == 1 || direction == 2) {
                            int canMove = checkSquare(this.playerTwoPieces[piece][1] - 1, this.playerTwoPieces[piece][2] + 1, false, piece);
                            if (canMove == 1) {
                                this.grid[this.playerTwoPieces[piece][1] - 1][this.playerTwoPieces[piece][2] + 1] = 56;
                                this.playerTwoPieces[piece][1] -= 1;
                                this.playerTwoPieces[piece][2] += 1;
                                activePiece(piece,dimension);
                            }

                            if (canMove == 0) {
                                this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                                activePiece(piece,dimension);


                            }
                        }

                    }
                    if (direction == 0)
                    {
                        this.grid[this.playerTwoPieces[piece][1]][this.playerTwoPieces[piece][2]] = 56;
                        this.pieceMovesTwo --;
                        this.playerTwoPieces[piece][5] += 1;
                        removePiece(this.playerTwoPieces[piece][1], this.playerTwoPieces[piece][2], piece);
                    }


                } catch (Exception e) {

                }
            }
        }
    }

    public int checkSquare(int xVal, int yVal, boolean king, int piece)
    {
        if (this.turn == 1)
        {
            this.p1KingGarrisoned = false;
            if (king) {
                if (this.grid[xVal][yVal] == 0) {
                    this.p1kingstep = 0;
                    return 1;
                }
                if (this.grid[xVal][yVal] == 22) {
                    this.p1kingstep = 22;
                    return 1;
                }
                if (this.grid[xVal][yVal] == 34) {
                    this.p1kingstep = 34;
                    return 2;
                }

                if (this.kingOneInventory.size() < 3) {
                if (this.grid[xVal][yVal] == 23) {
                    this.p1kingstep = 0;

                        this.kingOneInventory.add("Δ");

                    return 1;
                }
                if (this.grid[xVal][yVal] == 42) {
                    this.p1kingstep = 0;

                        this.kingOneInventory.add("▿");

                    return 1;
                }
                if (this.grid[xVal][yVal] == 43) {
                    this.p1kingstep = 0;

                        this.kingOneInventory.add("°");

                    return 1;
                }
                if (this.grid[xVal][yVal] == 44) {
                    this.p1kingstep = 0;

                        this.kingOneInventory.add("✦");

                    return 1;
                }
                if (this.grid[xVal][yVal] == 20) {
                    this.p1kingstep = 0;

                        this.kingOneInventory.add("♦");

                    return 1;
                }
                }
                if (this.grid[xVal][yVal] > 0 && this.grid[xVal][yVal] < 10) {
                    this.p1kingstep = this.grid[xVal][yVal];
                    this.p1KingGarrisoned = true;
                    return 3;
                }
                if (this.grid[xVal][yVal] == 48 || this.grid[xVal][yVal] == 50 || this.grid[xVal][yVal] == 52 || this.grid[xVal][yVal] == 54 || this.grid[xVal][yVal] == 56)
                {
                    this.p1kingstep = this.grid[xVal][yVal];
                    return 1;}
            }
            else {
                if (this.grid[xVal][yVal] == 48 || this.grid[xVal][yVal] == 50 || this.grid[xVal][yVal] == 52 || this.grid[xVal][yVal] == 54
                        || this.grid[xVal][yVal] == 56 || this.grid[xVal][yVal] == 30 || this.grid[xVal][yVal] == 32 || this.grid[xVal][yVal] == 46)
                {
                    this.playerOnePieces[piece][4] = this.grid[xVal][yVal]; return 1;}
                if (this.grid[xVal][yVal] == 0 ){ this.playerOnePieces[piece][4] = 0; return 1;}
                if (this.grid[xVal][yVal] == 22){ this.playerOnePieces[piece][4] = 22; return 1;}
                if (this.playerOnePieces[piece][0] == 1)
                {
                    if (this.grid[xVal][yVal] == 34) {
                        this.playerOnePieces[piece][4] = 34;
                        return 2;
                    }
                }
                else {if (this.grid[xVal][yVal] == 34){ this.playerOnePieces[piece][4] = 34; return 1;}
                }
            }

        }
        if (this.turn == -1)
        {
            this.p2KingGarrisoned = false;
            if (king) {
                if (this.grid[xVal][yVal] == 0 ){ this.p2kingstep = 0; return 1;}
                if (this.grid[xVal][yVal] == 22){ this.p2kingstep = 22; return 1;}
                if (this.grid[xVal][yVal] == 33){ this.p2kingstep = 33; return 2;}

                if (this.kingTwoInventory.size() < 3) {
                    if (this.grid[xVal][yVal] == 23) {
                        this.p2kingstep = 0;

                            this.kingTwoInventory.add("Δ");

                        return 1;
                    }
                    if (this.grid[xVal][yVal] == 42) {
                        this.p2kingstep = 0;

                            this.kingTwoInventory.add("▿");

                        return 1;
                    }
                    if (this.grid[xVal][yVal] == 43) {
                        this.p2kingstep = 0;
                            this.kingTwoInventory.add("°");

                        return 1;
                    }
                    if (this.grid[xVal][yVal] == 44) {
                        this.p2kingstep = 0;

                            this.kingTwoInventory.add("✦");

                        return 1;
                    }
                    if (this.grid[xVal][yVal] == 20) {
                        this.p2kingstep = 0;
                            this.kingTwoInventory.add("♦");

                        return 1;
                    }
                }
                if (this.grid[xVal][yVal] > 9 && this.grid[xVal][yVal] < 19) {
                    this.p2kingstep = this.grid[xVal][yVal];
                    this.p2KingGarrisoned = true;
                    return 3;
                }
                if (this.grid[xVal][yVal] == 47 || this.grid[xVal][yVal] == 49 || this.grid[xVal][yVal] == 51 || this.grid[xVal][yVal] == 53 || this.grid[xVal][yVal] == 54)
                {
                    this.p2kingstep = this.grid[xVal][yVal];
                    return 1;}
            }
            else {
                if (this.grid[xVal][yVal] == 47 || this.grid[xVal][yVal] == 49 || this.grid[xVal][yVal] == 51 || this.grid[xVal][yVal] == 53
                        || this.grid[xVal][yVal] == 55 || this.grid[xVal][yVal] == 57 || this.grid[xVal][yVal] == 58  || this.grid[xVal][yVal] == 45)
                {
                    this.playerTwoPieces[piece][4] = this.grid[xVal][yVal]; return 1;}
                if (this.grid[xVal][yVal] == 0 ){ this.playerTwoPieces[piece][4] = 0; return 1;}
                if (this.grid[xVal][yVal] == 22){ this.playerTwoPieces[piece][4] = 22; return 1;}
                if (this.playerTwoPieces[piece][0] == 1)
                {
                    if (this.grid[xVal][yVal] == 33) {
                        this.playerTwoPieces[piece][4] = 33;
                        return 2;
                    }
                }
                else {if (this.grid[xVal][yVal] == 33){ this.playerTwoPieces[piece][4] = 33; return 1;}
                }
            }

        }
        return 0;
    }
    public void removePiece(int xVal, int yVal,int piece)
    {
        if (this.turn == 1)
        {
            for (int i = 0; i < this.playerTwoPieces.length; i++)
            {
                if (this.playerTwoPieces[i][1]== xVal && this.playerTwoPieces[i][2]== yVal)
                {
                    if (this.playerOnePieces[piece][0] == 2)
                    {
                        this.knightLastMoveOne[2] = this.playerTwoPieces[i][4];;
                    }
                    else {this.playerOnePieces[piece][4] = this.playerTwoPieces[i][4];}
                    this.playerTwoPieces[i][0] = 0;
                    this.playerTwoPieces[i][1] = 0;
                    this.playerTwoPieces[i][2] = 0;
                    this.playerTwoPieces[i][3] = 0;
                    this.playerTwoPieces[i][4] = 0;
                    this.playerTwoPieces[i][5] = 0;

                }
            }
            for (int i = 0; i < this.playerTwoNodeTracker.length; i++)
            {
                if (this.playerTwoNodeTracker[i][1]== xVal && this.playerTwoNodeTracker[i][2]== yVal)
                {

                    this.playerTwoNodeTracker[i][4] = 0;
                    this.playerOnePieces[piece][4] = 33;

                }
            }
            if (xVal == this.PlayerTwoKing[0] && yVal == this.PlayerTwoKing[1])
            {
                this.display.winAnimationOne(this.textGraphics,this.screen);
            }
        }
        if (this.turn == -1)
        {
            for (int i = 0; i < this.playerOnePieces.length; i++)
            {
                if (this.playerOnePieces[i][1]== xVal && this.playerOnePieces[i][2]== yVal)
                {
                    if (this.playerTwoPieces[piece][0] == 2)
                    {
                        this.knightLastMoveTwo[2] = this.playerOnePieces[i][4];;
                    }
                    else {this.playerTwoPieces[piece][4] = this.playerOnePieces[i][4];}
                    this.playerOnePieces[i][0] = 0;
                    this.playerOnePieces[i][1] = 0;
                    this.playerOnePieces[i][2] = 0;
                    this.playerOnePieces[i][3] = 0;
                    this.playerOnePieces[i][4] = 0;
                    this.playerOnePieces[i][5] = 0;

                }
            }
            for (int i = 0; i < this.playerOneNodeTracker.length; i++)
            {
                if (this.playerOneNodeTracker[i][1]== xVal && this.playerOneNodeTracker[i][2]== yVal)
                {

                    this.playerOneNodeTracker[i][4] = 0;
                    this.playerTwoPieces[piece][4] = 34;

                }
            }
            if (xVal == this.PlayerOneKing[0] && yVal == this.PlayerOneKing[1])
            {
                this.display.winAnimationTwo(this.textGraphics,this.screen);
            }
        }
    }
    public void deployPiece(int xVal, int yVal, int pieceCode)
    {
        if (xVal == 0 || yVal == 0){return;}
        boolean deployed = false;
        int charCode = 0;
        if (this.turn == 1) {
            if (pieceCode == 1) {charCode = 47;}
            if (pieceCode == 2) { charCode = 49;}
            if (pieceCode == 3) { charCode = 51;}
            if (pieceCode == 4) { charCode = 53;}
            if (pieceCode == 5) { charCode = 55;}
        }
        if (this.turn == -1) {
            if (pieceCode == 1) {charCode = 48;}
            if (pieceCode == 2) { charCode = 50;}
            if (pieceCode == 3) { charCode = 52;}
            if (pieceCode == 4) { charCode = 54;}
            if (pieceCode == 5) { charCode = 56;}
        }
        if(this.grid[xVal+1][yVal+1] == 0  || this.grid[xVal+1][yVal+1] == 22) {
            this.grid[xVal+1][yVal+1] = charCode;deployed = true; setPieceInfo(xVal + 1,yVal + 1, pieceCode); }
        if(this.grid[xVal+1][yVal] == 0  && !deployed|| this.grid[xVal+1][yVal] == 22 && !deployed) {
            this.grid[xVal+1][yVal] = charCode;deployed = true; setPieceInfo(xVal + 1,yVal, pieceCode);}
        if(this.grid[xVal+1][yVal-1] == 0 && !deployed|| this.grid[xVal+1][yVal-1] == 22 && !deployed) {
            this.grid[xVal+1][yVal-1] = charCode; deployed = true;setPieceInfo(xVal + 1,yVal -1, pieceCode);}
        if(this.grid[xVal][yVal+1] == 0 && !deployed|| this.grid[xVal][yVal+1] == 22 && !deployed) {
            this.grid[xVal][yVal+1] = charCode; deployed = true;setPieceInfo(xVal,yVal+1, pieceCode);}
        if(this.grid[xVal][yVal-1] == 0&& !deployed || this.grid[xVal][yVal-1] == 22 && !deployed) {
            this.grid[xVal][yVal-1] = charCode;deployed = true; setPieceInfo(xVal,yVal-1, pieceCode);}
        if(this.grid[xVal-1][yVal+1] == 0 && !deployed|| this.grid[xVal-1][yVal+1] == 22 && !deployed) {
            this.grid[xVal-1][yVal+1] = charCode; deployed = true;setPieceInfo(xVal - 1,yVal +1, pieceCode);}
        if(this.grid[xVal-1][yVal] == 0 && !deployed|| this.grid[xVal-1][yVal] == 22 && !deployed) {
            this.grid[xVal-1][yVal] = charCode;deployed = true; setPieceInfo(xVal - 1,yVal, pieceCode);}
        if(this.grid[xVal-1][yVal-1] == 0 && !deployed|| this.grid[xVal-1][yVal-1] == 22 && !deployed) {
            this.grid[xVal-1][yVal-1] = charCode; setPieceInfo(xVal - 1,yVal-1, pieceCode);}


    }
    public void setPieceInfo(int xVal, int yVal, int pieceCode)
    {
        if (this.turn == 1)
        {
            for (int i = 0; i < this.playerOnePieces.length; i++)
            {
                if (this.playerOnePieces[i][3] == 0 && this.playerOnePieces[i][0] == pieceCode)
                {
                    this.playerOnePieces[i][1] = xVal;
                    this.playerOnePieces[i][2] = yVal;
                    this.playerOnePieces[i][3] = 1;
                }
                if (this.playerOnePieces[i][0] == 2)
                {
                    this.knightLastMoveOne[0] = xVal;
                    this.knightLastMoveOne[1] = yVal;
                    this.knightLastMoveOne[2] = 0;
                }
            }
        }
        if (this.turn == -1)
        {
            for (int i = 0; i < this.playerTwoPieces.length; i++)
            {
                if (this.playerTwoPieces[i][3] == 0 && this.playerTwoPieces[i][0] == pieceCode)
                {
                    this.playerTwoPieces[i][1] = xVal;
                    this.playerTwoPieces[i][2] = yVal;
                    this.playerTwoPieces[i][3] = 1;
                }
                if (this.playerTwoPieces[i][0] == 2)
                {
                    this.knightLastMoveTwo[0] = xVal;
                    this.knightLastMoveTwo[1] = yVal;
                    this.knightLastMoveTwo[2] = 0;
                }
            }
        }
    }
    public void resetMoves()
    {
        if (this.turn == 1)
        {
            for (int i = 0; i < this.playerOnePieces.length; i++)
            {
                this.playerOnePieces[i][5] = 0;
            }
        }
        if (this.turn == -1)
        {
            for (int i = 0; i < this.playerTwoPieces.length; i++)
            {
                this.playerTwoPieces[i][5] = 0;
            }
        }

    }
    public void buildingResources()
    {
        if (this.turn == 1)
        {
            for(int i = 0; i < this.playerOneCities.length; i++)
            {
                if (this.playerOneCities[i][5] == 2){this.playerOneWheat += 2;}
                if (this.playerOneCities[i][5] == 3){this.playerOneLumber += 3;}
                if (this.playerOneCities[i][5] == 4){this.playerOneStone += 5;}
            }
        }
        if (this.turn == -1)
        {
            for(int i = 0; i < this.playerTwoCities.length; i++)
            {
                if (this.playerTwoCities[i][5] == 2){this.playerTwoWheat += 2;}
                if (this.playerTwoCities[i][5] == 3){this.playerTwoLumber += 3;}
                if (this.playerTwoCities[i][5] == 4){this.playerTwoStone += 5;}
            }
        }
    }

    public void manageCities(){
        if (this.turn == 1) {
            int list = 0;
            emptyText();
        stringConcactinate("Which settlement do");
        stringConcactinate("you want to manage?");
        for (int i = 0; i < playerOneCities.length; i++)
        {
            if(playerOneCities[i][0] != 0){
                list ++;
                stringConcactinate(list + ")Settlement " + printSpecialCharacter(i + 1));
            }
        }
        stringConcactinate("0) Back");
        int settlement = trycatch(false,2,list + 1, 0, false, null, 0);
        if (settlement == 0){return;}
        if (this.playerOneCities[settlement-1][5] == 0)
        {
            emptyText();
            stringConcactinate("Available Add Ons ");
            stringConcactinate("for Settlement");
            stringConcactinate("1) Barracks: ");
            stringConcactinate("   10 stone 5 lumber ");
            stringConcactinate("");
            stringConcactinate("2) Granary: ");
            stringConcactinate("   20 stone 10 lumber");
            stringConcactinate("   2 rubies");
            stringConcactinate("3) Lumbermill: ");
            stringConcactinate("   20 stone 10 lumber");
            stringConcactinate("   1 pearl");
            stringConcactinate("4) Quarry: ");
            stringConcactinate("   10 lumber 10 wheat");
            stringConcactinate("   1 pearl");
            stringConcactinate("0) Back");
            int building = trycatch(false,0,5,2, false, null, 0);
            if (building == 1)
            {
                if (canAfford(10,0,0,5,0,0))
                {
                    this.playerOneCities[settlement-1][5] = 1;
                    this.playerOneStone -= 10;
                    this.playerOneLumber -= 5;

                } }
            if (building == 2)
            {
                if (canAfford(20,0,2,10,0,0))
                {
                    this.playerOneCities[settlement-1][5] = 2;
                    this.playerOneStone -= 20;
                    this.playerOneLumber-= 10;
                    this.playerOneRubys -= 2;


                }
            }
            if (building == 3)
            {
                if (canAfford(20,0,0,10,1,0))
                {
                    this.playerOneCities[settlement-1][5] = 3;
                    this.playerOneStone -= 20;
                    this.playerOneLumber -= 10;
                    this.playerOnePearls -= 1;


                } }
            if (building == 4)
            {
                if (canAfford(0,10,0,10,1,0))
                { this.playerOneCities[settlement-1][5] = 4;
                  this.playerOneWheat -= 10;
                  this.playerOneLumber -= 10;
                  this.playerOnePearls -= 1;
                } }
        }
        else {
            if(this.playerOneCities[settlement-1][5] == 1)
            {
                emptyText();
                stringConcactinate("This settlement has ");
                stringConcactinate("a barracks. Can deploy ");
                stringConcactinate("Army pieces ");
                stringConcactinate("0) Back ");

            }
            if(this.playerOneCities[settlement-1][5] == 2)
            {
                emptyText();
                stringConcactinate("This settlement has ");
                stringConcactinate("a Granary.");
                stringConcactinate("+ 2 wheat per turn ");
                stringConcactinate("0) Back ");
            }
            if(this.playerOneCities[settlement-1][5] == 3)
            {
                emptyText();
                stringConcactinate("This settlement has ");
                stringConcactinate("a Lumbermill. ");
                stringConcactinate("+ 3 Lumber per turn ");
                stringConcactinate("0) Back ");
            }
            if(this.playerOneCities[settlement-1][5] == 4)
            {
                emptyText();
                stringConcactinate("This settlement has ");
                stringConcactinate("a quarry. ");
                stringConcactinate("+ 5 Stone per turn ");
                stringConcactinate("0) Back ");
            }
            trycatch(false,3,1,0, false, null, 0);

        }

        }
        if (this.turn == -1) {
            int list = 0;
            emptyText();
            stringConcactinate("Which settlement do");
            stringConcactinate("you want to manage?");
            for (int i = 0; i < playerTwoCities.length; i++)
            {
                if(playerTwoCities[i][0] != 0){
                    list ++;
                    stringConcactinate(list + ")Settlement " + printSpecialCharacter(i + 10));
                }
            }
            stringConcactinate("0) Back");
            int settlement = trycatch(false,2,list + 1, 0, false, null, 0);
            if (settlement == 0){return;}
            if (this.playerTwoCities[settlement-1][5] == 0)
            {
                emptyText();
                stringConcactinate("Available Add ons ");
                stringConcactinate("for Settlement ");
                stringConcactinate("1) Barracks: ");
                stringConcactinate("   10 stone 5 lumber ");
                stringConcactinate("");
                stringConcactinate("2) Granary: ");
                stringConcactinate("   20 stone 10 lumber");
                stringConcactinate("   2 rubies");
                stringConcactinate("3) Lumbermill: ");
                stringConcactinate("   20 stone 10 lumber");
                stringConcactinate("   1 pearl");
                stringConcactinate("4) Quarry: ");
                stringConcactinate("   10 lumber 10 wheat");
                stringConcactinate("   1 pearl");
                stringConcactinate("0) Back");
                int building = trycatch(false,0,5,2, false, null, 0);
                if (building == 1)
                {
                    if (canAfford(10,0,0,5,0,0))
                    {
                        this.playerTwoCities[settlement-1][5] = 1;
                        this.playerTwoStone -= 10;
                        this.playerTwoLumber -= 5;

                    } }
                if (building == 2)
                {
                    if (canAfford(20,0,2,10,0,0))
                    { this.playerTwoCities[settlement-1][5] = 2;
                        this.playerTwoStone -= 20;
                        this.playerTwoLumber -= 10;
                        this.playerTwoRubys -= 2;} }
                if (building == 3)
                {
                    if (canAfford(20,0,0,10,1,0))
                    {  this.playerTwoCities[settlement-1][5] = 3;
                        this.playerTwoStone -= 20;
                        this.playerTwoLumber -= 10;
                        this.playerTwoPearls -= 1;} }
                if (building == 4)
                {
                    if (canAfford(0,10,0,10,1,0))
                    {this.playerOneCities[settlement-1][5] = 4;
                        this.playerTwoWheat -= 10;
                        this.playerTwoLumber -= 10;
                        this.playerTwoPearls -= 1; } }
            }
            else {
                if(this.playerTwoCities[settlement-1][5] == 1)
                {
                    emptyText();
                    stringConcactinate("This settlement has ");
                    stringConcactinate("a barracks. Can deploy ");
                    stringConcactinate("Army pieces ");
                    stringConcactinate("0) Back ");

                }
                if(this.playerTwoCities[settlement-1][5] == 2)
                {
                    emptyText();
                    stringConcactinate("This settlement has ");
                    stringConcactinate("a Granary. + 2 wheat per turn ");
                    stringConcactinate("0) Back ");
                }
                if(this.playerTwoCities[settlement-1][5] == 3)
                {
                    emptyText();
                    stringConcactinate("This settlement has ");
                    stringConcactinate("a Lumbermill. ");
                    stringConcactinate("+ 3 Lumber per turn ");
                    stringConcactinate("0) Back ");
                }
                if(this.playerTwoCities[settlement-1][5] == 4)
                {
                    emptyText();
                    stringConcactinate("This settlement has ");
                    stringConcactinate("a quarry. ");
                    stringConcactinate("+ 5 Stone per turn ");
                    stringConcactinate("0) Back ");
                }
                trycatch(false,3,1,0, false, null, 0);
                //if (runt == 0){return;}
                //else {return;}
            }
        }
    }

    public void refresh(){
        try {
            int scroll = -1;
            if (this.turn == 1)
            {
                scroll = this.display.getMapScroll1();
            }
            if (this.turn == -1)
            {
                scroll = this.display.getMapScroll2();
            }

            this.display.checkerBoard(this.textGraphics);
            this.display.emptyText(this.textGraphics);
            this.display.render2(this.grid,this.screen,this.textGraphics,-1,scroll,0);
            this.display.renderText(this.screen,this.textGraphics,this.text, this.textBorder,false);
        }
        catch (Exception e){  System.out.println("Something bad happened: " + e.getMessage());}

    }
    public String printSpecialCharacter(int key)
    {
        if(key == 1){return "A";}
        if(key == 2){return("B");}
        if(key == 3){return("C");}
        if(key == 4){return ("Å");}
        if(key == 5){return ("ß");}
        if(key == 6){return ("Ç");}
        if(key == 7){return ("Ä");}
        if(key == 8){return ("฿");}
        if(key == 9){return ("¢");}
        if(key == 10){return ("X");}
        if(key == 11){return ("Y");}
        if(key == 12){return ("Z");}
        if(key == 13){return ("⌘");}
        if(key == 14){return ("¥");}
        if(key == 15){return ("ζ");}
        if(key == 16){return ("✥");}
        if(key == 17){return ("ϒ");}
        if(key == 18){return ("ϟ");}
        if(key == 19){return (" Right");}
        if(key == 20){return (" Down");}
        if(key == 21){return (" Left");}
        if(key == 22){return (" Up");}
        if(key == 23){return ("☭");}
        if(key == 24){return ("⛏");}
        return " ";


    }

    public int[] FindNode(int key) {
        int[] grid = {0,0};
        for (int i = 0; i < 34; i++) {
            for (int x = 0; x < 200; x++) {
                if (this.grid[i][x] == key){
                    grid[0]= i;
                    grid[1] = x;
                    return grid;
                }
            }
        }
        return grid;
    }

    public void WrongInput(){
        this.display.customInput(this.textGraphics,this.screen, 0);

        mainMenu();
    }

    public int trycatch(boolean mainMenu, int offset, int list, int skip, boolean jump, int[][] jumpLocations, int type)
    {
        try {

            System.out.println("list" + list);
            System.out.println("offset" + offset);
            int scroll = -1;
            if (this.turn == 1)
            {
                scroll = this.display.getMapScroll1();
            }
            if (this.turn == -1)
            {
                scroll = this.display.getMapScroll2();
            }
            int answer = 0;
            this.display.checkerBoard(this.textGraphics);
            this.display.emptyText(this.textGraphics);
            this.display.render2(this.grid,this.screen,this.textGraphics,-1,scroll,0);
            this.display.renderText(this.screen,this.textGraphics,this.text, this.textBorder, false);
            String alien = this.display.getInput(this.terminal, this.textGraphics, this.screen, this.grid, this.text, this.textBorder, this.turn, offset, list, skip, mainMenu, jump, jumpLocations, type);

            System.out.println(alien);
            try{
                answer = Integer.parseInt(alien);
            }
            catch (NumberFormatException e)
            {

                System.out.println("not good input ");
                System.out.println(answer);

            }
            return answer;

        }
        catch (Exception e){
            return 0;}

    }
    public String PrintCustomSpaces(int offset, int resource){
        int defaultSpace = 91;
        String spaces = "";
        if(resource > 9 )
        {
            defaultSpace = 90;
        }
        if(resource > 99 )
        {
            defaultSpace = 89;
        }
        if(resource > 999 )
        {
            defaultSpace = 88;
        }
        for(int i = 0; i < defaultSpace - offset; i ++ )
        {
            spaces += " ";
        }
        if (this.turn == 1)
            spaces += "│";
        if (this.turn == -1)
            spaces += "•";
        return spaces;

    }
    public int[][] getMap()
    {
        return this.grid;
    }
    public ArrayList<String> getText()
    {
        return this.text;
    }
    public void WinAnimation(int team)
    {
        String[] art;
        art = new String[3000];

        int king = 50;
        int queen = 50;
        int checker = - 1;
        boolean swap = false;
        boolean bottom = false;


        for (int i = 0; i < 3000; i++)
        {
            String animationSliver = " ";
            boolean textBox = false;
            for (int x = 0; x < 101; x ++)
            {
                boolean inside = false;
                if (x > king && x < queen || x > queen && x < king)
                {
                    inside = true;
                }
                if (king == x)
                {
                    if (team == 1)
                    {
                        animationSliver += "♚";
                    }
                    else { animationSliver += "✹";}

                }
                else if (queen == x)
                {
                    if (team == 1)
                    {
                        animationSliver += "♛";
                    }
                    else { animationSliver += "☪";}
                }
                else {
                    if (inside)
                    {
                        if (x > 20 && !textBox)
                        {
                            if (queen == 1 || king == 1)
                            {
                                if (!bottom)
                                {
                                    animationSliver += "╔==========================================================╗";
                                }
                                else
                                {
                                    animationSliver += "╚==========================================================╝";
                                }

                                x += 60;
                                bottom = !bottom;
                                textBox = true;

                            }
                            if (queen == 0 || king == 0)
                            {
                                if (team == 1)
                                {
                                    animationSliver += "║  ♦ ♦ ♦ ♦ ♦ ♦ ♦ Player One Wins the Game! ♦ ♦ ♦ ♦ ♦ ♦ ♦ ║";
                                }
                                if (team == 2)
                                {
                                    animationSliver += "║  ♦ ♦ ♦ ♦ ♦ ♦ ♦ Player Two Wins the Game! ♦ ♦ ♦ ♦ ♦ ♦ ♦ ║";
                                }
                                textBox = true;
                                x += 60;
                            }
                        }
                        animationSliver += GetRandomChar(team);
                    }
                    else {
                        checker *= -1;
                        if (checker == 1)
                        {
                            animationSliver += '-';
                        }
                        else {
                            animationSliver += '|';
                        }
                    }
                }
            }
            if (swap == false)
            {
                king ++;
                queen --;
            }
            else
            {
                king --;
                queen ++;
            }
            if (king > 99 || king < 1)
            {
                swap = !swap;
            }
            art[i] = animationSliver;

            System.out.println(animationSliver);
            try
            {
                Thread.sleep(80);
            }catch (InterruptedException ie)
            {
                Thread.currentThread().interrupt();
            }

        }


    }
    public String GetRandomChar(int team)
    {
        Random rand = new Random();
        String returnChar = " ";

        int randInt = rand.nextInt(130);

        if (team == 1)
        {
            if (randInt == 1){returnChar = ("A");}
            if (randInt == 2){returnChar = ("B");}
            if (randInt == 3){returnChar = ("C");}
            if (randInt == 4){returnChar = ("Å");}
            if (randInt == 5){returnChar = ("ß");}
            if (randInt == 6){returnChar = ("Ç");}
            if (randInt == 7){returnChar = ("Ä");}
            if (randInt == 8){returnChar = ("฿");}
            if (randInt == 9){returnChar = ("¢");}

        }
        if (team == 2)
        {

            if (randInt == 10){returnChar = ("\u001B[31m" + "X"+ "\033[0m");}
            if (randInt == 11){returnChar = ("\u001B[31m" +"Y"+ "\033[0m");}
            if (randInt == 12){returnChar = ("\u001B[31m" +"Z"+ "\033[0m");}
            if (randInt == 13){returnChar = ("\u001B[31m" +"⌘"+ "\033[0m");}
            if (randInt == 14){returnChar = ("\u001B[31m" +"¥"+ "\033[0m");}
            if (randInt == 15){returnChar = ("\u001B[31m" +"ζ"+ "\033[0m");}
            if (randInt == 16){returnChar = ("\u001B[31m" +"✥"+ "\033[0m");}
            if (randInt == 17){returnChar = ("\u001B[31m" +"ϒ"+ "\033[0m");}
            if (randInt == 18){returnChar = ("\u001B[31m" +"ϟ"+ "\033[0m");}
        }

        if (randInt == 19){returnChar = ("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
        if (randInt == 20){returnChar = ("\u001B[36m" + "♦" + "\033[0m");}//prints Diamiond
        if (randInt == 21){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 22){returnChar = ("~");}//prints grass
        if (randInt == 23){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 24){returnChar = (" ");}//prints vertical line
        if (randInt == 25){returnChar = (" ");}//Prints vertical wall
        if (randInt == 26){returnChar = ("=");}//prints horizantal wall
        if (randInt == 27){returnChar = (" ");}//prints horizantal line
        if (randInt == 28){returnChar = (" ");}//prints horizantal line
        if (randInt == 29){returnChar = ("·");}//prints soilder
        if (randInt == 30){returnChar = ("☭");}//prints worker
        if (randInt == 31){returnChar = (" ");}//prints monster
        if (randInt == 32){returnChar = ("⚔");}//prints soilder
        if (randInt == 33){returnChar = ("•");}//prints playerTwoRoad
        if (randInt == 34){returnChar = ("≡");}//prints playerOneRoad
        if (randInt == 35){returnChar =(" ");}//prints Tower
        if (randInt == 36){returnChar =(" ");}//prints Reaped land
        if (randInt == 37){returnChar =(" ");}//prints corner
        if (randInt == 38){returnChar =(" ");}//prints corner
        if (randInt == 39){returnChar =(" ");}//prints corner
        if (randInt == 40){returnChar =(" ");}//prints corner
        if (randInt == 41){returnChar =(" ");}//prints Reaped land
        if (randInt == 42){returnChar = ( "*");}//Prints stone
        if (randInt == 43){returnChar = ("\u001B[36m" + "♦" + "\033[0m");}//prints Diamiond
        if (randInt == 44){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 45){returnChar = ("~");}//prints grass
        if (randInt == 46){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 47){returnChar = ("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
        if (randInt == 48){returnChar = ("\u001B[36m" + "♦" + "\033[0m");}//prints Diamiond
        if (randInt == 49){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 50){returnChar = ("~");}//prints grass
        if (randInt == 51){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 52){returnChar = (" ");}//prints vertical line
        if (randInt == 53){returnChar = (" ");}//Prints vertical wall
        if (randInt == 54){returnChar = ("=");}//prints horizantal wall
        if (randInt == 55){returnChar = (" ");}//prints horizantal line
        if (randInt == 56){returnChar = (" ");}//prints horizantal line
        if (randInt == 57){returnChar = ("·");}//prints soilder
        if (randInt == 58){returnChar = ("☭");}//prints worker
        if (randInt == 59){returnChar = (" ");}//prints monster
        if (randInt == 60){returnChar = ("⚔");}//prints soilder
        if (randInt == 61){returnChar = ("•");}//prints playerTwoRoad
        if (randInt == 62){returnChar = ("~");}//prints playerOneRoad
        if (randInt == 70){returnChar =("~");}//prints Reaped land
        if (randInt == 71){returnChar = ("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
        if (randInt == 72){returnChar = ("\u001B[36m" + "♦" + "\033[0m");}//prints Diamiond
        if (randInt == 73){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 74){returnChar = ("~");}//prints grass
        if (randInt == 75){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 76){returnChar = (" ");}//prints vertical line
        if (randInt == 77){returnChar = (" ");}//Prints vertical wall
        if (randInt == 78){returnChar = ("!");}//prints horizantal wall
        if (randInt == 79){returnChar = ("?");}//prints horizantal line
        if (randInt == 80){returnChar = ("$");}//prints horizantal line
        if (randInt == 81){returnChar = ("·");}//prints soilder
        if (randInt == 82){returnChar = ("☭");}//prints worker
        if (randInt == 83){returnChar = (" ");}//prints monster
        if (randInt == 84){returnChar = ("⚔");}//prints soilder
        if (randInt == 85){returnChar = ("•");}//prints playerTwoRoad
        if (randInt == 86){returnChar = ("≡");}//prints playerOneRoad
        if (randInt == 87){returnChar =(" ");}//prints Tower
        if (randInt == 88){returnChar =(" ");}//prints Reaped land
        if (randInt == 89){returnChar =(" ");}//prints corner
        if (randInt == 90){returnChar =(" ");}//prints corner
        if (randInt == 91){returnChar =(" ");}//prints corner
        if (randInt == 92){returnChar =(" ");}//prints corner
        if (randInt == 93){returnChar =(" ");}//prints Reaped land
        if (randInt == 94){returnChar = ("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
        if (randInt == 95){returnChar = (" ");}//prints Diamiond
        if (randInt == 96){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 97){returnChar = ("~");}//prints grass
        if (randInt == 98){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 99){returnChar = ("\u001B[37m" + "*"+ "\033[0m");}//Prints stone
        if (randInt == 100){returnChar = ("o");}//prints Diamiond
        if (randInt == 101){returnChar = ( "\u001B[33m" + "#"+ "\033[0m" );}//Prints wheat
        if (randInt == 102){returnChar = ("~");}//prints grass
        if (randInt == 103){returnChar = ("\u001B[32m"	+ "Δ" + "\033[0m" );}//prints tree
        if (randInt == 104){returnChar = ("!");}//prints vertical line
        if (randInt == 105){returnChar = ("?");}//Prints vertical wall
        if (randInt == 106){returnChar = ("=");}//prints horizantal wall
        if (randInt == 107){returnChar = ("^");}//prints horizantal line
        if (randInt == 108){returnChar = (" ");}//prints horizantal line
        if (randInt == 109){returnChar = ("·");}//prints soilder
        if (randInt == 110){returnChar = ("☭");}//prints worker
        if (randInt == 111){returnChar = (" ");}//prints monster
        if (randInt == 112){returnChar = ("⚔");}//prints soilder
        if (randInt == 113){returnChar = ("•");}//prints playerTwoRoad
        if (randInt == 114){returnChar = ("~");}//prints playerOneRoad
        if (randInt == 115){returnChar =("~");}//prints Reaped land
        if (randInt >= 116){returnChar =(" ");}//prints Tower


        return returnChar;
    }
    public void gameInfo()
    {
        emptyText();
        stringConcactinate("      Game Info");
        stringConcactinate("");
        stringConcactinate("To win the game you ");
        stringConcactinate("must either capture ");
        stringConcactinate("the enemy king, or ");
        stringConcactinate("bring the diamond in ");
        stringConcactinate("the labyrinth to one");
        stringConcactinate("of your settlements. ");
        stringConcactinate("");
        stringConcactinate("Learn about");
        stringConcactinate("1) Pieces");
        stringConcactinate("2) Units");
        stringConcactinate("3) Resources");
        stringConcactinate("0) Back");
        int path = trycatch(false,10,4,0, false, null, 0);
        if (path == 0){return;}
        if (path == 1)
        {
            emptyText();
            stringConcactinate("      Pieces");

            stringConcactinate("♚) King - can move  ");
            stringConcactinate("6 squares in any dire- ");
            stringConcactinate("ction.Moves at 2X speed");
            stringConcactinate("on roads.Picks up items ");
            stringConcactinate("cant take king/units");
            stringConcactinate("♟) Pawn - can move 4");
            stringConcactinate("squares in any direct- ");
            stringConcactinate("ion, 2x speed on roads. ");
            stringConcactinate("♞) Knight-max 3 moves*");
            stringConcactinate("♝) Bishop-max 2 moves*");
            stringConcactinate("♜) Rook-max 2 moves*");
            stringConcactinate("♛) Queen-max 2 moves*");
            stringConcactinate("* Max moves per turn");
            stringConcactinate("0) Back");
            int ty = trycatch(false,0,0, 0, false, null, 0);
            if (ty == 0){gameInfo();}
            else {return;}
        }
        if (path == 2)
        {
            emptyText();
            stringConcactinate("      Units");

            stringConcactinate("☭) Worker-mines stone  ");
            stringConcactinate("and wheat. Is placed at  ");
            stringConcactinate("the end of a road.  ");
            stringConcactinate("Can be taken by pieces ");
            stringConcactinate("⛏) Miner-Mines through");
            stringConcactinate("enemy roads. Is placed at ");
            stringConcactinate("the end of a road. ");
            stringConcactinate("Can be taken by pieces ");
            stringConcactinate("A-C/X-Z) Settlements");
            stringConcactinate("can create up to 4 roads");
            stringConcactinate("Kings bring gems/trees");
            stringConcactinate("to settlements to amass");
            stringConcactinate("them.Can garrisons kings.");
            stringConcactinate("0) Back");
            int ty = trycatch(false,0,0,0, false, null, 0);
            if (ty == 0){gameInfo();}
            else {return;}
        }
        if (path == 3)
        {
            emptyText();
            stringConcactinate("      Resources");

            stringConcactinate("* Stone and # Wheat ");
            stringConcactinate("Must be collected by  ");
            stringConcactinate("worker.  ");
            stringConcactinate("");
            stringConcactinate("Δ Trees (lumber)   ");
            stringConcactinate("▿ Rubies ° Pearls");
            stringConcactinate("✦ Sapphires ♦ Diamonds");
            stringConcactinate("Must be picked up by ");
            stringConcactinate("Your king and taken ");
            stringConcactinate("back to a settlement to ");
            stringConcactinate("be collected. ");

            stringConcactinate("0) Back");
            int ty = trycatch(false, 0,0,0, false, null, 0);
            if (ty == 0){gameInfo();}
            else {return;}
        }


    }

    public void mainMenu(){


        int answer = trycatch(true, 9,5, 0, false, null, 0);
        if (answer == 1){StartRoad();}
        if (answer == 2){ContinueRoad();}
        if (answer == 3){BuyMenu();}
        if (answer == 4){ManageUnits();}
        if (answer == 5){manageCities();}
        if (answer == 6){emptyText(); asked = false; UnitActions();buildingResources(); if (this.turn == -1){AddMaterial();} this.turn *= - 1; resetMoves(); ChangeTurn(); }
        if (answer == 7){gameInfo();}
        if (answer == 8){ViewData();}
        if (answer == 9){}
        //if (answer == 0){WrongInput();}
        //if (answer > 8){WrongInput();}

    }
    public void update(int answer)
    {
       ChangeTurn();

    }
    public int getTurn()
    {
        return  this.turn;

    }
    public void stringConcactinate(String text)
    {
        this.text.add(text);

    }
    public void borderConcactinate(String text)
    {
        this.textBorder.add(text);

    }
    public void emptyText()
    {
        if (this.text != null)
        {
            this.text.clear();
        }

    }
    public void emptyBorder()
    {
        if (this.textBorder != null)
        {
            this.textBorder.clear();
        }

    }

}
