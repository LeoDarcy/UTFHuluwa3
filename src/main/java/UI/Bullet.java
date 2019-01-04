package UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class Bullet {
    public boolean ok;
    final static int OffX = 100;
    final static int OffY = 100;
    final static int EachWidth = 50;
    final static int EachHeight = 50;
    private int x;
    private int y;
    private int tox;
    private int toy;
    private boolean turnx;
    private Timeline timeline;
    private String image;
    private DoubleProperty off = new SimpleDoubleProperty();

    public Bullet(int fromx, int fromy, int dx, int dy) {
        x = fromx;
        y = fromy;
        tox = dx;
        toy = dy;
        if (dx != fromx) {
            turnx = true;
            if (dx > fromx) {
                image = "/bulletdown.png";
                timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(off, 0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(off, 50)
                        )
                );
            }
            else{
                image = "/bulletup.png";
                timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(off, 0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(off, -50)
                        )
                );
            }
        }
        else{
            turnx = false;
            if (dy > fromy) {
                image = "/bulletright.png";
                timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(off, 0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(off, 50)
                        )
                );
            }
            else{
                image = "/bulletleft.png";
                timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(off, 0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(off, -50)
                        )
                );
            }
        }
        timeline.setAutoReverse(false);
        timeline.play();
        //timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public int Getx() {
        if(turnx)
            return x*EachHeight + OffX + (off.getValue().intValue());
        else
            return x*EachHeight + OffX;
    }

    public int Gety() {
        if(turnx)
            return y*EachWidth + OffY;
        else
            return y*EachWidth + OffY + (off.getValue().intValue());
    }

    public boolean Finished(){
        int tmp = (off.getValue().intValue());
        return (50 == tmp || -50 == tmp);
    }
    public String GetImage(){
        return image;
    }
}
