package others.observable;

public class TestObservable {

    /**
     * @param args
     */
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model();
        model.addObserver(view);
        
        model.addPrice(new Double(10.01));

    }

}
