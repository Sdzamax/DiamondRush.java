
public class LifeSimulator {
    private int x;
    private int y;
    private int grid[][];
    private int updateGrid[][];

    public LifeSimulator(int sizeX, int sizeY)
    {
        this.x = sizeX;
        this.y = sizeY;
        this.grid = new int[x][y];
        this.updateGrid = new int[x][y];




    }


    public int getSizeX() { return x;}
    public int getSizeY() { return y;}
    public boolean getCell(int x , int y)
    {
        if (grid[x][y] == 1){
            return true;
        }
        else return false;
    }

    public void insertPattern(Pattern pattern, int startX, int startY)
    {
        for(int i = 0; i < x-1; i++) {
            for (int z = 0; z < y - 1; z++) {
                if (pattern.getCell(i,z)){

                    grid[i + startX][z + startY] = 1;

                }
            }
        }
    }
    public void update()
    {

        for(int i = 0; i < x-1; i++){
            for(int z = 0; z < y-1; z++){
                int aliveCells = 0;
                if (i > 0 && i < x){
                    if (z > 0 && z < y){
                       if (grid[i-1][z-1] == 1){aliveCells++;}
                       if (grid[i-1][z] == 1){aliveCells++;}
                       if (grid[i-1][z+1] == 1){aliveCells++;}
                       if (grid[i][z-1] == 1){aliveCells++;}
                       if (grid[i][z+1] == 1){aliveCells++;}
                       if (grid[i+1][z-1] == 1){aliveCells++;}
                       if (grid[i+1][z] == 1){aliveCells++;}
                       if (grid[i+1][z+1] == 1){aliveCells++;}
                    }
                }
                if (grid[i][z]==1){
                    //System.out.println(aliveCells + " " + i + " " + z);
                }

                if (grid[i][z] == 1){
                    if (aliveCells < 2){
                        updateGrid[i][z] = 0;
                    }
                    if (aliveCells>3){
                        updateGrid[i][z] = 0;
                    }
                    if (aliveCells == 2){
                        updateGrid[i][z] = 1;
                    }
                    if (aliveCells == 3){
                        updateGrid[i][z] = 1;
                    }
                }
                else {
                    if (aliveCells ==3){
                        updateGrid[i][z] = 1;
                    }
                }
            }
        }
        for(int i = 0; i < x-1; i++) {
            for (int z = 0; z < y - 1; z++) {
                grid[i][z] = updateGrid[i][z];
            }
        }
    }
}
