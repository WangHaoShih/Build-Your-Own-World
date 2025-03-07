package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Character {
    private boolean check;
    private TETile[][] newworld;
    private int[] coords;
    private String movements;
    private int avatarnum;
    public Character(TETile[][] world, int avatar) {
        this.avatarnum = avatar;
        this.check = false;
        this.newworld = world;
        this.coords = new int[2];
        this.movements = "";
        for (int x = 0; x < this.newworld.length; x++) {
            for (int y = 0; y < this.newworld[0].length; y++) {
                if (!this.check && this.newworld[x][y].description() == "grass") {
                    if (this.avatarnum == 0) {
                        this.newworld[x][y] = Tileset.AVATAR;
                    } else if (this.avatarnum == 1) {
                        this.newworld[x][y] = Tileset.TREE;
                    } else {
                        this.newworld[x][y] = Tileset.MOUNTAIN;
                    }
                    this.check = true;
                    this.coords[0] = x;
                    this.coords[1] = y;
                }
            }
        }
    }

    public TETile[][] returnWorld() {
        return this.newworld;
    }

    public TETile[][] drawOnWorld(TETile[][] world) {
        if (this.avatarnum == 0) {
            world[this.coords[0]][this.coords[1]] = Tileset.AVATAR;
        } else if (this.avatarnum == 1) {
            world[this.coords[0]][this.coords[1]] = Tileset.TREE;
        } else {
            world[this.coords[0]][this.coords[1]] = Tileset.MOUNTAIN;
        }

        return world;
    }

    public int[] changeCoordinates(int x, int y) {
        this.coords[0] = x;
        this.coords[1] = y;
        return this.coords;
    }

    public void addMovement(char c) {
        this.movements += c;
    }

    public String returnMovements() {
        return this.movements;
    }
    public int[] coordinates() {
        return this.coords;
    }
}
