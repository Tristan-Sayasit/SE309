package coms309;

import datamethods.DbMethods;
import datamethods.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {
    @RequestMapping(
            value = "/getPersons",
            produces = "application/json",
            method = {RequestMethod.GET}
    )
    public ResponseEntity<List<Person>> GetPersons(){
        DbMethods db = new DbMethods();
        List<Person> ret;
        try {
           ret = db.GetAllPersons();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/insertPerson",
            method = {RequestMethod.POST}
    )
    public ResponseEntity<Person> InsertPerson(@RequestBody Person newPerson){
        DbMethods db = new DbMethods();

        if(newPerson.Person_Id == null){
            newPerson.Person_Id = UUID.randomUUID().toString();
        }
        if(newPerson.First_Name == null || newPerson.Last_Name == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            db.InsertNewPerson(newPerson);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newPerson, HttpStatus.OK);
    }
}
