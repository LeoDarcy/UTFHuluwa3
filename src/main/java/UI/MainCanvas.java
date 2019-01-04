package UI;

import BattleField.*;
import Creature.CreateFactory;
import Creature.*;
import MyThread.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.concurrent.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

import java.beans.EventHandler;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.paint.*;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.beans.property.DoubleProperty;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import static java.lang.Thread.sleep;


public class MainCanvas{

    // ??????
    //private GameMap gameMap;
    private Canvas canvas;
    private GraphicsContext gContext;
    private BattleField battleField;
    private Image map;
    private TextArea textField;
    private Text clock;
    final static int row = 10;
    final static int col = 10;
    final static int EachWidth = 50;
    final static int EachHeight = 50;
    final static int OffX = 100;
    final static int OffY = 100;
    private boolean isRunning = true;
    private long sleep = 100;
    private DoubleProperty opacity;
    private Timeline timeline;
    private AnimationTimer timer;
    private AnimationTimer loader;
    private ArrayList<MyThread> monster;
    private ArrayList<MyThread> heros;
    private ArrayList<Position> strategyset;
    private volatile ArrayList<Bullet> bullets;
    private long beginTime;
    private BufferedWriter record;
    private BufferedReader reader;
    private AudioClip music;

    public MainCanvas(Canvas c, TextArea t, Text cl) {
        canvas = c;
        textField = t;
        gContext = canvas.getGraphicsContext2D();
        heros = new ArrayList<MyThread>();
        monster = new ArrayList<MyThread>();
        battleField = new BattleField();
        strategyset = new ArrayList<Position>();
        bullets = new ArrayList<Bullet>();
        clock = cl;
        InitialTimer();
        SetStrategy("默认");
        SetMap("长蛇阵");
        record = null;
        reader = null;
        PrepareText();
        music = new AudioClip(getClass().getResource("/music/葫芦娃.mp3").toString());
        music.setCycleCount(1000);
        music.play();
    }

    public void drawDead(int posx, int posy) {
        //gContext.drawImage(new Image("resource/grass.png"), posy * EachWidth, posx * EachHeight);
        Image image = new Image("/ghost.jpg");
        gContext.setGlobalAlpha(opacity.get());//
        gContext.setEffect(new BoxBlur(EachWidth, EachHeight, 0));
        gContext.drawImage(image, TranslateYPosition(posy), TranslateXPosition(posx));
        gContext.setGlobalAlpha(1);//
    }

    public void drawCreature(int oldx, int oldy, int posx, int posy, Image image) {
        //Image oldimage = new Image("resource/grass.png");
        //gContext.drawImage(oldimage, oldy * EachWidth, oldx * EachHeight);
        gContext.drawImage(image, TranslateYPosition(posy), TranslateXPosition(posx));
    }

    public void drawCreature(MyThread th) {
        Image image = new Image(th.GetAppearance());
        gContext.drawImage(image, TranslateYPosition(th.nowy), TranslateXPosition(th.nowx));
    }

    private int checkThread(ArrayList<MyThread> threads) {
        int num = 0;
        for (MyThread th : threads) {
            //System.out.println("Draw " + th.nowx + ", " + th.nowy);
            if (th.live) {
                num++;
                drawCreature(th.lastx, th.lasty, th.nowx, th.nowy, new Image(th.GetAppearance()));
                WriteRecord(String.valueOf(TranslateXPosition(th.nowx)));
                WriteRecord(String.valueOf(TranslateYPosition(th.nowy)));
                WriteRecord(String.valueOf(th.GetAppearance()));
            } else {
                drawDead(th.nowx, th.nowy);
                WriteRecord(String.valueOf(TranslateXPosition(th.nowx)));
                WriteRecord(String.valueOf(TranslateYPosition(th.nowy)));
                WriteRecord("/ghost.jpg");
            }
        }
        return num;
    }

    private void PrepareText() {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        clock.setEffect(ds);
        clock.setCache(true);
        clock.setFill(Color.RED);
        clock.setFont(Font.font(null, FontWeight.BOLD, 32));
    }

    private long drawClock() {
        long last = System.currentTimeMillis() - beginTime;
        //gContext.setFont(gContext.getFont().getFamily(), 30);//new Font("Verdana", 70)Font.font("Verdana", FontWeight.BOLD, 70)
        clock.setText("时间" + last / 1000 + "秒");
        WriteRecord(String.valueOf(last / 1000));
        /*DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
        if ( last < 0 ) {
            //  timeLabel.setText( "00:00:00" );
            gContext.fillText( "haha" +  timeFormat.format( 0 ),50,50 );
        } else {
            gContext.strokeText( "haha" + last/1000, 50,50 );
            gContext.drawImage(new Image("resource/ghost.jpg"), 0, 0);
        }*/
        return last;
    }

    public void InitialTimer() {
        //初始化时间线
        opacity = new SimpleDoubleProperty();
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(opacity, 1)
                ),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(opacity, 0)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        //game loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (heros.isEmpty() && monster.isEmpty())
                    return;
                drawBackground();
                int good = checkThread(heros);
                int bad = checkThread(monster);
                LookThroughBullets();
                WriteRecord("time");
                if (good == 0 && bad > 0) {//妖怪胜利
                    clock.setText(clock.getText() + "   妖怪胜利");
                    WriteRecord("Victory");
                    WriteRecord("   妖怪胜利");
                    StopAllThread(heros);
                    StopAllThread(monster);
                    stop();
                    return;
                } else if (good > 0 && bad == 0) {//???????
                    clock.setText(clock.getText() + "   葫芦娃胜利");
                    WriteRecord("Victory");
                    WriteRecord("   葫芦娃胜利");
                    StopAllThread(heros);
                    StopAllThread(monster);
                    stop();
                    return;
                }
                if (drawClock() > 60000) {
                    System.out.println("Too long time");
                    WriteRecord("End");
                    WriteRecord("   打平");
                    clock.setText(clock.getText() + "   打平");
                    StopAllThread(heros);
                    StopAllThread(monster);
                    stop();
                } else
                    WriteRecord("Next");
            }
        };
        //gameMap = new GameMap(tileWidth, tileHeight, map);
        loader = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    sleep(25);
                    canvas.getGraphicsContext2D().drawImage(new Image(battleField.getImage()), 0, 0);
                    String num;
                    if ((num = reader.readLine()) != null) {
                        while (!num.equals("time")) {
                            int posx = Integer.parseInt(num);
                            num = reader.readLine();
                            int posy = Integer.parseInt(num);
                            String im = reader.readLine();
                            gContext.drawImage(new Image(im), posy, posx);
                            num = reader.readLine();
                        }
                        String time = reader.readLine();
                        if (time.equals("Victory")) {
                            String victor = reader.readLine();
                            clock.setText(clock.getText() + victor);
                            stop();
                            reader.close();
                        } else{
                            clock.setText(time + "秒");
                            String end = reader.readLine();
                            if (end.equals("End")) {
                                clock.setText(clock.getText() + reader.readLine());
                                stop();
                                reader.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("loader出错");
                    assert(true);
                    e.printStackTrace();
                }
            }
        }

        ;
        //thread.start();
    }

    private void StopAllThread(ArrayList<MyThread> threads) {
        for (MyThread th : threads)
            th.interrupt();
        threads.clear();
    }

    public void drawBackground() {
        //gameMap.drawMap(gContext);
        canvas.getGraphicsContext2D().drawImage(new Image(battleField.getImage()), 0, 0);
        gContext.fillRect(OffY, OffX, 500,2);
        gContext.fillRect(OffY, OffX, 2,500);
        gContext.fillRect(OffY + 500, OffX, 2,500);
        gContext.fillRect(OffY, OffX + 500, 500,2);
        Holder[][] map = battleField.GetMap();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                //"resource/grass.png"
                try {
                    Image image = new Image(map[i][j].Getappearance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (map[i][j].getType() == ItemType.Rock && map[i][j].getItem() instanceof BlockObject) {
                    canvas.getGraphicsContext2D().drawImage(new Image(map[i][j].Getappearance()), TranslateYPosition(j), TranslateXPosition(i));
                    WriteRecord(String.valueOf(TranslateXPosition(i)));
                    WriteRecord(String.valueOf(TranslateYPosition(j)));
                    WriteRecord(map[i][j].Getappearance());
                }
            }
        }
    }

    public void PrepareBat() {
        //准备战斗
        CreateFactory factory = new CreateFactory();
        int num_of_hero = 7;
        for (int i = 0; i < num_of_hero; i++) {
            Huluwa man = factory.CreateHuluwa();
            battleField.SetCreature(2 + i, 2, ItemType.Huluwa, man);
            MyThread huluwa = new MyThread(2 + i, 2, battleField, man, textField, this);
            heros.add(huluwa);
        }
        Grandpa g = factory.CreateGrandpa();
        battleField.SetCreature(6, 1, ItemType.Grandpa, g);
        MyThread grandpa = new MyThread(6, 1, battleField, g, textField, this);
        heros.add(grandpa);
        int num_of_badguy = strategyset.size();
        for (int i = 0; i < num_of_badguy; i++) {
            if (i != num_of_badguy - 1) {
                Badguy man = factory.CreateBadguy();
                Position p = strategyset.get(i);
                if (battleField.SetCreature(p.x, p.y, ItemType.BadGuy, man)) {
                    MyThread badguy = new MyThread(p.x, p.y, battleField, man, textField, this);
                    monster.add(badguy);
                }
            } else {
                Snake s = factory.CreateSnake();
                Position p = strategyset.get(num_of_badguy - 1);
                if (battleField.SetCreature(p.x, p.y, ItemType.Snake, s)) {
                    MyThread snake = new MyThread(p.x, p.y, battleField, s, textField, this);
                    monster.add(snake);
                }
            }
        }

    }

    public void StartFight() {
        //战斗开始
        try {
            //File path = new File("/records.txt");
            //if (!path.exists()) {
            //    path.createNewFile();
            //}
            //record = new BufferedWriter(new FileWriter(path));records.txt"
            record = new BufferedWriter(new FileWriter("records.txt"));
        } catch (Exception e) {
            System.out.println("初始化文件有错！");
            e.printStackTrace();
        }
        music.stop();
        music = new AudioClip(getClass().getResource("/music/仙妖乱.mp3").toString());
        music.setCycleCount(1000);
        music.play();
        battleField.Reload();
        PrepareBat();
        timeline.play();
        timer.start();
        beginTime = System.currentTimeMillis();
        PrepareText();
        //AddBullet(0, 2, 0, 3);
        for (MyThread th : heros)
            th.start();
        for (MyThread th : monster)
            th.start();
    }
    @Deprecated
    public synchronized void update() {//不用了
        double W = 200; // canvas dimensions.
        double H = 200;
        double D = 20;  // diameter.
        Image im = new Image("resource/boom.jpg");
        /*DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(x, 0),
                        new KeyValue(y, 0)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(x, 0),//new KeyValue(x, W -D),
                        new KeyValue(y, H - D)
                )
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(Timeline.INDEFINITE);*/
        /*
        //final Canvas canvas = new Canvas(W, H);
        timer.start();
        timeline.play();*/
    }

    public int TranslateXPosition(int x) {
        return OffX + x * EachHeight;
    }

    public int TranslateYPosition(int y) {
        return OffY + y * EachWidth;
    }

    public void SetStrategy(String strategytype) {
        String url;
        strategyset.clear();
        if (strategytype.equals("锋矢阵")) {
            url = "/strategy/fengshi.txt";
        } else if (strategytype.equals("雁行阵")) {
            url = "/strategy/yanxing.txt";
        } else if (strategytype.equals("方圆阵")) {
            url = "/strategy/fangyuan.txt";
        } else
            url = "/strategy/changshe.txt";
        try {
            InputStream in = getClass().getResourceAsStream(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String num;
            while ((num = br.readLine()) != null) {
                int posx = Integer.parseInt(num);
                num = br.readLine();
                int posy = Integer.parseInt(num);
                Position p = new Position(posx, posy);
                strategyset.add(p);
            }
        } catch (Exception e) {
            System.out.println("读取文件出错");
            e.printStackTrace();
        }
    }

    public void SetMap(String maptype) {
        battleField.SetMap(maptype);
    }

    public synchronized void AddBullet(int fromx, int fromy, int tox, int toy) {
        Bullet p = new Bullet(fromx, fromy, tox, toy);
        bullets.add(p);
    }

    //public ArrayList<Bullet> GetBullets(){
    //return bullets;
    //}
    public synchronized void LookThroughBullets() {
        //ArrayList<Bullet> bullets = battleField.GetBullets();
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet p = it.next();
            if (p.Finished())
                it.remove();
            else {
                gContext.drawImage(new Image(p.GetImage()), p.Gety(), p.Getx());
                WriteRecord(String.valueOf(p.Getx()));
                WriteRecord(String.valueOf(p.Gety()));
                WriteRecord(p.GetImage());
            }
        }
    }

    public void LoadBattle(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
            loader.start();
            //Doc loading = new Doc();
            //loading.run();
        } catch (Exception e) {
            System.out.println("读取出错");
        }
    }

    public void WriteRecord(String s) {
        try {
            record.write(s + "\n");
            record.flush();
        } catch (Exception e) {
            System.out.println("写文件出错");
        }
    }

    public void SaveFile(File file) {
        try {
            File path = new File("records.txt");
            if (!path.exists()) {
                return;
            }
            Files.copy(path.toPath(), file.toPath());
        } catch (Exception e) {
            System.out.println("存文件出错！");
            e.printStackTrace();
        }
    }
}
