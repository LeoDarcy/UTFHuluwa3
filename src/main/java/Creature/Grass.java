package Creature;

public class Grass extends Creature{
    public Grass(){
        HP = 0;
        MP = 0;
        appearence = "/grass.png";
    }
    public Grass(int hp, int mp, String imgPath) {
        HP = hp;
        MP = mp;
        appearence = imgPath;
    }
}