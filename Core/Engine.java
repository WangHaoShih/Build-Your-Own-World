package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    TETile[][] worldframe;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 75;
    public static final int HEIGHT = 50;
    public static final int TILE_SIZE = 16;
    private static final String FILENAME = "byow/Core/saveWorld.txt";
    private static final int PAUSE1 = 50;
    private static final int PAUSE2 = 1000;
    private static final int SIZE1 = 30;
    private static final int MAXLOOP = 20;
    private static final int MAXTIME = 26;
    private boolean gameOver;
    private int avatar;
    private String directions;
    private String movements;
    private long seed;
    private Character characterConstructor;
    private TETile[][] finalWorldFrame;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        this.ter.initialize(WIDTH, HEIGHT);
        this.avatar = 0;
        drawMainMenu(this.avatar);
        freshWorld();
        boolean typed = false;
        while (!typed) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                char character = java.lang.Character.toUpperCase(letter);
                if (character == 'Q') {
                    System.exit(0);
                    typed = true;
                } else if (character == 'N') {
                    drawFrame("Type in your seed!");
                    this.seed = makeSeed();
                    Random random = new Random(this.seed);
                    this.finalWorldFrame = makeRooms(this.finalWorldFrame, random);
                    this.worldframe = makeCharacter(TETile.copyOf(this.finalWorldFrame));
                    draw();
                    typed = true;
                } else if (character == 'L') {
                    In scanner = new In(FILENAME);
                    if (scanner.hasNextLine()) {
                        makeCharacter(this.finalWorldFrame);
                        load();
                        drawCharacter(letter);
                        draw();
                        typed = true;
                    } 
                } else if (character == 'R') {
                    In scanner = new In(FILENAME);
                    if (scanner.hasNextLine()) {
                        String sd = scanner.readLine();
                        this.seed = Long.parseLong(sd);
                        Random random = new Random(this.seed);
                        scanner.readLine();
                        scanner.readLine();
                        this.movements = scanner.readLine();
                        this.avatar = Integer.parseInt(scanner.readLine());

                        this.finalWorldFrame = makeRooms(this.finalWorldFrame, random);
                        this.worldframe = makeCharacter(TETile.copyOf(this.finalWorldFrame));

                        StdDraw.clear();
                        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
                        StdDraw.setFont(font);

                        drawWorld();

                        StdDraw.show();
                        StdDraw.pause(PAUSE1);
                        for (int i = 0; i < this.movements.length(); i++) {
                            drawCharacter2(this.movements.charAt(i));
                            drawWorld();
                            StdDraw.show();
                            StdDraw.pause(PAUSE2);
                            StdDraw.clear();
                        }
                        draw();
                        typed = true;
                    }
                } else if (character == 'T') {
                    this.avatar++;
                    drawMainMenu(this.avatar % 3);
                }
            }
        }
    }

    private void drawCharacter2(char input) {
        int[] coords = this.characterConstructor.coordinates();
        if (input == 'W') {
            if (checkWall(coords[0], coords[1] + 1)) {
                this.characterConstructor.changeCoordinates(coords[0], coords[1] + 1);
            }
        } else if (input == 'A') {
            if (checkWall(coords[0] - 1, coords[1])) {
                this.characterConstructor.changeCoordinates(coords[0] - 1, coords[1]);
            }
        } else if (input == 'S') {
            if (checkWall(coords[0], coords[1] - 1)) {
                this.characterConstructor.changeCoordinates(coords[0], coords[1] - 1);
            }
        } else if (input == 'D') {
            if (checkWall(coords[0] + 1, coords[1])) {
                this.characterConstructor.changeCoordinates(coords[0] + 1, coords[1]);
            }
        }
        this.worldframe = this.characterConstructor.drawOnWorld(TETile.copyOf(this.finalWorldFrame));
    }

    private Long makeSeed() {
        String newseed = "";
        boolean typed = false;
        while (!typed) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                char character = java.lang.Character.toUpperCase(letter);
                if (!(character >= 'A' && character <= 'Z')) {
                    newseed += character;
                    drawFrame(newseed);
                } else if (character == 'S') {
                    return Long.parseLong(newseed);
                }
            }
        }
        return null;
    }

    private void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, SIZE1);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        /* If the game is not over, display encouragement, and let the user know if they
         * should be typing their answer or watching for the next round. */
        StdDraw.show();
    }

    private void drawMainMenu(int avatarint) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, SIZE1);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New World (N)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 2, "Load (L)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 4, "Replay (R)");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 6, "Quit (Q)");
        if (avatarint == 0) {
            StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 8, "Avatar (T): @");
        } else if (avatarint == 1) {
            StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 8, "Avatar (T): Tree");
        } else {
            StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 8, "Avatar (T): Mountain");
        }
        /* If the game is not over, display encouragement, and let the user know if they
         * should be typing their answer or watching for the next round. */
        StdDraw.show();
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if (input.charAt(0) == 'n') {
            String[] noN = input.split("n");
            String[] splitseed = noN[1].split("s", 2);
            String stringSeed = splitseed[0];
            if (splitseed.length > 1) {
                this.directions = splitseed[1];
            }
            this.seed = Long.parseLong(stringSeed);
        }
        this.avatar = 0;
        Random random = new Random(this.seed);

        this.finalWorldFrame = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.finalWorldFrame[x][y] = Tileset.SAND;
                if (HEIGHT - y <= 2) {
                    this.finalWorldFrame[x][y] = Tileset.NOTHING;
                }

            }
        }

        this.finalWorldFrame = makeRooms(this.finalWorldFrame, random);
        this.ter.initialize(WIDTH, HEIGHT);
        this.worldframe = makeCharacter(TETile.copyOf(this.finalWorldFrame));

        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            In scanner = new In(FILENAME);
            this.seed = Long.parseLong(scanner.readLine());
            load();
            drawCharacter('-');
            StringBuilder s = new StringBuilder();
            s.append(input);
            this.directions = s.deleteCharAt(0).toString();
        }

        boolean quit = false;
        if (this.directions != null) {
            for (int i = 0; i < this.directions.length(); i++) {
                char letter = this.directions.charAt(i);
                char character = java.lang.Character.toUpperCase(letter);
                if (character == 'W' || character == 'A'
                        || character == 'S' || character == 'D') {
                    drawCharacter(character);
                } else if (this.directions.charAt(i) == ':') {
                    if (this.directions.charAt(i + 1) == 'Q' || this.directions.charAt(i + 1) == 'q') {
                        quitAndSave();
                        quit = true;
                        break;
                    }
                }
            }
            drawCharacter('-');
        } else {
            this.worldframe = makeCharacter(TETile.copyOf(this.finalWorldFrame));
        }
        if (!quit) {
            draw();
        }
        return this.worldframe;
    }

    private void drawWorld() {
        this.ter.renderFrame(this.worldframe);
    }

    private void draw() {
        int loop = 0;
        int second = 0;
        this.gameOver = false;
        while (!this.gameOver) {
            StdDraw.clear();
            Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
            StdDraw.setFont(font);

            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                char character = java.lang.Character.toUpperCase(letter);
                if (letter == ':') {
                    quit();
                } else if (letter == 'L') {
                    load();
                    drawCharacter(letter);
                } else if (character == 'W' || character == 'A' || character == 'S' || character == 'D') {
                    drawCharacter(character);
                }
            }

            drawWorld();
            // 0.05 seconds
            // if we want 1 second we need 1.00/0.05 loops.
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            // [0][0] = Sand - description, avatar,
            drawTileName(mouseX, mouseY);
            StdDraw.show();
            StdDraw.pause(PAUSE1);
            loop++;
            if (loop == MAXLOOP) {
                loop = 0;
                second++;
                if (second == MAXTIME) {
                    this.gameOver = true;
                }
            }
        }
    }

    private void freshWorld() {
        this.finalWorldFrame = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.finalWorldFrame[x][y] = Tileset.SAND;
                if (HEIGHT - y <= 2) {
                    this.finalWorldFrame[x][y] = Tileset.NOTHING;
                }

            }
        }
    }

    private void load() {
        In scanner = new In(FILENAME);
        String sd = scanner.readLine();

        this.seed = Long.parseLong(sd);
        Random random = new Random(this.seed);

        freshWorld();
        this.finalWorldFrame = makeRooms(this.finalWorldFrame, random);

        int x = Integer.parseInt(scanner.readLine());
        int y = Integer.parseInt(scanner.readLine());
        this.characterConstructor.changeCoordinates(x, y);

        scanner.readLine();
        this.avatar = Integer.parseInt(scanner.readLine());
        scanner.close();
    }

    private void quitAndSave() {
        try {
            FileWriter saver = new FileWriter(FILENAME);
            saver.write(Long.toString(this.seed));
            saver.write("\n");
            int[] currcoords = this.characterConstructor.coordinates();
            saver.write(Integer.toString(currcoords[0]));
            saver.write("\n");
            saver.write(Integer.toString(currcoords[1]));
            saver.write("\n");
            saver.write(this.characterConstructor.returnMovements());
            saver.write("\n");
            saver.write(Integer.toString(this.avatar));
            saver.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        this.gameOver = true;
        // System.exit(0);
    }

    private void quit() {
        int n = 0;
        while (n < 1) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == 'Q') {
                    quitAndSave();
                }
                n++;
            }
        }
    }

    private TETile[][] makeCharacter(TETile[][] world) {
        this.characterConstructor = new Character(world, this.avatar);
        return this.characterConstructor.returnWorld();
    }

    // Places the hallways within the world
    private TETile[][] makeHallways(TETile[][] world, Map<Integer, int[]> coordinates) {
        Hallway hallwayConstructor = new Hallway(world, coordinates);
        return world;
    }

    // Places the rooms within the world
    private TETile[][] makeRooms(TETile[][] world, Random random) {
        Room roomConstructor = new Room(world, random);

        RandomUtils r = new RandomUtils();
        int numOfRooms = RandomUtils.uniform(random, 4, 8);

        TETile[][] newWorld = roomConstructor.addRooms(numOfRooms);
        return makeHallways(newWorld, roomConstructor.roomData());
    }

    private boolean checkWall(int x, int y) {
        return this.worldframe[x][y].description().equals("grass");
    }

    private void drawCharacter(char input) {
        int[] coords = this.characterConstructor.coordinates();
        if (input == 'W') {
            if (checkWall(coords[0], coords[1] + 1)) {
                this.characterConstructor.changeCoordinates(coords[0], coords[1] + 1);
                this.characterConstructor.addMovement('W');
            }
        } else if (input == 'A') {
            if (checkWall(coords[0] - 1, coords[1])) {
                this.characterConstructor.changeCoordinates(coords[0] - 1, coords[1]);
                this.characterConstructor.addMovement('A');
            }
        } else if (input == 'S') {
            if (checkWall(coords[0], coords[1] - 1)) {
                this.characterConstructor.changeCoordinates(coords[0], coords[1] - 1);
                this.characterConstructor.addMovement('S');
            }
        } else if (input == 'D') {
            if (checkWall(coords[0] + 1, coords[1])) {
                this.characterConstructor.changeCoordinates(coords[0] + 1, coords[1]);
                this.characterConstructor.addMovement('D');
            }
        }
        this.worldframe = this.characterConstructor.drawOnWorld(TETile.copyOf(this.finalWorldFrame));
    }

    private void drawTileName(int x, int y) {
        if (x < WIDTH && y < HEIGHT - 2) {
            StdDraw.setPenColor(StdDraw.WHITE);
            Font fontBig = new Font("Monaco", Font.BOLD, SIZE1);
            StdDraw.setFont(fontBig);
            StdDraw.textLeft(WIDTH / 2, HEIGHT - 1, this.worldframe[x][y].description());
        }
    }
}
