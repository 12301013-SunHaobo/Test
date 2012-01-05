package others.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigRangeContainer<T> {

    private boolean valid = false;
    private List<String> errors = null;
    
    private List<T> configList = new ArrayList<T>();//all config instances
    
    public ConfigRangeContainer(Class<T> clazz) {
        super();
        this.errors = validateConfig(clazz);
        if(errors.size()==0){
            this.valid = true;
        }
    }

    private void populateConfigs(Field f, List<T> configs){
        
    }
    
    private List<String> validateConfig(Class<T> testClass){
        List<String> errorList = new ArrayList<String>();
        Field[] field = testClass.getFields();
        for(int i=0;i<field.length;i++){
            Field f = field[i];
            String name = f.getName();
            IntRange intRange = f.getAnnotation(IntRange.class);
            DoubleRange doubleRange = f.getAnnotation(DoubleRange.class);

            //only one range is allowed
            if(intRange!=null && doubleRange!=null){
                errorList.add("["+name+"] has both IntRange and DoubleRange, only one is allowed.");
                continue;
            }
            //range must match data type
            if (Double.TYPE.equals(f.getType()) && intRange!=null){
                errorList.add("["+name+"] double type has IntRange.");
                continue;
            } 
            if (Integer.TYPE.equals(f.getType()) && doubleRange!=null){
                errorList.add("["+name+"] int type has DoubleRange.");
                continue;
            }
            //range interval is reasonable, 
            if (Integer.TYPE.equals(f.getType())){
                int start = intRange.start();
                int end = intRange.end();
                int intervals = intRange.intervals();
                if (end<start) {
                    errorList.add("["+name+"] end<start.");
                    continue;
                }
                if ((end-start)<intervals) {
                    errorList.add("["+name+"] unreasonable intervals.");
                    continue;
                }
            }
        }
        return errorList;
    }



    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    private void printErrors(List<String> errors) {
        for(String error : errors){
            System.out.println(error);
        }
    }    
    
    
    private static class ConfigField {
        
    }
}





@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface IntRange {
    int start();
    int end();
    int intervals();
    String description() default "";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface DoubleRange {
    double start();
    double end();
    int intervals();
    String description() default "";
}