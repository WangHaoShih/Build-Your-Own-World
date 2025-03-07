package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// This class does all the creation of Room's
public class Room {
    private TETile[][] world;
    private int width;
    private int height;
    private RandomUtils r = new RandomUtils();
    private Random random;
    private Map<Integer, int[]> coordinates = new HashMap<>();

    public Room(TETile[][] currentWorld, Random ra) {
        this.world = currentWorld;
        this.width = this.world.length;
        this.height = this.world[0].length - 2;
        this.random = ra;
    }

    public Map<Integer, int[]> roomData(){
        return this.coordinates;
    }

    // rooms are at least 4x4 size
    private void makeRoom(int roomNum, int startingX, int maxWidth, int maxHeight){
        if (maxWidth >= 4 && maxHeight >= 4) {
            int width = r.uniform(this.random, 4, maxWidth);
            int height = r.uniform(this.random, 4, maxHeight);
            int startingHeight = r.uniform(this.random, 4, this.height - height);
            int[] midXY = {Math.round((startingX + (startingX + width))/2), Math.round((startingHeight + (startingHeight + height))/2)};
            this.coordinates.put(roomNum, midXY);
            for (int x = startingX; x < startingX + width; x++) {
                for (int y = startingHeight; y < startingHeight + height; y++) {
                    if (x == startingX || x == startingX + (width - 1) || y == startingHeight || y == startingHeight + (height - 1)) {
                        this.world[x][y] = Tileset.FLOWER;
                    } else {
                        this.world[x][y] = Tileset.GRASS;
                    }
                }
            }
        }
    }

    //numOfRooms == 4, worldSize == 40x40
    public TETile[][] addRooms(int numOfRooms){
        int sectionWidth = this.width / numOfRooms;
        int sectionHeight = this.height / numOfRooms;
        int startingX = 0;
        for (int z = 0; z < numOfRooms; z++) {
            makeRoom(z, startingX,  sectionWidth, sectionHeight);
            startingX += sectionWidth;
        }
        return this.world;
    }
}
