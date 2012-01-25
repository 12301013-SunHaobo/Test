package others.e.sentence;

public class Item implements Comparable{

    private String word;
    private String meaning;
    private String sentence;
    
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getMeaning() {
        return meaning;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    public String getSentence() {
        return sentence;
    }
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
    }
    @Override
    public String toString() {
        //return word + "; " + meaning + "; " + sentence;
        return word;
    }
    @Override
    public int compareTo(Object o) {
        if(this.word==null || o==null){
            return 0;
        }
        return this.word.compareTo(((Item)o).getWord());
    }

    
    
}
