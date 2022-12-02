public class PatternBlinker extends Pattern{

    public int getSizeX(){
        return 1;
    }
    public int getSizeY(){
        return 3;
    }
    public boolean getCell(int x, int y){
        if (x == 3 && y == 3){
            return true;
        }
        if (x == 3 && y == 4){
            return true;
        }
        if (x == 3 && y == 5){
            return true;
        }
        else {return false;}
    }
}
