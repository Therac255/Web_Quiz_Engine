package engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component

public class QuestionStore {

    public static ArrayList<Question> questions;

    public QuestionStore(ArrayList<Question> questions) {
        this.questions = new ArrayList<>();
    }

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public static void setQuestions(ArrayList<Question> questions) {
        questions = questions;
    }

    //@Bean
    public static Question getQuestion(int id) {
        return questions.get(id);
    }

    //@Bean
    public static void addQuestion(Question question) {
        questions.add(question);
    }
}
