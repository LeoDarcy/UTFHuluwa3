package BattleField;
import Creature.Creature;
import Creature.Identity;
import Creature.ItemType;
import Creature.CreateFactory;
public class Holder {
    private Object item;
    private ItemType type;
    //public String appearance;
    public int posx;
    public int posy;
    public Holder(int x, int y, ItemType t){
        posx = x;
        posy = y;
        type = t;
        item = new CreateFactory().Creature(t);
    }
    public Holder(int x, int y, ItemType t, Object o){
        posx = x;
        posy = y;
        type = t;
        item = o;
    }
    public ItemType getType() {
        return type;
    }

    public Object getItem() {
        return item;
    }

    public void setType(ItemType t){
        type = t;
    }

    public void setItem(Object o){
        item = o;
    }
    public String Getappearance() {
        if(item instanceof Identity){
            Identity a = (Identity)item;
            return a.Identity();
        }
        else{
            return "resource/black.jpg";
        }
    }

    public int GetProperty(){
        if(item instanceof Creature){
            Creature cr = (Creature) item;
            return cr.getProperty();
        }
        System.out.println("访问出错！访问了非生物体的Property");
        return 0;
    }
    public int GetAttack(){
        if(item instanceof Creature){
            Creature cr = (Creature) item;
            return cr.GetAttack();
        }
        System.out.println("访问出错！访问了非生物体的Attack");
        return 0;
    }
    public boolean GetHurted(){
        if(item instanceof Creature){
            Creature cr = (Creature) item;
            boolean tmp = cr.GetHurted();
            if(tmp == false)
                type = ItemType.Rock;
            return tmp;
        }
        else
            assert (true);
        return false;
    }
    public boolean IsLivingCreature(){
        if(item instanceof Creature){
            Creature cr = (Creature) item;
            if(cr.getHP() <= 0)
                return false;
            else
                return true;
        }
        return false;
    }
}