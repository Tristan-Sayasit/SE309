package coms309;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/new")
public class NewController {
    @GetMapping("/")
    public String welcome() {
        return "Demo test text, what up";
    }

    @RequestMapping(
            value = "/testpost",
            produces = "application/json",
            method = {RequestMethod.POST}
    )
    public ResponseEntity<String> TestPost(@RequestBody String name){
        if(name.equalsIgnoreCase("Crystal Palace")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>("Hello "+name, HttpStatus.ACCEPTED);
        }
    }
}
