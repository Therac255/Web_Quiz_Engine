package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Service
public class TaskController {
    private ArrayList<Task> tasks = new ArrayList<>();
    public TaskController() {
    }

    @GetMapping(path = "/test")
    public String testIfApi() {
        return "OK";
    }

    @PostMapping(path = "/tasks")
    public void addTask(@RequestBody Task task){
        tasks.add(task);
    }

    @PostMapping(path = "/api/quiz")
    public srvResponse check(@RequestBody String request) {
        if (request.equals ("answer=2")) return new srvResponse(true);
        else return new srvResponse(false);
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    //@Autowired
    public Question add(@RequestBody String request) {
        try {
            //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            ObjectMapper objectMapper = new ObjectMapper();
            Question question = objectMapper.readValue(request, Question.class);
            //QuestionStore questionStore =  context.getBean(QuestionStore);
            QuestionStore.addQuestion(question);
            return question;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "/api/quiz")
    public Question giveQuiz() {
        return new Question ("The Java Logo", "What is depicted on the Java logo?", new String[]{"Robot", "Tea leaf", "Cup of coffee", "Bug"}, 2);
    }

    @GetMapping(value = "/api/quizzes")
    public Question get(@RequestBody int id) {
        //String serialized;
        ObjectMapper objectMapper = new ObjectMapper();
        Question question = QuestionStore.getQuestion(id);
        //objectMapper.writeValue(request, Question.class);
        return question;
    }

}
