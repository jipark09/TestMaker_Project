import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Test implements Serializable {
    private static final long serialVersionUID = -8124312954461054842L;

    private String name;
    private String group = "";
    private List<Subject> subjects;
    private int timeOut;
    private String lastEditDate;
    private String state = "출제중";
    private Calendar today = Calendar.getInstance();

    public Test(String name, List<Subject> subjects, int timeOut, String group) {
        this.name = name;
        this.subjects = subjects;
        this.timeOut = timeOut * 60;
        setGroup(group);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        lastEditDate = dateFormat.format(today.getTime());
    }
    public Test(String name) {
        setName(name);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Subject> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    public int getTimeOut() {
        return timeOut;
    }
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }
    public String getLastEditDate() {
        return lastEditDate;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
    public Calendar getToday() {
        return today;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    //메소드
    //과목번호넣으면 퀴즈 리스트 가지고 오게
    public List<Quiz> getQuizzes(int subjectIdx) {
        return getSubjects().get(subjectIdx).getQuizzes();
    }

    @Override
    public String toString() {
        //문항 수
        int quizSize = 0;
        for(Subject s : subjects) {
            quizSize += s.getQuizzes().size();
        }

        String info = "시험이름 : " + name;
        info += "\n시험 분류: " + group;
        info += "\n과목 수 : " + subjects.size();
        info += "\n문항 수 : " + quizSize;
        info += "\n제한시간 : " + (timeOut / 60) + " 분";
        info += "\n최종 수정 일시 : " + lastEditDate + "\n";
        return info;
    }
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Test)) {
            return false;
        }
        Test temp = (Test)o;

        return name.equals(temp.getName());
    }
}