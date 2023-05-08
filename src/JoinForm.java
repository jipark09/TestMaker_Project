import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinForm extends JDialog {
    private LoginForm owner;
    private UserDataSet users;
    private TestDataSet testList;

    private JLabel lblId;
    private JLabel lblPw;
    private JLabel lblRe;
    private JLabel lblName;
    private JLabel lblNickName;
    private JLabel lblquestion;
    private JLabel lblAnswer;
    private JTextField tfId;
    private JPasswordField pfPw;
    private JPasswordField pfRe;
    private JTextField tfName;
    private JTextField tfNickName;
    private JTextField tfAnswer;
    private String[] arrQuestion;
    private JComboBox<String> comQuestion;
    private JButton btnJoin;
    private JButton btnCancel;

    public JoinForm(LoginForm owner) {
        super(owner, "Join", true);
        this.owner = owner;
        users = owner.getUsers();

        init();
        setDisplay();
        addListeners();
        showFrame();
    }
    private void init() {
        // 크기 고정
        int tfSize = 10;
        Dimension lblSize = new Dimension(80, 35);
        Dimension btnSize = new Dimension(100 ,25);

        lblId = new JLabel("아이디", JLabel.LEFT);
        lblId.setPreferredSize(lblSize);
        lblPw = new JLabel("비밀번호", JLabel.LEFT);
        lblPw.setPreferredSize(lblSize);
        lblRe = new JLabel("재확인", JLabel.LEFT);
        lblRe.setPreferredSize(lblSize);
        lblName = new JLabel("이름", JLabel.LEFT);
        lblName.setPreferredSize(lblSize);
        lblNickName = new JLabel("닉네임", JLabel.LEFT);
        lblNickName.setPreferredSize(lblSize);

        tfId = new JTextField(tfSize);
        pfPw = new JPasswordField(tfSize);
        pfRe = new JPasswordField(tfSize);
        tfName = new JTextField(tfSize);
        tfNickName = new JTextField(tfSize);

        arrQuestion = new String[]{"내 고향은?", "나의 보물 1호는?", "나의 초등학교는?"};
        lblquestion = new JLabel("질문", JLabel.LEFT);
        lblquestion.setPreferredSize(lblSize);
        comQuestion = new JComboBox<>(arrQuestion);
        lblAnswer = new JLabel("답", JLabel.LEFT);
        lblAnswer.setPreferredSize(lblSize);
        tfAnswer = new JTextField(tfSize);

        btnJoin = new JButton("가입하기");
        btnJoin.setPreferredSize(btnSize);
        btnCancel = new JButton("취소");
        btnCancel.setPreferredSize(btnSize);

    }
    private void setDisplay() {
        // FlowLayout 왼쪽 정렬
        FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

        // pnlMain(pnlMNorth / pnlMCenter / pnlMSouth)
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        // pnlMCenter(pnlId / pnlPw / pnlRe / pnlName / pnlNickName)
        JPanel pnlMCenter = new JPanel(new GridLayout(0, 1));
        pnlMCenter.setBackground(Color.WHITE);
        JPanel pnlId = new JPanel(flowLeft);
        pnlId.add(lblId);
        pnlId.add(tfId);
        pnlId.setBackground(Color.WHITE);

        JPanel pnlPw = new JPanel(flowLeft);
        pnlPw.add(lblPw);
        pnlPw.add(pfPw);
        pnlPw.setBackground(Color.WHITE);

        JPanel pnlRe = new JPanel(flowLeft);
        pnlRe.add(lblRe);
        pnlRe.add(pfRe);
        pnlRe.setBackground(Color.WHITE);

        JPanel pnlName = new JPanel(flowLeft);
        pnlName.add(lblName);
        pnlName.add(tfName);
        pnlName.setBackground(Color.WHITE);

        JPanel pnlNickName = new JPanel(flowLeft);
        pnlNickName.add(lblNickName);
        pnlNickName.add(tfNickName);
        pnlNickName.setBackground(Color.WHITE);

        pnlMCenter.add(pnlId);
        pnlMCenter.add(pnlPw);
        pnlMCenter.add(pnlRe);
        pnlMCenter.add(pnlName);
        pnlMCenter.add(pnlNickName);
        pnlMCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        // pnlMSouth(rbtnMale / rbtnFemale)
        JPanel pnlMSouth = new JPanel(new GridLayout(0, 1));
        pnlMSouth.setBackground(Color.WHITE);

        JPanel pnlQues = new JPanel(flowLeft);
        pnlQues.add(lblquestion);
        pnlQues.add(comQuestion);
        pnlQues.setBackground(Color.WHITE);

        JPanel pnlAnswer = new JPanel(flowLeft);
        pnlAnswer.add(lblAnswer);
        pnlAnswer.add(tfAnswer);
        pnlAnswer.setBackground(Color.WHITE);

        pnlMSouth.add(pnlQues);
        pnlMSouth.add(pnlAnswer);

        pnlMSouth.setBorder(new TitledBorder("본인확인"));

        // pnlMain
        pnlMain.add(pnlMCenter, BorderLayout.CENTER);
        pnlMain.add(pnlMSouth, BorderLayout.SOUTH);

        // pnlSouth(btnJoin / btnCancel)
        JPanel pnlSouth = new JPanel();
        pnlSouth.add(btnJoin);
        pnlSouth.add(btnCancel);
        pnlSouth.setBackground(Color.WHITE);

        // 화면 테두리의 간격을 주기 위해 설정 (insets 사용 가능)
        pnlMain.setBorder(new EmptyBorder(0, 20, 0, 20));
        pnlSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

        add(pnlMain, BorderLayout.NORTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }
    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int choice = JOptionPane.showConfirmDialog(
                        JoinForm.this,
                        "회원가입을 종료합니다.",
                        "종료",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    owner.setVisible(true);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int choice = JOptionPane.showConfirmDialog(
                        JoinForm.this,
                        "회원가입을 종료합니다.",
                        "종료",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    owner.setVisible(true);
                }
            }
        });
        btnJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // 정보 하나라도 비어있으면
                if(isBlank()) {
                    JOptionPane.showMessageDialog(
                            JoinForm.this,
                            "모든 정보를 입력해주세요."
                    );
                    // 모두 입력했을 때
                } else {
                    // Id 중복일 때
                    if(users.isIdOverlap(tfId.getText())) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "이미 존재하는 Id입니다."
                        );
                        tfId.requestFocus();

                        // Pw와 Re가 일치하지 않았을 때
                    } else if(!String.valueOf(pfPw.getPassword()).equals(String.valueOf(pfRe.getPassword()))) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "Password와 Retry가 일치하지 않습니다."
                        );
                        pfPw.requestFocus();

                    }
                    else {
                        users.addUsers(new User(
                                        tfId.getText(),
                                        String.valueOf(pfPw.getPassword()),
                                        tfName.getText(),
                                        tfNickName.getText(),
                                        comQuestion.getSelectedItem().toString(),
                                        tfAnswer.getText(),
                                        new TestDataSet(),
                                        new HashList()
                                )
                        );
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "회원가입을 완료했습니다!"
                        );
                        dispose();
                        owner.setVisible(true);
                    }
                }
            }
        });
    }
    public boolean isBlank() {
        boolean result = false;
        if(tfId.getText().isEmpty()) {
            tfId.requestFocus();
            return true;
        }
        if(String.valueOf(pfPw.getPassword()).isEmpty()) {
            pfPw.requestFocus();
            return true;
        }
        if(String.valueOf(pfRe.getPassword()).isEmpty()) {
            pfRe.requestFocus();
            return true;
        }
        if(tfName.getText().isEmpty()) {
            tfName.requestFocus();
            return true;
        }
        if(tfNickName.getText().isEmpty()) {
            tfNickName.requestFocus();
            return true;
        }
        return result;
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}