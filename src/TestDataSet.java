import java.io.Serializable;
import java.util.ArrayList;

public class TestDataSet implements Serializable {
    private ArrayList<Test> tests;
    public TestDataSet() {
        tests = new ArrayList<Test>();
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

    // Test 추가
    public void addTest(Test test) {

        test.setTimeOut(test.getTimeOut() / 60);
        tests.add(test);
    }

    public void setTest(Test test) {
        getTest(test.getName()).setSubjects(test.getSubjects());
        getTest(test.getName()).setTimeOut(test.getTimeOut());
    }

    // Test 삭제
    public void withdraw(String name) {
        tests.remove(getTest(name));
    }

    // Test 정보 가져오기
    public Test getTest(String name) {
        return tests.get(tests.indexOf(new Test(name)));
    }

    // Test가 있는지 없는지 확인
    public boolean containsTest(Test test) {
        return tests.contains(test);
    }

    @Override
    public String toString() {
        return ": " + tests;
    }
}

