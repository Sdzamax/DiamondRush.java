// Reference for Lanterna 3: https://github.com/mabe02/lanterna/blob/master/docs/contents.md
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalTextUtils;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.MouseActionType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;
import java.util.ArrayList;

public class ConwaysLife {
    static boolean resized = false;
    final static TextColor wheatColor = new TextColor.Indexed(100);
    final static TextColor grassColor = new TextColor.Indexed(194);//106
    final static TextColor stoneColor = new TextColor.Indexed(240);
    final static TextColor diamondColor = new TextColor.Indexed(87);
    final static TextColor treeColor = new TextColor.Indexed(28);
    final static TextColor teamOne = new TextColor.Indexed(88);//88
    final static TextColor teamTwo = new TextColor.Indexed(117);//117
    final static TextColor purple = new TextColor.Indexed(57);
    final static TextColor yellow = new TextColor.Indexed(226);
    final static TextColor ruby = new TextColor.Indexed(89);
    final static TextColor pearl = new TextColor.Indexed(224);
    final static TextColor saphire = new TextColor.Indexed(21);
    final static TextColor DarkSquare = new TextColor.Indexed(0);
    final static TextColor LightSquare = new TextColor.Indexed(239);
    private int mapScroll1;
    private int mapScroll2;
    public String[] animationWall;


    public static void main(String[] args) {
        try {

            Font font = new Font("NanumGothic", Font.PLAIN, 15);
            AWTTerminalFontConfiguration chess = AWTTerminalFontConfiguration.newInstance(font);



            ConwaysLife conwaysLife = new ConwaysLife();
            DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
            Terminal terminal = null;
            terminal = defaultTerminalFactory.createTerminal();
            terminal.setBackgroundColor(TextColor.ANSI.WHITE);
            Screen screen = new TerminalScreen(terminal);

            TextGraphics graphics = screen.newTextGraphics();




            DiamondRush diamond = new DiamondRush(terminal, graphics, screen, conwaysLife);



            TerminalSize size = screen.getTerminalSize();
            boolean fullScreen = false;


            terminal.addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(Terminal terminal, TerminalSize newSize) {
                    // Be careful here though, this is likely running on a separate thread. Lanterna is threadsafe in
                    // a best-effort way so while it shouldn't blow up if you call terminal methods on multiple threads,
                    // it might have unexpected behavior if you don't do any external synchronization

                    resized = true;


                    try {

                        terminal.flush();

                    }
                    catch(IOException e) {
                        // Not much we can do here
                        throw new RuntimeException(e);
                    }
                }
            });

            LifeSimulator simulation = new LifeSimulator(size.getColumns() * 2, size.getRows());
            simulation.insertPattern(new PatternGlider(), 20, 10);
            simulation.insertPattern(new PatternAcorn(), 10, 0);
            simulation.insertPattern(new PatternBlock(), 60, 5);
            simulation.insertPattern(new PatternBlinker(), 40, 0);
            terminal.setCursorPosition(105,35);







            screen.startScreen();

            terminal.enterPrivateMode();
            int cap = 20;

            for (int i = 0; i < 100000; i++) {


                if (i == 0)
                {
                    animation(graphics,cap, screen,true);
                }
                else {animation(graphics,cap, screen,false);}

                cap --;
                if (cap < 1)
                {
                    cap = 20;
                }
                if (resized == true)
                {
                    System.out.println(resized);
                    i = 100000;
                }

                //terminal.flush();// Sleep for a bit to make for a nicer paced animation
            }


            for (int i = 0; i < 100000; i++) {

                int Map[][] = diamond.getMap();


                render2(Map,screen, graphics, 1,1,0);
                ArrayList<String> setText = diamond.getText();
                //renderText(screen,graphics, setText);
                //String answer = getInput(terminal,graphics, screen);
                //System.out.println(answer);
                diamond.update(0);
                Thread.yield();


                //terminal.flush();// Sleep for a bit to make for a nicer paced animation

            }
            terminal.exitPrivateMode();


            for (int i = 0; i < 100; i++) {
                checkerBoard(graphics);
                render(simulation,screen, graphics);
                Thread.yield();                         // Let the JVM have some time to update other things
                Thread.sleep(300);                // Sleep for a bit to make for a nicer paced animation
                simulation.update();                    // Tell the simulation to update
            }

            //screen.stopScreen();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }
    public static void render(LifeSimulator simulation, Screen screen, TextGraphics graphics)
    {
        for(int i = 0; i < simulation.getSizeX(); i++){
            for(int z = 0; z < simulation.getSizeY(); z++){
                if (simulation.getCell(i,z)){
                    graphics.setCharacter(i, z, '☼');
                }
                else {graphics.setCharacter(i, z, ' '); }
            }
        }

        try {
            screen.refresh();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }
    public String getInput(Terminal terminal, TextGraphics graphics, Screen screen, int[][] map, ArrayList<String> text, ArrayList<String> border, int turn, int offset, int list, int skip,
                           boolean mainMenu,boolean jump, int[][] jumpLocations, int type)  {
        try {


            int stringLength = 0;
            int arrow = 0;
            int mapScrollX = 0;
            int skipLength = skip + 1;
            if (turn == 1) {
                mapScrollX = this.mapScroll1;
            } else {
                mapScrollX = this.mapScroll2;
            }
            char[] dataString = new char[4];
            String answer = "";

            KeyStroke keyStroke = terminal.readInput();

            if (keyStroke.getKeyType() == KeyType.F1 || keyStroke.getKeyType() == KeyType.ArrowLeft || keyStroke.getKeyType() == KeyType.ArrowRight
                    || keyStroke.getKeyType() == KeyType.ArrowDown || keyStroke.getKeyType() == KeyType.ArrowUp) {
                getInput(terminal, graphics, screen, map, text, border, turn, offset, list, skip, mainMenu, jump, jumpLocations, type);
            }
            char answerChar = keyStroke.getCharacter();
            if (Character.isDigit(answerChar)) {
                dataString[stringLength] = answerChar;
                stringLength++;
                answer = new String(dataString);

            }
            if (list != 0) {
                if (answerChar == 's' && arrow < list || keyStroke.getKeyType() == KeyType.ArrowDown && arrow < list) {

                    arrow++;
                    displayArrow((arrow * skipLength) + offset, graphics);
                    System.out.println("arrow" + arrow + offset);

                }
            }
            if (jump && arrow > 0 ) { //&& arrow != list
                int yJump = (jumpLocations[0][1] * -1) + 15;
                int fluidType = type;
                if (type == 99) {
                    fluidType = arrow + 1;
                }
                else {yJump = (jumpLocations[arrow - 1][1] * -1) + 15;}
                if (yJump > 0) {
                    yJump = -1;
                }
                render2(map, screen, graphics, -1, yJump, 141 + yJump);
                if (arrow != list) {
                    if (type == 99) {
                        displaySymbol(jumpLocations[0][0], jumpLocations[0][1] + yJump, fluidType, graphics, screen);
                    } else {
                        displaySymbol(jumpLocations[arrow - 1][0], jumpLocations[arrow - 1][1] + yJump, fluidType, graphics, screen);
                    }
                }
            }
            while (keyStroke.getKeyType() != KeyType.Enter) {
                try {

                    if (stringLength == 1) {
                        graphics.setCharacter(61, 17, dataString[0]);
                    }
                    if (stringLength == 2) {
                        graphics.setCharacter(60, 17, dataString[0]);
                        graphics.setCharacter(61, 17, dataString[1]);
                    }
                    if (stringLength == 3) {
                        graphics.setCharacter(60, 17, dataString[0]);
                        graphics.setCharacter(61, 17, dataString[1]);
                        graphics.setCharacter(62, 17, dataString[2]);
                    }

                    terminal.flush();
                    screen.refresh();
                    screen.doResizeIfNecessary();
                    keyStroke = terminal.readInput();
                    //System.out.println(keyStroke);
                    char ringo = keyStroke.getCharacter();
                    if (Character.isDigit(ringo) && stringLength < 3) {
                        dataString[stringLength] = ringo;
                        stringLength++;

                    }
                    if (keyStroke.getKeyType() == KeyType.Backspace && stringLength > 0) {
                        dataString[stringLength] = ' ';
                        stringLength--;
                    }
                    if (list != 0) {
                        if (ringo == 's' && arrow < list || keyStroke.getKeyType() == KeyType.ArrowDown && arrow < list) {
                            arrow++;
                            renderText(screen, graphics, text, border, true);
                            displayArrow((arrow * skipLength) + offset, graphics);
                            System.out.println("arrow" + (arrow * skipLength) + offset);

                        }
                        if (ringo == 'w' && arrow > 1 || keyStroke.getKeyType() == KeyType.ArrowUp && arrow > 1) {
                            arrow--;
                            renderText(screen, graphics, text, border, true);
                            displayArrow((arrow * skipLength) + offset, graphics);
                            System.out.println("arrow" + arrow + offset);


                        }
                    }
                    if (jump && arrow > 0) { //arrow != list
                        int yJump = (jumpLocations[0][1] * -1) + 15;
                        int fluidType = type;
                        if (type == 99) {
                            fluidType = arrow + 1;
                        }
                        else {yJump = (jumpLocations[arrow - 1][1] * -1) + 15;}
                        if (yJump > 0) {
                            yJump = -1;
                        }
                        render2(map, screen, graphics, -1, yJump, 141 + yJump);
                        if (arrow != list) {
                            if (type == 99) {
                                displaySymbol(jumpLocations[0][0], jumpLocations[0][1] + yJump, fluidType, graphics, screen);
                            } else {
                                displaySymbol(jumpLocations[arrow - 1][0], jumpLocations[arrow - 1][1] + yJump, fluidType, graphics, screen);
                            }
                        }

                    }
                    if (ringo == 'd' && mapScrollX > -140) {

                        mapScrollX -= 2;
                        render2(map, screen, graphics, -1, mapScrollX, 0);
                        screen.refresh();
                        if (turn == 1) {
                            setScroll1(mapScrollX);
                        } else {
                            setScroll2(mapScrollX);
                        }

                        //screen.doResizeIfNecessary();
                    }
                    if (ringo == 'a' && mapScrollX < 0) {
                        mapScrollX += 2;
                        render2(map, screen, graphics, -1, mapScrollX, 0);
                        screen.refresh();
                        if (turn == 1) {
                            setScroll1(mapScrollX);
                        } else {
                            setScroll2(mapScrollX);
                        }

                    }
                    if (ringo == 'q') {
                        renderText(screen, graphics, text, border, false);
                        screen.refresh();
                        //screen.doResizeIfNecessary();
                    }
                    if (ringo != 'a' && ringo != 'd') {
                        if (stringLength == 0) {
                            graphics.putString(60, 17, " ");
                            graphics.putString(61, 17, " ");
                            graphics.putString(62, 17, " ");
                        }
                        if (stringLength == 1) {
                            graphics.putString(60, 17, " ");
                            graphics.putString(61, 17, " ");
                        }
                        if (stringLength == 2) {
                            graphics.putString(62, 17, " ");
                        }

                    }


                } catch (Exception ex) {
                    System.out.println("Something bad happened: " + ex.getMessage());
                }

            }
            if (stringLength == 0) {
                if (!mainMenu && arrow == list) {
                    arrow = 0;
                }
                return "" + arrow;
            }
            if (stringLength == 1) {
                answer = "" + dataString[0];
            }
            if (stringLength == 2) {
                answer = "" + dataString[0] + dataString[1];
            }
            if (stringLength == 3) {
                answer = "" + dataString[0] + dataString[1] + dataString[2];
            }
            keyStroke = null;
            graphics.putString(60, 17, " ");
            graphics.putString(61, 17, " ");
            graphics.putString(62, 17, " ");
            return answer;
        }
        catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage() + "Line" + ex.getStackTrace());
        }
        return "";
    }
    public void displaySymbol(int x, int y, int code, TextGraphics graphics, Screen screen)
    {
        System.out.println("DisplaySymbol: " + code);
        if (code == 1){ graphics.setCharacter(y, x - 1, '?'); }
        if (code == 2){ graphics.setCharacter(y, x - 1, '☭'); }
        if (code == 3){ graphics.setCharacter(y, x - 1, '⛏'); }
        if (code == 4){ graphics.setCharacter(y, x - 1, 'S'); }
    }


    public static void render2(int[][] map, Screen screen, TextGraphics graphics, int offsetX, int offsetY, int cuttoff){
        int checker = 1;
        int squares = 1;
        for(int i = 0; i < 200 - cuttoff; i++){
        for(int x = 0; x < 35; x++){
           {


                if (map[x][i] == 0){graphics.setCharacter(i + offsetY, x + offsetX , ' ');}//Prints empty area
                if (map[x][i] == 1){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'A');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 2){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX , 'B');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 3){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'C');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                }
                if (map[x][i] == 4){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Å');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 5){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'ß');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 6){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Ç');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 7){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Ä');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 8){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'฿');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 9){
                    graphics.setForegroundColor(teamOne);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'¢');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 10){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'X');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 11){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Y');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 12){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Z');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 13){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'⌘');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 14){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'¥');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 15){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'ζ');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 16){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'✥');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 17){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'ϒ');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE); }
                if (map[x][i] == 18){
                    graphics.setForegroundColor(teamTwo);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'ϟ');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}
                if (map[x][i] == 19){
                    graphics.setForegroundColor(stoneColor);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'*');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}//Prints stone
                if (map[x][i] == 20){
                    graphics.setForegroundColor(diamondColor);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'♦');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                }//prints Diamiond
                if (map[x][i] == 21){
                    graphics.setForegroundColor(wheatColor);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'#');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}//Prints wheat
                if (map[x][i] == 22){
                    graphics.setForegroundColor(grassColor);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'~');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}//prints grass
                if (map[x][i] == 23){
                    graphics.setForegroundColor(treeColor);
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,'Δ');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}//prints tree
                if (map[x][i] == 24){graphics.setCharacter(i+ offsetY, x+ offsetX ,'[');}//prints vertical line
                if (map[x][i] == 25){
                    char tile = ' ';
                    if (i > 198 ){
                        graphics.setBackgroundColor(TextColor.ANSI.WHITE);
                        graphics.setForegroundColor(TextColor.ANSI.BLACK);
                        if (checker%2 == 1){
                            tile = '░';
                        }
                        if (checker%2 == 0){
                            tile = '█';
                        }
                        if (checker > 9 && checker%2 == 1){
                            tile = '▣';
                        }
                        if (checker > 9 && checker%2 == 0){
                            tile = '◍';
                        }
                    }
                    else {

                        if (checker%2 == 1){
                            tile = '█';
                        }
                        if (checker%2 == 0){
                            tile = '▒';
                        }
                        if (checker > 9 && checker%2 == 1){
                            tile = '▩';
                        }
                        if (checker > 9 && checker%2 == 0){
                            tile = '◈';
                        }
                    }
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);
                    graphics.setBackgroundColor(TextColor.ANSI.BLACK);
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);

                }//Prints vertical wall
                if (map[x][i] == 26){ char tile = ' ';
                    if (checker%2 == 1){
                        tile = '█';
                    }
                    if (checker%2 == 0){
                        tile = '▒';
                    }
                    if (checker > 9 && checker%2 == 1){
                        tile = '▩';
                    }
                    if (checker > 9 && checker%2 == 0){
                        tile = '◈';
                    }

                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);}//prints horizantal wall
                if (map[x][i] == 27){graphics.setCharacter(i+ offsetY, x+ offsetX ,']');}//prints horizantal line
                if (map[x][i] == 28){graphics.setCharacter(i+ offsetY, x+ offsetX ,'|');}//prints horizantal line
                if (map[x][i] == 29){graphics.setCharacter(i+ offsetY, x+ offsetX ,'·');}//prints soilder
                if (map[x][i] == 33){graphics.setCharacter(i+ offsetY, x+ offsetX ,'•');}//prints playerTwoRoad
                if (map[x][i] == 34){graphics.setCharacter(i+ offsetY, x+ offsetX ,'≡');}//prints playerOneRoad
                if (map[x][i] == 35){graphics.setCharacter(i+ offsetY, x+ offsetX ,'♖');}//prints Tower
                if (map[x][i] == 36){graphics.setCharacter(i+ offsetY, x+ offsetX ,'░');}//prints Reaped land
                if (map[x][i] == 37){ char tile = ' ';
                    if (checker%2 == 1){
                        tile = '█';
                    }
                    if (checker%2 == 0){
                        tile = '▒';
                    }
                    if (checker > 9 && checker%2 == 1){
                        tile = '▩';
                    }
                    if (checker > 9 && checker%2 == 0){
                        tile = '◈';
                    }
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);}//prints corner
                if (map[x][i] == 38){ char tile = ' ';
                    if (checker%2 == 1){
                        tile = '█';
                    }
                    if (checker%2 == 0){
                        tile = '▒';
                    }
                    if (checker > 9 && checker%2 == 1){
                        tile = '▩';
                    }
                    if (checker > 9 && checker%2 == 0){
                        tile = '◈';
                    }
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);}//prints corner
                if (map[x][i] == 39){ char tile = ' ';
                    if (checker%2 == 1){
                        tile = '█';
                    }
                    if (checker%2 == 0){
                        tile = '▒';
                    }
                    if (checker > 9 && checker%2 == 1){
                        tile = '▩';
                    }
                    if (checker > 9 && checker%2 == 0){
                        tile = '◈';
                    }
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);}//prints corner
                if (map[x][i] == 40){ char tile = ' ';
                    if (checker%2 == 1){
                        tile = '█';
                    }
                    if (checker%2 == 0){
                        tile = '▒';
                    }
                    if (checker > 9 && checker%2 == 1){
                        tile = '▩';
                    }
                    if (checker > 9 && checker%2 == 0){
                        tile = '◈';
                    }
                    graphics.setCharacter(i+ offsetY, x+ offsetX ,tile);}//prints corner
                if (map[x][i] == 41){graphics.setCharacter(i+ offsetY, x+ offsetX ,'▌');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);}//prints Reaped land
               if (map[x][i] == 42){
                   graphics.setForegroundColor(ruby);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'▿');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);}//prints Reaped land
               if (map[x][i] == 43){
                   graphics.setForegroundColor(pearl);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'°');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);}//prints Reaped land
               if (map[x][i] == 44){
                   graphics.setForegroundColor(saphire);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'✦');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
                   }//prints Reaped land
               if (map[x][i] == 45){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♚');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 46){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♔');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 47){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♟');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 48){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♙');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 49){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♞');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 50){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♘');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 51){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♝');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 52){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♗');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 53){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♜');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);

               }
               if (map[x][i] == 54){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♖');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 55){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♛');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }
               if (map[x][i] == 56){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'♕');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);

               }
               if (map[x][i] == 57){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'☭');//prints worker
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);}
               if (map[x][i] == 58){
                   graphics.setForegroundColor(teamOne);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'⛏');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }//prints soilder
               if (map[x][i] == 30){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'☭');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }//prints worker
               if (map[x][i] == 32){
                   graphics.setForegroundColor(teamTwo);
                   graphics.setCharacter(i+ offsetY, x+ offsetX ,'⛏');
                   graphics.setForegroundColor(TextColor.ANSI.WHITE);
               }//prints soilder
               checker ++;
                if (checker > 12){
                    checker = 1;
                }
            }

            }
        }

        try {


            //screen.doResizeIfNecessary();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }
    public static void checkerBoard(TextGraphics graphics)
    {
        int marble = 1;
        int checker = -1;
        for(int i = 0; i < 300; i++) {
            for (int x = 0; x < 70; x++) {
                {
                    if (x > 32 && x < 58 && i > 4 && i < 124)
                    {
                        checker *= -1;
                        if (checker== -1 )
                        {
                            graphics.setCharacter(i,x,'░');
                        }
                        if (checker== 1 )
                        {
                            graphics.setCharacter(i,x,'█');
                        }
                    }
                    else {
                        if (marble == 1)
                        { graphics.setCharacter(i,x,'░'); }
                        if (marble == 2)
                        {
                            graphics.setCharacter(i,x,'▒');
                        }
                        if (marble == 3)
                        {
                            graphics.setCharacter(i,x,'▓');
                        }
                        if (marble == 4)
                        {
                            graphics.setCharacter(i,x,'█');
                        }
                        if (marble == 5)
                        {
                            graphics.setCharacter(i,x,' ');
                        }
                        if (marble == 6)
                        {

                            graphics.setCharacter(i,x,'°');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 7)
                        {

                            graphics.setCharacter(i,x,'¿');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 8)
                        {

                            graphics.setCharacter(i,x,'•');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 9)
                        {

                            graphics.setCharacter(i,x,'Σ');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 10)
                        {


                            graphics.setCharacter(i,x,'?');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 11)
                        {

                            graphics.setCharacter(i,x,'△');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 13)
                        {
                            graphics.setCharacter(i,x,'▼');
                        }
                        if (marble == 14)
                        {
                            graphics.setCharacter(i,x,'◬');
                        }
                        if (marble == 15)
                        {
                            graphics.setCharacter(i,x,'▩');
                        }
                        if (marble == 16)
                        {

                            graphics.setCharacter(i,x,'؋');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        if (marble == 17)
                        {
                            graphics.setCharacter(i,x,'₪');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        }
                        marble ++;
                        if (marble > 16)
                        {
                            marble = 1;
                        }
                    }

                }
            }
        }
    }
    public void displayArrow(int highlight, TextGraphics graphics)
    {
        graphics.setCharacter(59, highlight, '▷');
    }

    public static void renderText(Screen screen, TextGraphics graphics, ArrayList<String> text, ArrayList<String> textBorder, boolean justBorder) {


        //graphics.putString( 130, 35, "\u2654");




        for (int i = 0; i < textBorder.size(); i ++) {
            graphics.putString(59, i, textBorder.get(i));

        }

        graphics.putString( 65, 21, "╔═════════════╗");


        graphics.setCharacter(65, 22 ,'║');

        graphics.setCharacter(79, 22 ,'║');
        graphics.setForegroundColor(treeColor);
        graphics.setCharacter(69, 22 ,'Δ');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 22, "Tree");


        graphics.setCharacter(65, 23 ,'║');
        graphics.setCharacter(79, 23 ,'║');
        graphics.setForegroundColor(stoneColor);
        graphics.setCharacter(69, 23 ,'*');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 23, "Stone");


        graphics.setCharacter(65, 24 ,'║');
        graphics.setCharacter(79, 24 ,'║');
        graphics.setForegroundColor(wheatColor);
        graphics.setCharacter(69, 24 ,'#');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 24, "Wheat");


        graphics.setCharacter(65, 25 ,'║');
        graphics.setCharacter(79, 25 ,'║');
        graphics.setForegroundColor(ruby);
        graphics.setCharacter(69, 25 ,'▿');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 25, "Ruby");

        graphics.setCharacter(65, 26 ,'║');
        graphics.setCharacter(79, 26 ,'║');
        graphics.setForegroundColor(pearl);
        graphics.setCharacter(69, 26 ,'°');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 26, "Pearl");

        graphics.setCharacter(65, 27 ,'║');
        graphics.setCharacter(79, 27 ,'║');
        graphics.setForegroundColor(saphire);
        graphics.setCharacter(69, 27 ,'✦');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 27, "Saphire");



        graphics.setCharacter(65, 28 ,'║');
        graphics.setCharacter(79, 28 ,'║');
        graphics.setForegroundColor(diamondColor);
        graphics.setCharacter(69, 28 ,'♦');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.putString( 70, 28, "Diamond");

        graphics.putString( 65, 29, "╚═════════════╝");



        for (int i = 0; i < text.size(); i ++) {
            boolean empty = false;
            graphics.putString(60,1+ i, text.get(i));
            if (text.get(i) == "")
            {
                empty = true;
            }
            if (!empty)
            {
                try {

                    if (!justBorder){
                        screen.refresh();
                        Thread.sleep(40);
                        Thread.yield();
                    }

                } catch (Exception ex) {
                    System.out.println("Something bad happened: " + ex.getMessage());
                }

            }
        }

        try {
            screen.refresh();
            screen.doResizeIfNecessary();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }

    }
    public static void animation(TextGraphics graphics,int cap, Screen screen, boolean first)
    {
        if (first){
        for(int i = 0; i < 80; i++) {
            for (int x = 0; x < 24; x++) {
                graphics.setCharacter(25, 7, '╔');
                graphics.setCharacter(54, 7, '╗');
                graphics.setCharacter(25, 17, '╚');
                graphics.setCharacter(54, 17, '╝');
                for (int z = 0; z < 28; z++) {
                    graphics.setCharacter(26 + z, 7, '═');
                }
                for (int z = 0; z < 28; z++) {
                    graphics.setCharacter(26 + z, 17, '═');
                }

                for (int z = 0; z < 9; z++) {
                    graphics.setCharacter(54, 8 + z, '║');
                }
                for (int z = 0; z < 9; z++) {
                    graphics.setCharacter(25, 8 + z, '║');
                }
                graphics.putString(35, 9, "Welcome to ");
                graphics.putString(35, 11, "Diamond Rush");
                graphics.putString(29, 13, "Enter FullScreen Mode");
                graphics.putString(37, 15, "To Play ");


            }
            }
        }
        int marble = 1;
        for(int i = 0; i < 80; i++) {
            for (int x = 0; x < 24; x++) {
                {
                    try {
                        Thread.sleep(1);
                        screen.refresh();
                        screen.doResizeIfNecessary();
                    } catch (Exception ex) {
                        System.out.println("Something bad happened: " + ex.getMessage());
                    }
                    if (x > 6 && x < 18 && i > 24 && i < 55)
                    {
                    }
                    else {
                        if (marble == 1)
                        { graphics.setCharacter(i,x,'░'); }
                        if (marble == 2)
                        { graphics.setCharacter(i,x,'▒'); }
                        if (marble == 3)
                        { graphics.setCharacter(i,x,'▓'); }
                        if (marble == 4)
                        { graphics.setCharacter(i,x,'█'); }
                        if (marble == 5)
                        { graphics.setCharacter(i,x,' '); }
                        if (marble == 6)
                        { graphics.setCharacter(i,x,'°'); }
                        if (marble == 7)
                        { graphics.setCharacter(i,x,'¿'); }
                        if (marble == 8)
                        { graphics.setCharacter(i,x,'•'); }
                        if (marble == 9)
                        { graphics.setCharacter(i,x,'Σ'); }
                        if (marble == 10)
                        { graphics.setCharacter(i,x,'?'); }
                        if (marble == 11)
                        { graphics.setCharacter(i,x,'△'); }
                        if (marble == 13)
                        { graphics.setCharacter(i,x,'▼'); }
                        if (marble == 14)
                        { graphics.setCharacter(i,x,'◬'); }
                        if (marble == 15)
                        { graphics.setCharacter(i,x,'▩'); }
                        if (marble == 16)
                        { graphics.setCharacter(i,x,'؋'); }
                        if (marble == 17)
                        { graphics.setCharacter(i,x,'₪'); }
                        if (marble == 18)
                        { graphics.setCharacter(i,x,'♣'); }
                        if (marble == 19)
                        { graphics.setForegroundColor(diamondColor);
                            graphics.setCharacter(i, x ,'♦');
                            graphics.setForegroundColor(TextColor.ANSI.WHITE); }

                    }

                    marble --;
                    if (marble < 2)
                    { marble = cap; }
                }
            }
        }

    }
    public static void emptyText(TextGraphics graphics)
    {
        for (int x = 0; x < 101; x ++) {
            for (int y = 0; y < 17; y++)
            {
                graphics.putString(61 + x, 1 + y, "Y");
            }
        }
    }
    public static int movePiece(Terminal terminal, TextGraphics graphics, Screen screen) throws IOException {

        KeyStroke keyStroke = terminal.readInput();


        while (keyStroke.getKeyType() != KeyType.Enter)
        {

            if (keyStroke.getKeyType() == KeyType.ArrowDown)
            {
                return 3;
            }
            if (keyStroke.getKeyType() == KeyType.ArrowUp)
            {
                return 1;
            }
            if (keyStroke.getKeyType() == KeyType.ArrowLeft)
            {
                return 4;
            }
            if (keyStroke.getKeyType() == KeyType.ArrowRight)
            {
                return 2;
            }
            if (keyStroke.getKeyType() == KeyType.F1)
            {
                return 5;
            }
            keyStroke = terminal.readInput();
        }

        return 0;

    }
    public static void customInput(TextGraphics graphics, Screen screen, int code)
    {
        int sleep = 1500;
        if (code == 0) { graphics.putString(65, 17, "Invalid Input");}
        if (code == 1) { graphics.putString(65, 17, "+1 Worker"); }
        if (code == 2) { graphics.putString(65, 17, "+1 RoadBuster"); }
        if (code == 3) { graphics.putString(65, 17, "+1 Settlement"); }
        if (code == 4) { graphics.putString(62, 17, "Need more resources "); }
        if (code == 5) { graphics.putString(60, 17, "can only buy 1 settlement"); }
        if (code == 6) { graphics.putString(65, 17, "+1 Pawn"); }
        if (code == 7) { graphics.putString(65, 17, "+1 Knight"); }
        if (code == 8) { graphics.putString(65, 17, "+1 Bishop"); }
        if (code == 9) { graphics.putString(65, 17, "+1 Rook"); }
        if (code == 10) { graphics.putString(65, 17, "+1 Queen"); }
        if (code == 11) { graphics.putString(65, 17, "+3 Lumber"); sleep = 700;}
        if (code == 12) { graphics.putString(65, 17, "+1 Ruby"); sleep = 700;}
        if (code == 13) { graphics.putString(65, 17, "+1 Pearl"); sleep = 700;}
        if (code == 14) { graphics.putString(65, 17, "+1 Sapphire");sleep = 700; }
        if (code == 15) { graphics.putString(65, 17, " No Moves");}

        try {
            screen.refresh();
            Thread.sleep(sleep);
            Thread.yield();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }
    private void setScroll1(int scroll)
    {
        this.mapScroll1 = scroll;
    }
    private void setScroll2(int scroll)
    {
        this.mapScroll2 = scroll;
    }
    public int getMapScroll1()
    {
        return this.mapScroll1;
    }
    public int getMapScroll2()
    {
        return this.mapScroll2;
    }
    public void visualizeMovement(int x, int y, int piece, int[][] map, TextGraphics graphics, Screen screen, int turn)
    {
        int offset = 0;
        int road = 0;
        if (turn == 1){road = 34;}
        else {road = 33;}
        if (turn == 1){offset = this.mapScroll1;}
        else {offset = this.mapScroll2;}

        System.out.println("piece" + piece);
        if (piece == 2)
        {

            int newX = x -1;
            int newY = y + offset;

            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            if (map[x + 2][y + 1] == 0  || map[x + 2][y + 1] == 22 || map[x + 2][y + 1] == road)
            {
                graphics.setCharacter(newY + 1 ,newX + 2,'◦');
            }
            if (map[x + 2][y - 1] == 0 || map[x + 2][y - 1] == 22 || map[x + 2][y - 1] == road)
            {
                graphics.setCharacter(newY - 1 ,newX + 2,'◦');
            }
            if (map[x - 2][y + 1] == 0 || map[x - 2][y + 1] == 22 || map[x - 2][y + 1] == road)
            {
                graphics.setCharacter(newY + 1 ,newX - 2,'◦');
            }
            if (map[x - 2][y - 1] == 0 || map[x - 2][y - 1] == 22 || map[x - 2][y - 1] == road)
            {
                graphics.setCharacter(newY - 1 ,newX - 2,'◦');
            }

            if (map[x + 1][y + 2] == 0 || map[x + 1][y + 2] == 22 ||  map[x + 1][y + 2] == road)
            {
                graphics.setCharacter(newY + 2 ,newX + 1,'◦');
            }
            if (map[x + 1][y - 2] == 0 || map[x + 1][y - 2] == 22 || map[x + 1][y - 2] == road)
            {
                graphics.setCharacter(newY - 2 ,newX + 1,'◦');
            }
            if (map[x - 1][y + 2] == 0 || map[x - 1][y + 2] == 22 || map[x - 1][y + 2] == road)
            {
                graphics.setCharacter(newY + 2 ,newX - 1,'◦');
            }
            if (map[x - 1][y - 2] == 0 || map[x - 1][y - 2] == 22 || map[x - 1][y - 2] == road)
            {
                graphics.setCharacter(newY - 2 ,newX - 1,'◦');
            }
            try {
                screen.refresh();
                Thread.sleep(1500);

            } catch (Exception ex) {
                System.out.println("Something bad happened: " + ex.getMessage());
            }

        }
        if (piece == 3)
        {
            int increase = 1;
            int newY = y + offset;
            int newX = x - 1;
            boolean contLeft = true;
            boolean contRight = true;
            boolean contUp = true;
            boolean contDown = true;
            for (int i = 0; i < 150; i++){
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                if (contLeft && map[x + increase][y + increase] == 0 || contLeft && map[x + increase][y + increase] == 22 || contLeft && map[x + increase][y + increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX + increase,'◦');
                }
                else {contLeft = false;
                }
                if (contRight && map[x + increase][y - increase] == 0 || contRight && map[x + increase][y - increase] == 22 || contRight && map[x + increase][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX + increase,'◦');
                }
                else {contRight = false;
                }
                if (contUp && map[x - increase][y - increase] == 0 || contUp && map[x - increase][y - increase] == 22 || contUp && map[x - increase][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX - increase,'◦');
                }
                else {contUp = false;
                }
                if (contDown && map[x - increase][y + increase] == 0 || contDown && map[x - increase][y + increase] == 22|| contDown && map[x - increase][y + increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX - increase,'◦');
                }
                else {contDown = false;
                }
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                increase ++;
                if (!contLeft && !contDown && !contUp && !contRight){i = 150;}
                try {
                    screen.refresh();
                    Thread.sleep(50);
                    Thread.yield();
                } catch (Exception ex) {
                    System.out.println("Something bad happened: " + ex.getMessage());
                }
            }
            try {
                screen.refresh();
                Thread.sleep(1000);
                Thread.yield();
            } catch (Exception ex) {
                System.out.println("Something bad happened: " + ex.getMessage());
            }

        }
        if (piece == 4)
        {
            int increase = 1;
            int newX = x -1;
            int newY = y + offset;
            boolean contLeft = true;
            boolean contRight = true;
            boolean contUp = true;
            boolean contDown = true;
            for (int i = 0; i < 150; i++){
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                if (contLeft && map[x][y + increase] == 0 || contLeft && map[x][y+ increase] == 22 || contLeft && map[x][y+ increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX,'◦');
                }
                else {contLeft = false;
                }
                if (contRight && map[x + increase][y] == 0 || contRight && map[x + increase][y] == 22 || contRight && map[x + increase][y] == road )
                {
                    graphics.setCharacter(newY,newX + increase,'◦');
                }
                else {contRight = false;
                }
                if (contUp && map[x - increase][y] == 0 || contUp && map[x - increase][y] == 22 || contUp && map[x - increase][y] == road)
                {
                    graphics.setCharacter(newY,newX - increase,'◦');
                }
                else {contUp = false;
                }
                if (contDown && map[x][y - increase] == 0 || contDown && map[x][y - increase] == 22 || contDown && map[x][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX,'◦');
                }
                else {contDown = false;
                }
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                increase ++;
                if (!contLeft && !contDown && !contUp && !contRight){i = 150;}
                try {
                    screen.refresh();
                    Thread.sleep(25);
                    Thread.yield();
                } catch (Exception ex) {
                    System.out.println("Something bad happened: " + ex.getMessage());
                }
            }
            try {
                screen.refresh();
                Thread.sleep(1000);
                Thread.yield();
            } catch (Exception ex) {
                System.out.println("Something bad happened: " + ex.getMessage());
            }

        }
        if (piece == 5)
        {
            int increase = 1;
            int newX = x -1;
            int newY = y + offset;
            boolean contLeft = true;
            boolean contRight = true;
            boolean contUp = true;
            boolean contDown = true;
            boolean upRight = true;
            boolean upLeft = true;
            boolean downRight = true;
            boolean downLeft = true;
            for (int i = 0; i < 150; i++){
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                if (contLeft && map[x][y + increase] == 0 || contLeft && map[x][y+ increase] == 22 || contLeft && map[x][y+ increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX,'◦');
                }
                else {contLeft = false;
                }
                if (contRight && map[x + increase][y] == 0 || contRight && map[x + increase][y] == 22 || contRight && map[x + increase][y] == road)
                {
                    graphics.setCharacter(newY,newX + increase,'◦');
                }
                else {contRight = false;
                }
                if (contUp && map[x - increase][y] == 0 || contUp && map[x - increase][y] == 22 || contUp && map[x - increase][y] == road)
                {
                    graphics.setCharacter(newY,newX - increase,'◦');
                }
                else {contUp = false;
                }
                if (contDown && map[x][y - increase] == 0 || contDown && map[x][y - increase] == 22 || contDown && map[x][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX,'◦');
                }
                else {contDown = false;
                }
                if (upLeft && map[x + increase][y + increase] == 0 || upLeft && map[x + increase][y + increase] == 22 || upLeft && map[x + increase][y + increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX + increase,'◦');
                }
                else {upLeft = false;
                }
                if (upRight && map[x + increase][y - increase] == 0 || upRight && map[x + increase][y - increase] == 22 || upRight && map[x + increase][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX + increase,'◦');
                }
                else {upRight = false;
                }
                if (downLeft && map[x - increase][y - increase] == 0 || downLeft && map[x - increase][y - increase] == 22 || downLeft && map[x - increase][y - increase] == road)
                {
                    graphics.setCharacter(newY - increase ,newX - increase,'◦');
                }
                else {downLeft = false;
                }
                if (downRight && map[x - increase][y + increase] == 0 || downRight && map[x - increase][y + increase] == 22 || downRight && map[x - increase][y + increase] == road)
                {
                    graphics.setCharacter(newY + increase ,newX - increase,'◦');
                }
                else {downRight = false;
                }
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                increase ++;
                if (!contLeft && !contDown && !contUp && !contRight && !downLeft && !downRight && !upLeft && !upRight){i = 150;}
                try {
                    screen.refresh();
                    Thread.sleep(25);
                    Thread.yield();
                } catch (Exception ex) {
                    System.out.println("Something bad happened: " + ex.getMessage());
                }
            }
            try {
                screen.refresh();
                Thread.sleep(1000);
                Thread.yield();
            } catch (Exception ex) {
                System.out.println("Something bad happened: " + ex.getMessage());
            }

        }

    }
    public static void winAnimationOne(TextGraphics graphics, Screen screen)
    {
            int cap = 15;
            int increase = 0;
            for (int w = 0; w < 1000; w ++) {
                cap --;

                for (int i = 0; i < 85; i++) {

                    for (int x = 0; x < 33; x++) {
                        if (x > 11 && x < 21 && i > 24 && i < 55) {
                            graphics.setCharacter(i, x, ' ');
                        }
                        graphics.setCharacter(25, 11, '╔');
                        graphics.setCharacter(54, 11, '╗');
                        graphics.setCharacter(25, 21, '╚');
                        graphics.setCharacter(54, 21, '╝');
                        for (int z = 0; z < 28; z++) {
                            graphics.setCharacter(26 + z, 11, '═');
                        }
                        for (int z = 0; z < 28; z++) {
                            graphics.setCharacter(26 + z, 21, '═');
                        }

                        for (int z = 0; z < 9; z++) {
                            graphics.setCharacter(54, 12 + z, '║');
                        }
                        for (int z = 0; z < 9; z++) {
                            graphics.setCharacter(25, 12 + z, '║');
                        }
                        graphics.putString(35, 14, "Player One");
                        graphics.putString(35, 16, "Has Won");
                        graphics.putString(35, 18, "the Game!");
                        graphics.setForegroundColor(diamondColor);
                        graphics.setCharacter(26, 12, '♦');
                        graphics.setCharacter(53, 12, '♦');
                        graphics.setCharacter(26, 20, '♦');
                        graphics.setCharacter(53, 20, '♦');
                        graphics.setForegroundColor(TextColor.ANSI.WHITE);

                    }
                }

                int marble = 1;
                for (int i = 0; i < 85; i++) {
                    for (int x = 0; x < 33; x++) {
                        {
                            try {
                                Thread.sleep(1);
                                screen.refresh();
                                screen.doResizeIfNecessary();
                            } catch (Exception ex) {
                                System.out.println("Something bad happened: " + ex.getMessage());
                            }
                            if (x > 10 && x < 22 && i > 24 && i < 55) {

                            } else {
                                if (marble == 1) {
                                    graphics.setCharacter(i, x, '░');
                                }
                                if (marble == 2) {
                                    graphics.setCharacter(i, x, '▒');
                                }
                                if (marble == 3) {
                                    graphics.setCharacter(i, x, '▓');
                                }
                                if (marble == 4) {
                                    graphics.setCharacter(i, x, '█');
                                }
                                if (marble == 5) {
                                    graphics.setCharacter(i, x, ' ');
                                }
                                if (marble == 6) {
                                    graphics.setCharacter(i, x, '°');
                                }
                                if (marble == 7) {
                                    graphics.setCharacter(i, x, '¿');
                                }
                                if (marble == 8) {
                                    graphics.setCharacter(i, x, '•');
                                }
                                if (marble == 9) {
                                    graphics.setCharacter(i, x, 'Σ');
                                }
                                if (marble == 10) {
                                    graphics.setCharacter(i, x, '?');
                                }
                                if (marble == 11) {
                                    graphics.setCharacter(i, x, '△');
                                }
                                if (marble == 13) {
                                    graphics.setCharacter(i, x, '▼');
                                }
                                if (marble == 14) {
                                    graphics.setCharacter(i, x, '◬');
                                }
                                if (marble == 15) {
                                    graphics.setCharacter(i, x, '▩');
                                }
                                if (marble == 16) {
                                    graphics.setCharacter(i, x, '؋');
                                }
                                if (marble == 17) {
                                    graphics.setCharacter(i, x, '₪');
                                }
                                if (marble == 18) {
                                    graphics.setCharacter(i, x, '♣');
                                }
                                if (marble == 19) {
                                    graphics.setForegroundColor(diamondColor);
                                    graphics.setCharacter(i, x, '♦');
                                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                                }

                            }
                            increase ++;
                            marble--;
                            if (marble < 2) {
                                marble = cap;
                            }
                            if (increase > 4)
                            {
                                increase = 0;
                            }
                        }
                    }
                }
            }
    }
    public static void winAnimationTwo(TextGraphics graphics, Screen screen)
    {
        int cap = 15;
        int increase = 0;
        for (int w = 0; w < 1000; w ++) {
            cap --;

            for (int i = 0; i < 85; i++) {

                for (int x = 0; x < 33; x++) {
                    if (x > 11 && x < 21 && i > 24 && i < 55) {
                        graphics.setCharacter(i, x, ' ');
                    }
                    graphics.setCharacter(25, 11, '╔');
                    graphics.setCharacter(54, 11, '╗');
                    graphics.setCharacter(25, 21, '╚');
                    graphics.setCharacter(54, 21, '╝');
                    for (int z = 0; z < 28; z++) {
                        graphics.setCharacter(26 + z, 11, '═');
                    }
                    for (int z = 0; z < 28; z++) {
                        graphics.setCharacter(26 + z, 21, '═');
                    }

                    for (int z = 0; z < 9; z++) {
                        graphics.setCharacter(54, 12 + z, '║');
                    }
                    for (int z = 0; z < 9; z++) {
                        graphics.setCharacter(25, 12 + z, '║');
                    }
                    graphics.putString(35, 14, "Player Two");
                    graphics.putString(35, 16, "Has Won");
                    graphics.putString(35, 18, "the Game!");
                    graphics.setForegroundColor(diamondColor);
                    graphics.setCharacter(26, 12, '♦');
                    graphics.setCharacter(53, 12, '♦');
                    graphics.setCharacter(26, 20, '♦');
                    graphics.setCharacter(53, 20, '♦');
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);

                }
            }

            int marble = 1;
            for (int i = 0; i < 85; i++) {
                for (int x = 0; x < 33; x++) {
                    {
                        try {
                            Thread.sleep(1);
                            screen.refresh();
                            screen.doResizeIfNecessary();
                        } catch (Exception ex) {
                            System.out.println("Something bad happened: " + ex.getMessage());
                        }
                        if (x > 10 && x < 22 && i > 24 && i < 55) {

                        } else {
                            if (marble == 1) {
                                graphics.setCharacter(i, x, '░');
                            }
                            if (marble == 2) {
                                graphics.setCharacter(i, x, '▒');
                            }
                            if (marble == 3) {
                                graphics.setCharacter(i, x, '▓');
                            }
                            if (marble == 4) {
                                graphics.setCharacter(i, x, '█');
                            }
                            if (marble == 5) {
                                graphics.setCharacter(i, x, ' ');
                            }
                            if (marble == 6) {
                                graphics.setCharacter(i, x, '°');
                            }
                            if (marble == 7) {
                                graphics.setCharacter(i, x, '¿');
                            }
                            if (marble == 8) {
                                graphics.setCharacter(i, x, '•');
                            }
                            if (marble == 9) {
                                graphics.setCharacter(i, x, 'Σ');
                            }
                            if (marble == 10) {
                                graphics.setCharacter(i, x, '?');
                            }
                            if (marble == 11) {
                                graphics.setCharacter(i, x, '△');
                            }
                            if (marble == 13) {
                                graphics.setCharacter(i, x, '▼');
                            }
                            if (marble == 14) {
                                graphics.setCharacter(i, x, '◬');
                            }
                            if (marble == 15) {
                                graphics.setCharacter(i, x, '▩');
                            }
                            if (marble == 16) {
                                graphics.setCharacter(i, x, '؋');
                            }
                            if (marble == 17) {
                                graphics.setCharacter(i, x, '₪');
                            }
                            if (marble == 18) {
                                graphics.setCharacter(i, x, '♣');
                            }
                            if (marble == 19) {
                                graphics.setForegroundColor(diamondColor);
                                graphics.setCharacter(i, x, '♦');
                                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                            }

                        }
                        increase ++;
                        marble--;
                        if (marble < 2) {
                            marble = cap;
                        }
                        if (increase > 4)
                        {
                            increase = 0;
                        }
                    }
                }
            }
        }
    }
}

//System.out.println("Something bad happened: " + ex.getMessage())