package MyThread;

import BattleField.BattleField;
import UI.MainCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.BufferedReader;

//负责处理收到的信息。用一个stack信息储存，把信息收集到文件中
public class DocThread {//extends Thread
/*    private BattleField battleField;
    private Text clock;
    private GraphicsContext gContext;
    private MainCanvas mainCanvas;
    private BufferedReader reader;
    public DocThread(BattleField b, Text cl, MainCanvas c, GraphicsContext g, BufferedReader bf) {
        //super();
        battleField = b;
        clock = cl;
        mainCanvas =c;
        gContext = g;
        reader = bf;
    }
    @Override
    public void run() {
        try {
            boolean flag = false;
            while (flag == true) {
                sleep(1550);
                gContext.drawImage(battleField.getImage(), 0, 0);
                String num;
                if ((num = reader.readLine()) != null) {
                    while (!num.equals("time")) {
                        int posx = Integer.parseInt(num);
                        num = reader.readLine();
                        int posy = Integer.parseInt(num);
                        String im = reader.readLine();
                        gContext.drawImage(new Image(im), mainCanvas.TranslateYPosition(posy), mainCanvas.TranslateXPosition(posx));
                        num = reader.readLine();
                    }
                    String time = reader.readLine();
                    if (time.equals("Victory")) {
                        String victor = reader.readLine();
                        clock.setText(clock.getText() + victor);
                        flag = false;
                        reader.close();
                    } else {
                        clock.setText(time);
                        String end = reader.readLine();
                        if (end.equals("End")) {
                            clock.setText(clock.getText() + reader.readLine());
                            flag = false;
                            reader.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("读取出错！");
            assert (true);
            e.printStackTrace();
        }
    }
*/
}
