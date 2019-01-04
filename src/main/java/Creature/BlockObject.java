package Creature;

public class BlockObject implements Identity{
    protected String name;
    protected String appearence;
    public BlockObject(){
        name = "Block Object";
        appearence = "/rock.jpg";
    }
    public void Show(){
        System.out.println(name);
    }
    public String Identity(){
        return appearence;
    }
}