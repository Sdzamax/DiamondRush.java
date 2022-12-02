public class PatternAcorn extends Pattern{

    public int getSizeX(){
        return 7;
    }
    public int getSizeY(){
        return 3;
    }
    public boolean getCell(int x, int y){
        if (x == 3 && y == 6){
            return true;
        }
        if (x == 4 && y == 6){
            return true;
        }
        if (x == 4 && y == 4){
            return true;
        }
        if (x == 6 && y == 5){
            return true;
        }
        if (x == 7 && y == 6){
            return true;
        }
        if (x == 8 && y == 6){
            return true;
        }
        if (x == 9 && y == 6){
            return true;
        }
        else {return false;}
    }
}