import BattleField.*;
import Creature.CreateFactory;
import Creature.ItemType;
import UI.Main;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * BattleField Tester.
 *
 * @author <Authors name>
 * @since <pre>Ê®¶þÔÂ 18, 2018</pre>
 * @version 1.0
 */
public class TestBattleField {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: Reload()
     *
     */
    @Test
    public void testReload() throws Exception {
//TODO: Test goes here...
    }

    /**
     *
     * Method: getImage()
     *
     */
    @Test
    public void testGetImage() throws Exception {
//TODO: Test goes here...
    }

    /**
     *
     * Method: GetMap()
     *
     */
    @Test
    public void testGetMap() throws Exception {
//TODO: Test goes here...
    }

    /**
     *
     * Method: SetCreature(int posx, int posy, ItemType t, Object o)
     *
     */
    @Test
    public void testSetCreature() throws Exception {
//TODO: Test goes here...
        BattleField battleField = new BattleField();
        battleField.SetCreature(0,0, ItemType.Snake, new CreateFactory().CreateSnake());
        Holder[][] map = battleField.GetMap();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(map[i][j].getType() + "    ");
            }
            System.out.println(" ");
        }
        System.out.print(map);
    }

    /**
     *
     * Method: LoadMap()
     *
     */
    @Test
    public void testLoadMap() throws Exception {
//TODO: Test goes here...
    }

    /**
     *
     * Method: SetMap(String type)
     *
     */
    @Test
    public void testSetMap() throws Exception {
//TODO: Test goes here...
        //Main main = new Main();
        //JFXPanel jfxPanel = new JFXPanel();
        BattleField battleField = new BattleField();
        battleField.SetMap("³¤ÉßÕó");
        battleField.Reload();
        Holder[][] map = battleField.GetMap();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(map[i][j].getType() + "    ");
            }
            System.out.println(" ");
        }
        System.out.print(map);
    }

    /**
     *
     * Method: ExchangePosition(int lastx, int lasty, int newx, int newy)
     *
     */
    @Test
    public void testExchangePosition() throws Exception {
//TODO: Test goes here...
    }


}