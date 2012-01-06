package others.anno;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import modules.at.model.DoubleRange;
import modules.at.model.IntRange;
import utils.Formatter;

public class ConfigRangeContainer<T> {

    private boolean valid = false;
    
    private List<Field> fields = new ArrayList<Field>(); //all range fields
    private List<T> configList = new ArrayList<T>();//all config instances
    
    public ConfigRangeContainer(Class<T> clazz) {
        super();
        //validation
        List<String> errors = validateConfig(clazz);
        if(errors.size()==0){
            this.valid = true;
        } else {
            printErrors(errors);
            System.exit(-1); 
        }
        //create config instances list
        try {
            populateFieldsAndConfigInstances(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
    }
    
    private void populateFieldsAndConfigInstances(Class<T> clazz) throws Exception{
        //create initial empty combinations
        List<List<Number>> combSet = new ArrayList<List<Number>>();
        combSet.add(new ArrayList<Number>());
        Field[] field = clazz.getFields();
        for(int i=0;i<field.length;i++){
            Field f = field[i];
            IntRange intRange = f.getAnnotation(IntRange.class);
            DoubleRange doubleRange = f.getAnnotation(DoubleRange.class);
            if((doubleRange!=null && Double.TYPE.equals(f.getType())) 
                    || (intRange!=null & Integer.TYPE.equals(f.getType()))) {
                combSet = addNewFieldRangeValues(combSet, f);
                fields.add(f);
            }
        }
        //populate config instances
        for(List<Number> comb : combSet) {
            T c = clazz.newInstance();
            for(int i=0; i<fields.size();i++){
                Field f = fields.get(i);
                Number n = comb.get(i);
                f.set(c, n);
            }
            configList.add(c);
        }
    }
    
    
    /**
     * @param combs  should initialize as new List<empty list> for the first set
     * @param newSet
     * @return List< CombinationsList<elementObject>>
     */
    private List<List<Number>> addNewFieldRangeValues(List<List<Number>> combs, Field f){
        List<List<Number>> newCombs = new ArrayList<List<Number>>();
        //order of the nested loop affect order of output, but either way works.
        for(List<Number> comb : combs){
            
            if (Double.TYPE.equals(f.getType())) {
                DoubleRange doubleRange = f.getAnnotation(DoubleRange.class);
                double start = doubleRange.start();
                double end = doubleRange.end();
                int intervals = doubleRange.intervals();
                double increment = (end - start)/intervals;
                
                for(double d = start; d<=end; d+= increment){
                    List<Number> tmpList = new ArrayList<Number>(comb);
                    tmpList.add(d);
                    newCombs.add(tmpList);
                }
            } else if (Integer.TYPE.equals(f.getType())) {
                IntRange doubleRange = f.getAnnotation(IntRange.class);
                int start = doubleRange.start();
                int end = doubleRange.end();
                int intervals = doubleRange.intervals();
                int increment = (end - start)/intervals; //int, so modulus
                
                for(int d = start; d<=end; d+= increment){
                    List<Number> tmpList = new ArrayList<Number>(comb);
                    tmpList.add(d);
                    newCombs.add(tmpList);
                } 
                if((start+intervals*increment)<end) {
                    List<Number> tmpList = new ArrayList<Number>(comb);
                    tmpList.add(end);
                    newCombs.add(tmpList);
                }
            } 
        }
        return newCombs;
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
            if (intRange != null && Integer.TYPE.equals(f.getType())){
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
            //range filed cannot be final
            if(intRange != null || doubleRange != null){
                int foundMods = f.getModifiers();
                if((foundMods & Modifier.FINAL) == Modifier.FINAL
                        || (foundMods & Modifier.STATIC) == Modifier.STATIC) {
                    errorList.add("["+name+"] range field shouldn't be static or final.");
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

    private void printErrors(List<String> errors) {
        for(String error : errors){
            System.out.println(error);
        }
    }
    
    public void printConfigs() {
        try {
            System.out.println();
            for(T t : configList){
                for(Field f : fields) {
                    if(Double.TYPE.equals(f.getType())){
                        double d = (Double)f.get(t);
                        System.out.print(f.getName()+"="+Formatter.DECIMAL_FORMAT.format(d)+", ");
                    } else if (Integer.TYPE.equals(f.getType())){
                        int i = (Integer)f.get(t);
                        System.out.print(f.getName()+"="+i+", ");
                    }
                }
                System.out.println();
            }
            System.out.println("Total instances = "+ configList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}






