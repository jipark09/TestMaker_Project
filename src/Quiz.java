import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Quiz implements Serializable {
    private static final long serialVersionUID = -8124312954461054842L;

    private int num;
    private int point; // 배점
    private String title;
    private List<String> following;	//문제지문
    private List<String> texts;	//선지지문
    private List<Integer> answers;
    private String explain;
    private List<String> hashTag;

    public Quiz(int num) {
        setNum(num);
    }

    public Quiz(int num, int point, String title, List<String> following, List<String> texts, List<Integer> answers, String explain, List<String> hashTag) {
        this.num = num;
        this.point = point;
        this.title = title;
        this.following = following;
        this.texts = texts;
        this.answers = answers;
        this.explain = explain;
        this.hashTag = hashTag;
    }

    // getter/setter
    public int getPoint() {
        return point;
    }
    public void setPoint(int point) {
        this.point = point;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getFollowing() {
        return following;
    }
    public void setFollowing(List<String> following) {
        this.following = following;
    }
    public List<String> getTexts() {
        return texts;
    }
    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
    public List<Integer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
    public String getExplain() {
        return explain;
    }
    public void setExplain(String explain) {
        this.explain = explain;
    }
    public List<String> getHashTag() {
        return hashTag;
    }
    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Quiz)) {
            return false;
        }
        Quiz temp = (Quiz) o;

        return num == temp.num;
    }

    @Override
    public String toString() {
        return "Quiz [num=" + num + ", point=" + point + ", title=" + title + ", following=" + following + ", texts="
                + texts + ", answers=" + answers + ", explain=" + explain + ", hashTag=" + hashTag + "]";
    }


}