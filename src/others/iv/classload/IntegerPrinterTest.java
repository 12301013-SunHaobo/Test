package others.iv.classload;


/**
 * static block in others.iv.classload.IntegerPrinter
 * is executed only once
 * @author r
 *
 */
public class IntegerPrinterTest {
    /**
     * This main method shows a use of our CustomClassLoader for
     * loading some class and running it. All the objects referenced
     * from the IntegerPrinter class will be loaded with
     * our CustomClassLoader.
     */
    public static void main(String[] args) throws Exception {
        CustomClassLoader loader = new CustomClassLoader(IntegerPrinterTest.class.getClassLoader());
        Class<?> clazz = loader.loadClass("others.iv.classload.IntegerPrinter");
        Object instance = clazz.newInstance();
        clazz.getMethod("runMe").invoke(instance);
        
        CustomClassLoader loader1 = new CustomClassLoader(IntegerPrinterTest.class.getClassLoader());
        Class<?> clazz1 = loader1.loadClass("others.iv.classload.IntegerPrinter");
        Object instance1 = clazz1.newInstance();
        clazz1.getMethod("runMe").invoke(instance1);
        
    }
}
