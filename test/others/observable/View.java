package others.observable;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("View.update() called: update(" + o + "," + arg + ");");
        
    }

}
