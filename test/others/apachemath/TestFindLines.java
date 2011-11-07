package others.apachemath;

public class TestFindLines {

    //{t,p}
    private static double tp[][] = new double[][]{
        {0, 50},
        {1, 51},
        {2, 52},
        {3, 53},
        {4, 54},
        {5, 55},
        {6, 56},
        {7, 57},
        {8, 58},
        {9, 59},
        {10, 60},
    };
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {


    }

    
    public static void findIntervals(){
        
    }
    
    public static void testPrintOut(){
        System.out.println(tp.length);
        for(int i=0;i<tp.length;i++){
            System.out.println(tp[i][0]+","+tp[i][1]);
        }
    }
    
}
