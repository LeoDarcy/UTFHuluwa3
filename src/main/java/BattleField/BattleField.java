package BattleField;

import Creature.CreateFactory;
import Creature.ItemType;
import MyThread.StateType;
import UI.Bullet;
import UI.Controller;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;

public class BattleField{
    final static int row = 10;
    final static int col = 10;

    final static int EachWidth = 50;
    final static int EachHeight = 50;
    private Holder[][] map;
    private ArrayList<Position> mapset;
    private String mapurl;
    private String maptype;
    private String image;
    private CreateFactory factory;
    //private ArrayList<Bullet> bullets;
    public BattleField() {
        factory = new CreateFactory();
        //bullets = new ArrayList<Bullet>();
        //初始化地图
        map = new Holder[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = new Holder(i, j, ItemType.Background);
            }
        }
        mapset = new ArrayList<Position>();
        SetMap("默认");
        image = "/map.jpg";
    }

    public void Reload() {
        //可以加入地图元素
        //初始化地图
        map = new Holder[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = new Holder(i, j, ItemType.Background);
            }
        }
        for (Position p : mapset) {
            map[p.x][p.y].setType(ItemType.Rock);
            map[p.x][p.y].setItem(factory.CreateBlocked(maptype));
        }
    }

    public String getImage() {
        return image;
    }

    //得到地图信息
    public Holder[][] GetMap() {
        return map;
    }

    public boolean SetCreature(int posx, int posy, ItemType t, Object o) {
        if (map[posx][posy].getType() == ItemType.Background) {
            map[posx][posy] = new Holder(posx, posy, t, o);
            return true;
        } else
            return false;
    }

    //加载地图
    public boolean LoadMap() {
        return false;
    }

    public void SetMap(String type) {
        mapset.clear();
        maptype = type;
        if (maptype.equals("山林")) {
            mapurl = "/mountain.txt";
        } else if (maptype.equals("江河")) {
            mapurl = "/sea.txt";
        } else {
            mapurl = "/map.txt";
            return;
        }
        try {
            //File file=new File(mapurl);
            //System.out.println(file.exists());
            InputStream in = getClass().getResourceAsStream(mapurl);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //BufferedReader br = new BufferedReader(new FileReader(mapurl));
            String num;
            while ((num = br.readLine()) != null) {
                int px = Integer.parseInt(num);
                num = br.readLine();
                int py = Integer.parseInt(num);
                Position tmp = new Position(px, py);
                mapset.add(tmp);
            }
        } catch (Exception e) {
            System.out.println("读取文件出错！");
            e.printStackTrace();
        }
    }




    public synchronized StateType ExchangePosition(int lastx, int lasty, int newx, int newy) {
        if (newx < 0 || newy < 0 || newx >= row || newy >= col)
            return StateType.STAY;
        if (map[lastx][lasty].getType() == ItemType.Background)
            return StateType.STAY;
        if(!map[lastx][lasty].IsLivingCreature())
            return StateType.STAY;
        ItemType newtype = map[newx][newy].getType();
        if (newtype == ItemType.Rock)
            return StateType.STAY;
        //交换
        if (newtype == ItemType.Background) {
            ItemType t = map[lastx][lasty].getType();
            Object j = map[lastx][lasty].getItem();
            map[lastx][lasty].setType(newtype);
            map[lastx][lasty].setItem(map[newx][newy].getItem());
            map[newx][newy].setItem(j);
            map[newx][newy].setType(t);
            return StateType.MOVE;
        } else {
            if (map[newx][newy].GetProperty() != map[lastx][lasty].GetProperty()) {
                //战斗开始
                int atkl = map[lastx][lasty].GetAttack();
                int atkn = map[newx][newy].GetAttack();
                if(atkl >= atkn){
                    if (map[newx][newy].GetHurted() == false)
                        map[newx][newy].setType(ItemType.Rock);
                    return StateType.ATTACK;
                }
                else{
                    if (map[lastx][lasty].GetHurted() == false) {
                        map[lastx][lasty].setType(ItemType.Rock);
                        return StateType.DEAD;
                    }
                    return StateType.STAY;
                }
                /*int victoy = (int) Math.random() * 2;
                if (victoy == 0) {
                    System.out.println("坐标：" + lastx + "，" + lasty + "受伤！");
                    if (map[lastx][lasty].GetHurted() == false)
                        map[lastx][lasty].setType(ItemType.Rock);
                    return StateType.STAY;
                } else {
                    System.out.println("坐标：" + lastx + "，" + lasty + "受伤！");
                    if (map[newx][newy].GetHurted() == false)
                        map[newx][newy].setType(ItemType.Rock);
                    return StateType.MOVE;
                }*/
                //return true;
            } else
                return StateType.STAY;
        }
    }
}
