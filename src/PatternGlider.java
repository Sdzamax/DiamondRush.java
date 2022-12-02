public class PatternGlider extends Pattern{

    public int getSizeX(){
        return 3;
    }
    public int getSizeY(){
        return 3;
    }
    public boolean getCell(int x, int y){
        if (x == 5 && y == 5){
            return true;
        }
        if (x == 5 && y == 6){
            return true;
        }
        if (x == 5 && y == 7){
            return true;
        }
        if (x == 4 && y == 7){
            return true;
        }
        if (x == 3 && y == 6){
            return true;
        }
        else {return false;}
    }
}
