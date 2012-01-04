package others.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TestSortedFieldAnno {


    /**
     * @param args
     */
    public static void main(String[] args) {

        //testGetSortedFieldNames();
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

    

    private static void printErrors(List<String> errors) {
        for(String error : errors){
            System.out.println(error);
        }
    }



}      
