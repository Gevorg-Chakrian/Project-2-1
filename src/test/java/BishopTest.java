
import Chessboard.Board;
import Chessboard.Game;
import Pieces.Bishop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopTest {


    Bishop bishop33;
    Board board;
    Game game;


    @BeforeEach
    void setup() {

        game = new Game();
        game.start_game();
        board = Board.getInstance();
        bishop33 = new Bishop(1, 3, 3);
    }

    @Test
    void testGreaterLesserOccupied() {

        assertTrue(bishop33.move(Board.getInstance().getSquares()[6][0],board));
    }

    @Test
    void testGreaterGreaterObstacle() {
        assertFalse(bishop33.move(Board.getInstance().getSquares()[7][7],board));
    }

    @Test
    void testLesserGreater() {
        assertTrue(bishop33.move(Board.getInstance().getSquares()[2][2],board));
    }

    @Test
    void testUnreachableSquareVertical() {
        assertFalse(bishop33.move(Board.getInstance().getSquares()[5][2],board));
    }

    @Test
    void testUnreachableSquareHorizontal() {
        assertFalse(bishop33.move(Board.getInstance().getSquares()[2][1],board));
    }

    @Test
    void testImmediatelyUnreachableSquareHorizontal() {
        assertFalse(bishop33.move(Board.getInstance().getSquares()[3][5],board));
    }

    @Test
    void testLegalButSameColor() {
        assertFalse(bishop33.move(Board.getInstance().getSquares()[1][5],board));
    }



}




