package MyThread;

import BattleField.BattleField;
import Creature.Creature;
import UI.Main;
import UI.MainCanvas;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MyThread extends Thread {
    public int lastx;
    public int lasty;
    public int nowx;
    public int nowy;
    public StateType laststate;
    public StateType nowstate;
    public boolean live;
    private BattleField battleField;
    private TextArea textField;
    private MainCanvas canvas;
    //boolean cartoon;
    private Creature creature;

    public int getNowx() {
        return nowx;
    }

    public String GetAppearance() {
        return creature.Identity();
    }

    public MyThread(int x, int y, BattleField b, Creature c, TextArea t, MainCanvas m) {
        lastx = x;
        lasty = y;
        nowx = x;
        nowy = y;
        live = true;
        laststate = StateType.MOVE;
        nowstate = StateType.MOVE;
        //live = false;
        //cartoon = false;
        battleField = b;
        creature = c;
        textField = t;
        canvas = m;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                sleep(1000);
                int direction = (int) (Math.random() * 4);//获得随机数
                //direction = 1;
                boolean flag = true;
                while (live == true && flag == true) {
                    switch (direction) {
                        case 0: {
                            StateType state = battleField.ExchangePosition(this.nowx, this.nowy, this.nowx, this.nowy - 1);
                            if (state == StateType.MOVE) {
                                //System.out.println("from (" + this.lastx + ", "+ this.lasty + ") to (" + this.nowx + ", " + (this.nowy - 1) + ")");
                                //System.out.println("from (" + this.nowx + ", "+ this.nowy + ") to Y--");
                                /*int tmp = (this.nowy - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）到（" + this.nowx + ", " + tmp + "）\r\n");
                                });*/
                                this.lastx = this.nowx;
                                this.lasty = this.nowy;
                                this.nowy--;
                                flag = false;
                            }
                            else if(state == StateType.ATTACK){
                                flag = false;
                                int tmp = (this.nowy - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）攻击（" + this.nowx + ", " + tmp + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy, nowx, nowy-1);
                            }
                            else if(state == StateType.DEAD){
                                flag = false;
                                int tmp = (this.nowy - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）受攻击（" + this.nowx + ", " +  tmp + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy-1, nowx, nowy);
                            }
                        }break;
                        case 1: {
                            StateType state = battleField.ExchangePosition(this.nowx, this.nowy, this.nowx, this.nowy + 1);
                            if (state == StateType.MOVE) {
                                //System.out.println("from (" + this.lastx + ", "+ this.lasty + ") to (" + this.nowx + ", " + (this.nowy + 1) + ")");
                                //System.out.println("from (" + this.nowx + ", "+ this.nowy + ") to Y++");
                                /*int tmp = (this.nowy + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）到（" + this.nowx + ", " + tmp + "）\r\n");
                                });*/
                                this.lastx = this.nowx;
                                this.lasty = this.nowy;
                                this.nowy++;
                                flag = false;
                            }
                            else if(state == StateType.ATTACK){
                                flag = false;
                                int tmp = (this.nowy + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）攻击（" + this.nowx + ", " + tmp + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy, nowx, nowy+1);
                            }
                            else if(state == StateType.DEAD){
                                flag = false;
                                int tmp = (this.nowy + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）受攻击（" + this.nowx + ", " +  tmp + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy+1, nowx, nowy);
                            }
                        }break;
                        case 2: {
                            StateType state = battleField.ExchangePosition(this.nowx, this.nowy, this.nowx - 1, this.nowy);
                            if (state == StateType.MOVE) {
                                //System.out.println("from (" + this.lastx + ", "+ this.lasty + ") to (" + (this.nowx - 1) + ", " + this.nowy + ")");
                                //System.out.println("from (" + this.nowx + ", "+ this.nowy + ") to X--");
                                /*int tmp = (this.nowx - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）到（" + tmp + ", " + this.nowy + "）\r\n");
                                });*/
                                this.lastx = this.nowx;
                                this.lasty = this.nowy;
                                this.nowx--;
                                flag = false;
                            } else if (state == StateType.ATTACK) {
                                flag = false;
                                int tmp = (this.nowx - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）攻击（" + tmp + ", " + this.nowy + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy, nowx-1, nowy);
                            }else if(state == StateType.DEAD){
                                flag = false;
                                int tmp = (this.nowx - 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）受攻击（" + tmp + ", " + this.nowy + "）\r\n");
                                });
                                canvas.AddBullet(nowx-1, nowy, nowx, nowy);
                            }
                        }break;
                        case 3: {
                            StateType state = battleField.ExchangePosition(this.nowx, this.nowy, this.nowx + 1, this.nowy);
                            if (state == StateType.MOVE) {
                                //System.out.println("from (" + this.lastx + ", "+ this.lasty + ") to (" + (this.nowx + 1)+ ", " + this.nowy + ")");
                                //System.out.println("from (" + this.nowx + ", "+ this.nowy + ") to X++");
                                /*int tmp = (this.nowx + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）到（" + tmp + ", " + this.nowy + "）\r\n");
                                });*/
                                this.lastx = this.nowx;
                                this.lasty = this.nowy;
                                this.nowx++;
                                flag = false;
                            } else if (state == StateType.ATTACK) {
                                flag = false;
                                int tmp = (this.nowx + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）攻击（" + tmp + ", " + this.nowy + "）\r\n");
                                });
                                canvas.AddBullet(nowx, nowy, nowx + 1, nowy);
                            }else if(state == StateType.DEAD){
                                flag = false;
                                int tmp = (this.nowx + 1);
                                Platform.runLater(() -> {
                                    textField.appendText("从（" + this.nowx + ", " + this.nowy + "）受攻击（" + tmp + ", " + this.nowy + "）\r\n");
                                });
                                canvas.AddBullet(nowx+1, nowy, nowx, nowy);
                            }
                        }break;
                    }
                    direction = (int) (Math.random() * 3);
                    if (creature.getHP() <= 0) {
                        Platform.runLater(() -> {
                            textField.appendText("位于（" + this.nowx + ", " + this.nowy + "）死亡！\r\n");
                        });
                        sleep(800);
                        nowstate = StateType.DEAD;
                        live = false;
                        interrupt();
                        System.out.println("线程退出");
                    }
                    //direction = 1;
                }
                //interrupt();
                //sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("线程退出?");
            //System.out.println("Thread Wrong!");
            //e.printStackTrace();
        }
    }
}