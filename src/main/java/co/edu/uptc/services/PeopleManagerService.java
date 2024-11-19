package co.edu.uptc.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.edu.uptc.models.PersonModel;
import co.edu.uptc.models.PersonModel.Genders;

@Service
public class PeopleManagerService {

    private final MyProperties myProperties;
    @Autowired
    public PeopleManagerService(@Qualifier("myProperties")  MyProperties myProperties) {
        this.myProperties = myProperties;
    }

    private ArrayList<PersonModel> people;
    private Long ids=3L;

    public ArrayList<PersonModel> showPeople(){
        try {
            people = new ArrayList<>();
            RandomAccessFile file;
            file = new RandomAccessFile(myProperties.getPath(), "r");
            int p = 0;
            String person;
            while ((person = file.readLine())!=null) {
                System.out.println(person); 
                PersonModel create = createPerson(person);
                if (create.isIsvisible()==true){
                    people.add(create);
                    }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } 
       return people; 
    }
    public void addPerson(PersonModel person){
        person.setId(ids);
        person.setIsvisible(true);
        ids++;
        try {
            RandomAccessFile file = new RandomAccessFile(myProperties.getPath(), "rw");
            file.seek(file.length());
            file.writeBytes(writePerson(person));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } 
        //people.add(person);
    }

   public PersonModel deletePerson(Long id){
    PersonModel auxPersonModel=getPerson(id); 
    try {
        auxPersonModel.setIsvisible(false);
        int p = 0;
        p=(int) ((id*82));
        RandomAccessFile file = new RandomAccessFile(myProperties.getPath(), "rw");
        file.seek(p);
        file.writeBytes(writePerson(auxPersonModel));
    }catch (FileNotFoundException e) {
        e.printStackTrace();
    }catch (IOException e) {
        e.printStackTrace();
    }
    return auxPersonModel;
   }

   public PersonModel getPerson(Long id){ 
    PersonModel auxPersonModel=null;
    try {
        RandomAccessFile file = new RandomAccessFile(myProperties.getPath(), "r"); 
        int p = 0;
        p=(int) ((id*81));
        file.seek(p); 
        System.out.println(p);
        String person=file.readLine(); 
        System.out.println(person);
        if (person != null) {
                auxPersonModel=createPerson(person);
        }
        file.close();
     }catch (FileNotFoundException e) {
        e.printStackTrace();
    }catch (IOException e) {
        e.printStackTrace();
    } 
   return auxPersonModel;
}
private String writePerson(PersonModel person) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-10d", person.getId()))
      .append(String.format("%-20s", person.getName()))
      .append(String.format("%-20s", person.getLastName()))
      .append(String.format("%-10s", person.getBirthday().toString()))
      .append(String.format("%-10s", person.getGender().toString()))
      .append(String.format("%-10s", person.isIsvisible()))
      .append("\n");
    return sb.toString();
}
private PersonModel createPerson(String stringPerson) {
    PersonModel person = new PersonModel();
    person.setId(Long.parseLong(stringPerson.substring(0, 10).trim()));
    person.setName(stringPerson.substring(10, 30).trim());
    person.setLastName(stringPerson.substring(30, 50).trim());
    person.setBirthday(LocalDate.parse(stringPerson.substring(50, 60).trim()));
    person.setGender(PersonModel.Genders.valueOf(stringPerson.substring(60, 70).trim().toUpperCase()));
    person.setIsvisible(Boolean.parseBoolean(stringPerson.substring(70, 80).trim()));
    return person;
}  
}
