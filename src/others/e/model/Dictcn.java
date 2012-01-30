package others.e.model;

import java.io.IOException;

import utils.FileUtil;
/**
 * 
 * haven't found a way to download
 *
 */
public class Dictcn {
    //instance members
    private String meaning;
    public final static String NOT_FOUND="NotFound";
    
    private String enIPA;//International Phonetic Alphabet
    private String enMp3Url;
    private boolean enHasMp3;//iciba site
    
    private String usIPA;
    private String usMp3url;
    private boolean usHasMp3;//iciba site

    //indicate if local has mp3
    private boolean localEnHasMp3 = true;
    private boolean localUsHasMp3 = true;
    
    
    //testing
    public static void main(String [] args) throws IOException{
        System.out.println("aaa");
    }
    
    
    public static String extractICIBAMeaning(String icibaContent) {
        return null;
    }
}
