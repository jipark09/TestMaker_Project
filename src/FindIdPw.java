import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class FindIdPw extends JDialog {
    private JLabel lblName;
    private JLabel lblNickname;
    private JLabel lblQuestion;
    private JLabel lblAnswer;
    private JTextField tfName;
    private JTextField tfNickname;
    private String[] arrQuestion;
    private JComboBox<String> comQuestion;
    private JTextField tfAnswer;
    private JButton btnOK;

    private JLabel lblID;
    private JLabel lblQuestion2;
    private JLabel lblAnswer2;
    private JTextField tfID;
    private String[] arrQuestion2;
    private JComboBox<String> comQuestion2;
    private JTextField tfAnswer2;
    private JButton btnOK2;

    private UserDataSet users;
    private LoginForm owner;
    boolean findIdCheck = false;

    public FindIdPw(LoginForm owner) {
        this.owner = owner;
        users = owner.getUsers();
        init();
        setDisplay();
        addListener();
        showFrame();
    }

    private void init() {
        int tfSize = 15;
        Dimension lblSize = new Dimension(55, 35);
        Dimension btnSize = new Dimension(95, 30);

        lblName = new JLabel("이름", JLabel.LEFT);
        lblName.setBackground(Color.WHITE);
        lblName.setForeground(Color.GRAY);
        lblName.setPreferredSize(lblSize);

        lblNickname = new JLabel("닉네임", JLabel.LEFT);
        lblNickname.setBackground(Color.WHITE);
        lblNickname.setForeground(Color.GRAY);
        lblNickname.setPreferredSize(lblSize);

        lblQuestion = new JLabel("질문", JLabel.LEFT);
        lblQuestion.setBackground(Color.WHITE);
        lblQuestion.setForeground(Color.GRAY);
        lblQuestion.setPreferredSize(lblSize);

        lblAnswer = new JLabel("답", JLabel.LEFT);
        lblAnswer.setBackground(Color.WHITE);
        lblAnswer.setForeground(Color.GRAY);
        lblAnswer.setPreferredSize(lblSize);

        tfName = new JTextField(tfSize);
        tfNickname = new JTextField(tfSize);
        arrQuestion = new String[] { "내 고향은?", "나의 보물 1호는?", "나의 초등학교는?" };
        comQuestion = new JComboBox<>(arrQuestion);
        comQuestion.setBackground(Color.WHITE);
        comQuestion.setForeground(Color.GRAY);
        comQuestion.setPreferredSize(new Dimension(168,27));

        tfAnswer = new JTextField(tfSize);
        btnOK = new JButton("확인");
        btnOK.setBackground(new Color(157, 195, 230));
        btnOK.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btnOK.setForeground(Color.BLACK);
        btnOK.setBorder(new LineBorder(Color.WHITE));
        btnOK.setPreferredSize(btnSize);
//===================================================================================
        lblID = new JLabel("아이디", JLabel.LEFT);
        lblID.setBackground(Color.WHITE);
        lblID.setForeground(Color.GRAY);
        lblID.setPreferredSize(lblSize);

        lblQuestion2 = new JLabel("질문", JLabel.LEFT);
        lblQuestion2.setBackground(Color.WHITE);
        lblQuestion2.setForeground(Color.GRAY);
        lblQuestion2.setPreferredSize(lblSize);

        lblAnswer2 = new JLabel("답", JLabel.LEFT);
        lblAnswer2.setBackground(Color.WHITE);
        lblAnswer2.setForeground(Color.GRAY);
        lblAnswer2.setPreferredSize(lblSize);

        tfID = new JTextField(tfSize);
        arrQuestion2 = new String[] { "내 고향은?", "나의 보물 1호는?", "나의 초등학교는?" };
        comQuestion2 = new JComboBox<>(arrQuestion2);
        comQuestion2.setBackground(Color.WHITE);
        comQuestion2.setForeground(Color.GRAY);
        comQuestion2.setPreferredSize(new Dimension(168,27));

        tfAnswer2 = new JTextField(tfSize);
        btnOK2 = new JButton("확인");
        btnOK2.setPreferredSize(btnSize);
        btnOK2.setBackground(new Color(157, 195, 230));
        btnOK2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btnOK2.setForeground(Color.BLACK);
        btnOK2.setBorder(new LineBorder(Color.WHITE));
    }

    private void setDisplay() {
        JTabbedPane tabFind = new JTabbedPane();
        tabFind.setBackground(Color.WHITE);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(tabFind,BorderLayout.CENTER);
        pnlMain.setBorder(new EmptyBorder(20,20,20,20));
        add(pnlMain);

        JPanel pnlFindID = new JPanel(new BorderLayout());
        pnlFindID.setBackground(Color.WHITE);
        pnlFindID.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1)));

        FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

        JPanel pnlFindIDCenter = new JPanel(new GridLayout(0, 1));
        pnlFindIDCenter.setBackground(Color.WHITE);

        JPanel pnlName = new JPanel(flowLeft);
        pnlName.add(lblName);
        pnlName.add(tfName);
        pnlName.setBackground(Color.WHITE);

        JPanel pnlNickname = new JPanel(flowLeft);
        pnlNickname.add(lblNickname);
        pnlNickname.add(tfNickname);
        pnlNickname.setBackground(Color.WHITE);

        JPanel pnlQuestion = new JPanel(flowLeft);
        pnlQuestion.add(lblQuestion);
        pnlQuestion.add(comQuestion);
        pnlQuestion.setBackground(Color.WHITE);

        JPanel pnlAnswer = new JPanel(flowLeft);
        pnlAnswer.add(lblAnswer);
        pnlAnswer.add(tfAnswer);
        pnlAnswer.setBackground(Color.WHITE);

        pnlFindIDCenter.add(pnlName);
        pnlFindIDCenter.add(pnlNickname);
        pnlFindIDCenter.add(pnlQuestion);
        pnlFindIDCenter.add(pnlAnswer);
        pnlFindIDCenter.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel pnlFindIDSouth = new JPanel();
        pnlFindIDSouth.setBackground(Color.WHITE);
        pnlFindIDSouth.setBorder(new EmptyBorder(0, 20, 10, 20));
        pnlFindIDSouth.add(btnOK);

        pnlFindID.add(pnlFindIDCenter, BorderLayout.CENTER);
        pnlFindID.add(pnlFindIDSouth, BorderLayout.SOUTH);

        JPanel pnlFindPW = new JPanel(new BorderLayout());
        pnlFindPW.setBackground(Color.WHITE);
        pnlFindPW.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1)));

        JPanel pnlFindPWCenter = new JPanel(new GridLayout(0, 1));
        pnlFindPWCenter.setBackground(Color.WHITE);

        JPanel pnlID = new JPanel(flowLeft);
        pnlID.add(lblID);
        pnlID.add(tfID);
        pnlID.setBackground(Color.WHITE);

        JPanel pnlQuestion2 = new JPanel(flowLeft);
        pnlQuestion2.add(lblQuestion2);
        pnlQuestion2.add(comQuestion2);
        pnlQuestion2.setBackground(Color.WHITE);

        JPanel pnlAnswer2 = new JPanel(flowLeft);
        pnlAnswer2.add(lblAnswer2);
        pnlAnswer2.add(tfAnswer2);
        pnlAnswer2.setBackground(Color.WHITE);

        pnlFindPWCenter.add(pnlID);
        pnlFindPWCenter.add(pnlQuestion2);
        pnlFindPWCenter.add(pnlAnswer2);
        pnlFindPWCenter.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel pnlFindPWSouth = new JPanel();
        pnlFindPWSouth.setBackground(Color.WHITE);
        pnlFindPWSouth.setBorder(new EmptyBorder(0, 20, 25, 10));

        pnlFindPWSouth.add(btnOK2);

        pnlFindPW.add(pnlFindPWCenter, BorderLayout.CENTER);
        pnlFindPW.add(pnlFindPWSouth, BorderLayout.SOUTH);

        tabFind.addTab("아이디 찾기", pnlFindID);
        tabFind.addTab("비밀번호 찾기", pnlFindPW);
        tabFind.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        tabFind.setTabPlacement(JTabbedPane.TOP);

        tabFind.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        tabFind.setForeground(Color.GRAY);
        tabFind.setBackground(Color.WHITE);

    }

    private void addListener() {
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idIsBlank()) {
                    warningMessage("모든 정보를 입력해주세요");
                } else {
                    String str = findId(
                            tfName.getText(),
                            tfNickname.getText(),
                            comQuestion.getSelectedItem().toString(),
                            tfAnswer.getText()
                    );
                    if(findIdCheck == true) {
                        JOptionPane.showMessageDialog(
                                FindIdPw.this,
                                "ID: " + str,
                                "ID 찾기",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else if(findIdCheck == false) {
                        warningMessage("없는 정보입니다.");
                    }
                    tfName.setText("");
                    tfNickname.setText("");
                    tfAnswer.setText("");
                }
            }
        });

        btnOK2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pwIsBlank()) {
                    warningMessage("모든 정보를 입력해 주세요.");

                } else {
                    if(findPw(
                            tfID.getText(),
                            comQuestion2.getSelectedItem().toString(),
                            tfAnswer2.getText()
                    )) {
                        String input = JOptionPane.showInputDialog("새로운 비밀번호를 입력해주세요.");
                        users.getUser(tfID.getText()).setPw(input);
                        JOptionPane.showMessageDialog(
                                FindIdPw.this,
                                "정상적으로 변경되었습니다. 다시 로그인 해 주세요."
                        );
                        dispose();
                        owner.setVisible(true);
                    }

                }
            }
        });


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int choice = JOptionPane.showConfirmDialog(
                        FindIdPw.this,
                        "아이디/비밀번호 찾기를 종료합니다.",
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
    }

    private void showFrame() {
        setTitle("아이디/비밀번호 재설정");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private String findId(String name, String nickname, String question, String answer) {
        ArrayList<User> list = users.getUsers();
        String str = "";

        for(int i = 0; i < list.size(); i++) {
            if(
                list.get(i).getName().equals(name)
                && list.get(i).getNickName().equals(nickname)
                && list.get(i).getQuestion().equals(question)
                && list.get(i).getAnswer().equals(answer)
            ) {
                findIdCheck = true;
                str = list.get(i).getId();
            } else {
                //warningMessage("없는 정보입니다.");
                findIdCheck = false;
                str = "";
            }
        }
        return str;
    }

    private boolean findPw(String id, String question, String answer) {
        ArrayList<User> list = users.getUsers();
        boolean result = false;

        for(int i = 0; i < list.size(); i++) {
            if(
                list.get(i).getId().equals(id)
                && list.get(i).getQuestion().equals(question)
                && list.get(i).getAnswer().equals(answer)
            ) {
                result = true;
            } else {
                warningMessage("없는 정보입니다.");
            }
        }
        return result;
    }

    private boolean idIsBlank() {
        boolean result = false;
        if(tfName.getText().isEmpty()) {
            tfName.requestFocus();
            return true;
        }
        if(tfNickname.getText().isEmpty()) {
            tfNickname.requestFocus();
            return true;
        }
        if(tfAnswer.getText().isEmpty()) {
            tfAnswer.requestFocus();
            return true;
        }
        return result;
    }

    private boolean pwIsBlank() {
        boolean result = false;
        if(tfID.getText().isEmpty()) {
            tfID.requestFocus();
            return true;
        }
        if(tfAnswer2.getText().isEmpty()) {
            tfAnswer2.requestFocus();
            return true;
        }
        return result;
    }

    public void warningMessage(String text) {
        JOptionPane.showMessageDialog(
                null,
                text,
                "경고",
                JOptionPane.WARNING_MESSAGE
        );
    }
}