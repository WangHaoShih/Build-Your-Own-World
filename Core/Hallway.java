package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Map;

public class Hallway {
    private Map<Integer, int[]> data;
    private TETile[][] newworld;

    public Hallway(TETile[][] world, Map<Integer, int[]> coordinates) {
        this.data = coordinates;
        this.newworld = world;
        connect();
    }
    // this.newworld[0][0] == "SAND"
    private void connectXaxis(int x1, int y1, int x2) {
        for (int x = x1; x < x2; x++) {
            for (int y = y1 - 1; y < y1 + 2; y++) {
                if (this.newworld[x][y] == Tileset.SAND || this.newworld[x][y] == Tileset.FLOWER) {
                    if (y == y1) {
                        this.newworld[x][y] = Tileset.GRASS;
                    } else {
                        this.newworld[x][y] = Tileset.FLOWER;
                    }
                }
            }
        }
    }

    private void connectYaxis(int y1, int x2, int y2) {
        if (y1 < y2) {
            for (int y = y1 - 1; y < y2; y++) {
                for (int x = x2 - 2; x < x2 + 1; x++) {
                    if (this.newworld[x][y] == Tileset.SAND || this.newworld[x][y] == Tileset.FLOWER) {
                        if (x == x2 - 1 && y != y1 -1) {
                            this.newworld[x][y] = Tileset.GRASS;
                        } else {
                            this.newworld[x][y] = Tileset.FLOWER;
                        }
                    }
                }
            }
        } else {
            for (int y = y2; y < y1 + 2; y++) {
                for (int x = x2 - 2; x < x2 + 1; x++) {
                    if (this.newworld[x][y] == Tileset.SAND || this.newworld[x][y] == Tileset.FLOWER) {
                        if (x == x2 - 1 && y != y1 + 1) {
                            this.newworld[x][y] = Tileset.GRASS;
                        } else {
                            this.newworld[x][y] = Tileset.FLOWER;
                        }
                    }
                }
            }
        }

    }


    private void connect() {
        int[] firstRoomCoord;
        int[] secondRoomCoord;
        for (int i : this.data.keySet()) {
            if (i == this.data.size() - 1) {
                break;
            }
            firstRoomCoord = this.data.get(i);
            int x1 = firstRoomCoord[0];
            int y1 = firstRoomCoord[1];
            secondRoomCoord = this.data.get(i + 1);
            int x2 = secondRoomCoord[0];
            int y2 = secondRoomCoord[1];
            connectXaxis(x1, y1, x2);
            connectYaxis(y1, x2, y2);
        }
    }
}
