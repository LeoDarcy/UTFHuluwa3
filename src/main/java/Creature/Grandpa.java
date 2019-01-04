package Creature;

public class Grandpa extends Creature {
    public Grandpa(){
        super();
        appearence = "/grandpa.png";
        property = 1;//´ú±íºÃÈË
    }
    @Override
    public int GetAttack(){
        return (int)Math.random()*10 - 3;
    }
}