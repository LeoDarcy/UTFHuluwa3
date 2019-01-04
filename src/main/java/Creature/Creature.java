package Creature;

public class Creature implements Identity{
    protected String name;
    protected int HP;
    protected int MP;
    protected String appearence;
    protected int property;
    public Creature(){
        HP = 1;
        MP = 0;
        name = "grass";
        appearence = "/grass.png";
        property = 0;
    }
    public int getHP(){return HP;}
    public int getMP() {
        return MP;
    }
    public int getProperty(){return property;}
    public void Show(){
        System.out.println(name);
    }
    public String Identity(){
        return appearence;
    }
    public int GetAttack(){
        return (int)Math.random()*10;
    }
    public boolean GetHurted(){
        HP--;
        if(HP <= 0)
            return false;
        else
            return true;
    }
    public void SetAppearance(String face){
        appearence = face;
    }
}