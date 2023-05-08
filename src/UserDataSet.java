import java.util.ArrayList;

public class UserDataSet {
    private ArrayList<User> users;

    public UserDataSet() {
        users = new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    // 회원 추가
    public void addUsers(User user) {
        users.add(user);
    }
    // 아이디 중복 확인
    public boolean isIdOverlap(String id) {
        return users.contains(new User(id));
    }
    // 회원 삭제
    public void withdraw(String id) {
        users.remove(getUser(id));
    }
    // 유저 정보 가져오기
    public User getUser(String id) {
        return users.get(users.indexOf(new User(id)));
    }
    // 회원인지 아닌지 확인
    public boolean contains(User user) {
        return users.contains(user);
    }

}