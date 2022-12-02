public class PatternBlock extends Pattern{

    public int getSizeX(){
        return 2;
    }
    public int getSizeY(){
        return 2;
    }
    public boolean getCell(int x, int y){
        if (x == 3 && y == 3){
            return true;
        }
        if (x == 3 && y == 4){
            return true;
        }
        if (x == 4 && y == 3){
            return true;
        }
        if (x == 4 && y == 4){
            return true;
        }
        else {return false;}
    }
}
