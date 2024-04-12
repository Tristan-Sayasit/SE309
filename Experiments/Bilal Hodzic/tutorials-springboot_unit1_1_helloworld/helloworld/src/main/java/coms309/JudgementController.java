package coms309;

import datamethods.DbMethods;
import datamethods.Person;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/judgement")
class JudgementController {
    @GetMapping("/soccer")
    public String soccer(){
        return "Do you like soccer?";
    }
    @GetMapping("/soccer/{likes}")
    public String soccer(@PathVariable String likes){
        DbMethods db = new DbMethods();
        try {
            db.GetAllPersons();
        }catch (Exception e){
            System.out.println("Error with DB query");
        }
        if(likes.equalsIgnoreCase("yes")){
            return "Good choice";
        }else{
            return "Poor choice";
        }
    }
}
