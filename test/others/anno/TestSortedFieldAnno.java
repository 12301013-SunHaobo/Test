package others.anno;

import java.util.ArrayList;
import java.util.List;

public class TestSortedFieldAnno {


    /**
     * @param args
     */
    public static void main(String[] args) {
        testComb();
        //testGetSortedFieldNames();
    }
    

    //[numberOfCombinations][numberOfElementsInOneComb]
    private static List<List<Object>> combs = new ArrayList<List<Object>>();
    
    /**
     * 
     * @param combs  should initialize as new List<empty list> for the first set
     * @param newSet
     * @return
     */
    private static List<List<Object>> addNewSet(List<List<Object>> combs, List<Object> newSet){
        List<List<Object>> newCombs = new ArrayList<List<Object>>();
        for(Object obj : newSet){
            for(List<Object> comb : combs){
                List<Object> tmpList = new ArrayList<Object>(comb);
                tmpList.add(obj);
                newCombs.add(tmpList);
            }
        }
        return newCombs;
    }
    
    private static void testComb(){
        //all sets for combination
        List<List<Object>> allSets = new ArrayList<List<Object>>();
        List<Object> intList = new ArrayList<Object>();
        intList.add(1);
        intList.add(2);
        intList.add(3);

        List<Object> intList1 = new ArrayList<Object>();
        intList1.add(1.1);
        intList1.add(2.2);
        intList1.add(3.3);

        List<Object> intList2 = new ArrayList<Object>();
        intList2.add(100);
        intList2.add(200);
        intList2.add(300);
        
        allSets.add(intList);
        allSets.add(intList1);
        allSets.add(intList2);

        //create combinations
        List<List<Object>> resultSet = new ArrayList<List<Object>>();
        resultSet.add(new ArrayList<Object>());
        
        for(List<Object> oneSet : allSets) {
            resultSet = addNewSet(resultSet, oneSet);
        }
        
        //print out 
        for(List<Object> combs : resultSet){
            System.out.println();
            for(Object element : combs){
                System.out.print(element+",");
            }
        }
        
    }



    /*
    private static void testGetSortedFieldNames(){
        List<String> errorList = validateConfig();
        if(errorList.size()>0){
            printErrors(errorList);
            return;
        }
        @SuppressWarnings("rawtypes")
        Class testClass = ConfigRangeContainer.class;
        Field[] field = testClass.getFields();
        for(int i=0;i<field.length;i++){
            Field f = field[i];
            IntRange r = f.getAnnotation(IntRange.class);
            if(r!=null){
                System.out.println(field[i]+";"+r.start()+","+r.end());
            } else {
                System.out.println(field[i]+"; no range");
            }
        }
        
    }
    */

}      
