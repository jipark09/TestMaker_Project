import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class User implements Serializable {
    private String id;
    private String pw;
    private String name;
    private String nickName;
    private String question;
    private String answer;
    private TestDataSet testList;
    private HashList hashList;

    public User(String id, String pw, String name, String nickName, String question, String answer, TestDataSet testList, HashList hashList) {
        setId(id);
        setPw(pw);
        setName(name);
        setNickName(nickName);
        setQuestion(question);
        setAnswer(answer);
        setTestList(testList);
        setHashList(hashList);
    }

    public User(String id) {
        setId(id);
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getName() {
        return name;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public String getNickName() {
        return nickName;
    }
    public TestDataSet getTestList() {
        return testList;
    }
    public void setTestList(TestDataSet testList) {
        this.testList = testList;
    }
    public void setHashList(HashList hashList) {
        this.hashList = hashList;
    }
    public ArrayList<Hash> getHashList() {
        return hashList.getHashList();
    }

    public void write(Test test, File file) {

        FileOutputStream fos = null;    // 어디에 쓰는가?
        ObjectOutputStream oos = null;    // 오브젝트를 쓴다.

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(test);

            System.out.println(test);

            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeAll(oos, fos);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof User)) {
            return false;
        }
        User temp = (User)o;

        return id.equals(temp.getId());
    }

    @Override
    public String toString() {
        String info = "아이디: " + id + "\n";
        info += "비밀번호: " + pw + "\n";
        info += "이름: " + name + "\n";
        info += "닉네임: " + nickName + "\n";
        info += "질문: " + question + "\n";
        info += "답: " + answer + "\n";
        //info += "내 문제집" + testList;
        return info;
    }
}