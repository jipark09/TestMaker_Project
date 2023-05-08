import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Subject implements Serializable {
    private static final long serialVersionUID = -8124312954461054842L;

    private String name;
    private List<Quiz> quizzes = new LinkedList<>();

    public Subject(String name, int num, int startNum) {
        setName(name);

        for(int i=startNum; i<startNum+num; i++) {
            quizzes.add(new Quiz(i));
        }

    }
    public Subject(String name, List<Quiz> quizzes) {
        super();
        this.name = name;
        this.quizzes = quizzes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Quiz> getQuizzes() {
        return quizzes;
    }
    public void setQuizzes(List<Quiz> subjects) {
        this.quizzes = subjects;
    }

    @Override
    public String toString() {
        return "Subject [name=" + name + ", subjects=" + quizzes.size() + "]";
    }


}