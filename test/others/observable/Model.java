package others.observable;

import java.util.Observable;

public class Model extends Observable{
    
    double price;
    
    public void addPrice(double price){
        this.price = price;
        
        //Notify observers of change
        setChanged();
        notifyObservers(price);
    }
    
}
