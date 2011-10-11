package others;

import others.json.Person;
import others.json.Pet;
import flexjson.JSONSerializer;

public class TestJavaToJson {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Pet pet = new Pet();
        pet.setPetName("pet name 001");
        
        Person person = new Person();
        person.setPet(pet);
        
        JSONSerializer serializer = new JSONSerializer();
        System.out.println(serializer.serialize(person));
        

    }
    
    
    

}
