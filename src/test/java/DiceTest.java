import Chessboard.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiceTest {

    private Game game;

    @BeforeEach
    void setup(){
        game = new Game();
        game.start_game();

    }


    @Test
    void testDice(){


        for ( int i = 0 ; i < 100 ; i++ )
        {
            int n = Dice.throwDice();
            assertTrue( n >= 1 && n <= 6 );
        }

    }



}
