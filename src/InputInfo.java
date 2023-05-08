import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InputInfo extends JDialog {
    private MainFrame owner;
    private TestMaker testMaker;
    private UserDataSet users;
    private User user;
    private TestDataSet testList;
    private Test test;

    private JLabel lblBack;
    private JLabel lblTest;
    private JLabel lblGroup;
    private JLabel lblSubject;
    private JLabel lblTime;

    private JTextField tfTest;
    private JTextField tfQuiz;
    private JTextField tfTime;
    private JComboBox<String> cbGroup;
    private JComboBox<Integer> cbSubject;
    private JButton btnSelect;
    private JButton btnInput;

    private JPanel pnlSubjects;
    private JPanel pnlTime;
    private ArrayList<JTextField> tfList = new ArrayList<>();

    ArrayList<Line> subjectArr = new ArrayList<Line>();

    // 새 시험창 시험 정보 입력 활용
    public InputInfo(MainFrame owner) {
        super(owner, "시험 정보 입력", true);
        this.owner = owner;

        usersInit();
        init();
        setDisplay();
        addListeners();
        setFrame();
    }
    // TestMaker 시험 정보 수정 시 활용
    public InputInfo(MainFrame owner, TestMaker testMaker) {
        super(testMaker, "시험 정보 수정", true);
        this.owner = owner;
        this.testMaker = testMaker;

        usersInit();
        test = testList.getTest(testMaker.getTest().getName());

        init();
        setDisplayModi(); // 참고로 Modification(수정)의 Modi를 땃습니다^^~
        addListenersModi();
        setFrame();

    }

    private void usersInit() {
        users = owner.getOwner().getUsers();
        user = users.getUser(owner.getLblNo());
        testList = user.getTestList();
    }

    // lbl만듬
    private JLabel makeLbl(String name) {
        JLabel lbl = new JLabel(name, Label.LEFT);
        lbl.setPreferredSize(new Dimension(60, 30));
        return lbl;
    }

    private void init() {
        lblBack = new JLabel(new ImageIcon("뒤로.jpg"));

        lblGroup = makeLbl("시험 분류");
        String[] testGroup = {"정보처리기사", "한국사", "국어", "수학", "영어", "기타"};
        cbGroup = new JComboBox<String>(testGroup);
        cbGroup.setBackground(Color.WHITE);

        lblTest = makeLbl("시험 이름");
        lblTest.setBackground(Color.WHITE);
        lblSubject = makeLbl("과목 수");
        lblSubject.setBackground(Color.WHITE);
        lblTime = makeLbl("제한 시간");
        lblTime.setBackground(Color.WHITE);

        tfTest = new JTextField();
        tfTest.setBackground(Color.WHITE);
        tfTest.setPreferredSize(new Dimension(245,30));
        tfQuiz = new JTextField("최대 100문제");
        tfQuiz.setBackground(Color.WHITE);
        tfQuiz.setPreferredSize(new Dimension(240,30));
        tfTime  = new JTextField();
        tfTime.setBackground(Color.WHITE);
        tfTime.setPreferredSize(new Dimension(225,30));

        Integer[] subjectNum = {1, 2, 3, 4, 5};
        cbSubject = new JComboBox<Integer>(subjectNum);
        cbSubject.setBackground(Color.WHITE);
        cbSubject.setPreferredSize(new Dimension(160,30));
        btnSelect = new JButton("선택");
        btnSelect.setBackground(Color.decode("#EBF7FF"));
        btnInput = new JButton("시험 정보 입력");
        btnInput.setBackground(Color.decode("#EBF7FF"));
    }

    // 과목패널 만듬
    private JPanel pnlSubjectsMake(int idx) {
        JLabel lbl = makeLbl(String.valueOf(idx + 1) + "과목");

        JTextField tfName = new JTextField();
        tfList.add(tfName);

        tfName.setText(test.getSubjects().get(idx).getName());
        tfName.setPreferredSize(new Dimension(120, 30));

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl.setBackground(Color.WHITE);
        pnl.add(lbl);
        pnl.add(tfName);

        return pnl;

    }

    // 문제 수정 setDisplayModi()
    private void setDisplayModi() {

        // 시험이름
        JPanel pnlTest = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTest.setBackground(Color.WHITE);
        pnlTest.add(lblTest);
        tfTest.setText(test.getName());
        pnlTest.add(tfTest);

        // 과목
        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.setBackground(Color.WHITE);
        try{
            int subjectSize = test.getSubjects().size();
            for(int i = 0; i < subjectSize; i++) {
                pnl.add(pnlSubjectsMake(i));

            }
        } catch (IndexOutOfBoundsException e) {}

        // 시험시간
        pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTime.setBackground(Color.WHITE);
        pnlTime.add(lblTime);
        tfTime.setText(String.valueOf((test.getTimeOut() / 60)));
        pnlTime.add(tfTime);
        tfTime.setPreferredSize(new Dimension(120, 30));
        pnlTime.add(new JLabel("분"));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(pnl, BorderLayout.NORTH);
        pnlCenter.add(pnlTime, BorderLayout.CENTER);

        btnSelect.setText("시험 정보 수정");
        add(pnlTest, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(btnSelect, BorderLayout.SOUTH);

    }
    private void setDisplay() {
        // 시험 분류
        JPanel pnlGroup = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGroup.setBackground(Color.WHITE);
        pnlGroup.add(lblGroup);
        pnlGroup.add(cbGroup);

        // 시험 이름
        JPanel pnlTest = new JPanel();
        pnlTest.setBackground(Color.WHITE);
        pnlTest.add(lblTest);
        pnlTest.add(tfTest);

        JPanel pnlTG = new JPanel(new GridLayout(0, 1));
        pnlTG.add(pnlGroup);
        pnlTG.add(pnlTest);

        JPanel subjectNum = new JPanel();
        subjectNum.setBackground(Color.WHITE);
        subjectNum.add(lblSubject);
        subjectNum.add(cbSubject);
        subjectNum.add(btnSelect);

        pnlSubjects = new JPanel(new GridLayout(0,1));
        pnlSubjects.setBackground(Color.WHITE);

        pnlTime = new JPanel();
        pnlTime.setBackground(Color.WHITE);
        pnlTime.add(lblTime);
        pnlTime.add(tfTime);
        pnlTime.add(new JLabel("분"));


        JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNorth.setBackground(Color.WHITE);
        pnlNorth.add(lblBack);


        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(pnlTG, BorderLayout.NORTH);
        pnlCenter.add(subjectNum,BorderLayout.CENTER);
        pnlCenter.add(pnlSubjects,BorderLayout.SOUTH);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.add(pnlTime,BorderLayout.NORTH);
        pnlSouth.add(btnInput,BorderLayout.CENTER);

        add(pnlNorth, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    // 문제 수정 액션
    private void addListenersModi() {
        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 시험이름 수정
                test.setName(tfTest.getText());

                // 과목이름 수정 -> 콤보박스도 같이 수정
                String[] subjectNames = {};
                for(int i = 0; i < test.getSubjects().size(); i++) {
                    test.getSubjects().get(i).setName(tfList.get(i).getText());
                    subjectNames = testMaker.sNameChange(i, tfList.get(i).getText());
                }
                testMaker.setSubjectName(subjectNames);
                testMaker.getCbSubjects().setModel(new DefaultComboBoxModel(subjectNames));
                testMaker.cardChange();

                // 시험시간 수정
                test.setTimeOut(Integer.parseInt(tfTime.getText()) * 60);
                testMaker.getTaInfo().setText(test.toString());

                dispose();
            }
        });


    }
    private void addListeners() {

        MouseListener mListener = new MouseAdapter()  {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == lblBack) {
                    dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                if(e.getSource() == lblBack) {
                    lblBack.setCursor(handCursor);
                }
            }
        };
        lblBack.addMouseListener(mListener);

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlSubjects.removeAll();
                subjectArr.clear();
                int num = (int)(cbSubject.getSelectedItem());

                for(int i=1; i<num+1; i++) {
                    String lbl = String.valueOf(i) + "과목";
                    Line line = new Line(lbl);
                    subjectArr.add(line);
                }
                pack();
            }
        });

        btnInput.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                boolean flag = true;
                String msg = "";

                //입력확인
                if(flag && Util.isEmpty(tfTest)) {
                    flag = false;
                    msg = "시험이름을 입력하세요";
                    tfTest.requestFocus();
                }else {
                    //똑같은 이름있는지 체크
                    if(testList.containsTest(new Test(tfTest.getText()))) {
                        flag = false;
                        tfTest.requestFocus();
                        msg = "같은 이름이 있습니다. 시험이름을 수정해주세요.";
                    }
                }

                if(flag && subjectArr.size()==0) {
                    flag = false;
                    msg = "과목수를 선택하세요";
                    btnSelect.requestFocus();
                }

                List<Subject> subjects = new LinkedList<>();

                if(flag) {
                    int startNum = 1;
                    for (Line subject : subjectArr) {
                        String name = subject.tfName.getText();
                        if (name.equals("과목이름")) {
                            flag = false;
                            msg = "과목이름을 입력하세요";
                        }
                        if(flag) {
                            int same = 0;
                            for (Line s : subjectArr) {
                                if(subject.tfName.getText().equals(s.tfName.getText())) {
                                    same++;
                                };
                            }
                            if (same > 1) {
                                flag = false;
                                msg = "과목 이름이 같습니다. 수정해주세요.";
                            }

                            if (flag) {
                                try {
                                    int num = Integer.parseInt(subject.tfQuiz.getText());
                                    if (num < 1 || num > 100) {
                                        flag = false;
                                        msg = "1부터 100 사이의 숫자를 입력하세요";
                                        subject.tfQuiz.requestFocus();

                                    } else {
                                        // subjects 배열에 추가
                                        subjects.add(new Subject(name, num, startNum));

                                        startNum += num;
                                    }
                                } catch (NumberFormatException nee) {
                                    flag = false;
                                    msg = "문제수를 숫자로 입력하세요";
                                    subject.tfQuiz.requestFocus();
                                }
                            }

                        }
                    }
                }

                if(flag) {
                        try {
                            int timeOut = Integer.parseInt(tfTime.getText()) * 60;
                            if(timeOut > 0) {
                                //테스트 생성
                                msg = "시험 생성 완료!";
                                Test t = new Test(tfTest.getText(), subjects, timeOut, (String)cbGroup.getSelectedItem());
                                testList.addTest(t);
                                TestMaker tm = new TestMaker(t, owner);
                                tm.setBeforeFrame(owner);
                                //닫기
                                dispose();
                                owner.setVisible(false);

                            } else {
                                msg = "0보다 큰 숫자를 입력하세요";
                                tfTime.requestFocus();
                            }
                        } catch (NumberFormatException ne) {
                            msg = "제한시간을 숫자로 입력하세요";
                            tfTime.requestFocus();
                        }
                }

                JOptionPane.showMessageDialog(
                        InputInfo.this,
                        msg,
                        "Information",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        });
    }

    private void setFrame() {
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    //inner-class
    private class Line {
        private JLabel lbl;
        private JTextField tfName;
        private JTextField tfQuiz;
        private JLabel lblDelete;
        private JPanel pnl;

        private Line(String name) {
            init();
            setDisplay();
            addListeners();
            lbl.setText(name);
        }

        private void init() {
            lbl = makeLbl("");
            tfName = new JTextField("과목이름");
            tfQuiz = new JTextField("최대 100문제");
            lblDelete = new JLabel("X");

            tfName.setForeground(Color.GRAY);
            tfQuiz.setForeground(Color.GRAY);
            tfName.setHorizontalAlignment(JTextField.CENTER);
            tfQuiz.setHorizontalAlignment(JTextField.CENTER);
            tfName.setPreferredSize(new Dimension(120,30));
            tfQuiz.setPreferredSize(new Dimension(100,30));
        }

        private void setDisplay() {
            pnl = new JPanel();
            pnl.add(lbl);
            pnl.add(tfName);
            pnl.add(tfQuiz);
            pnl.add(lblDelete);
            pnl.setBackground(Color.WHITE);
            pnlSubjects.add(pnl);
            pnlSubjects.setBackground(Color.WHITE);
        }

        private void addListeners() {

            MouseListener mListener = new MouseAdapter()  {

                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getSource() == lblDelete) {
                        pnlSubjects.remove(pnl);
                        int idx = subjectArr.indexOf(Line.this);
                        subjectArr.remove(Line.this);

                        for(int i = idx; i<subjectArr.size(); i++) {
                            subjectArr.get(i).lbl.setText(String.valueOf(i + 1) + "과목");
                        }
                        pack();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                    if(e.getSource() == lblDelete) {
                        lblDelete.setCursor(handCursor);
                    }
                }
            };
            lblDelete.addMouseListener(mListener);

            FocusListener focusListener = new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {

                    if(e.getSource() == tfName) {
                        tfName.setText("");
                        tfName.setForeground(Color.BLACK);
                    }
                    if(e.getSource() == tfQuiz) {
                        tfQuiz.setText("");
                        tfQuiz.setForeground(Color.BLACK);
                    }

                }
                // 포커스를 잃었을 때
                @Override
                public void focusLost(FocusEvent e) {
                    if(e.getSource() == tfName) {
                        if(tfName.getText().trim().equals("")) {
                            tfName.setText("과목이름");
                            tfName.setForeground(Color.LIGHT_GRAY);
                        }
                    } else {
                        if(tfQuiz.getText().trim().equals("")) {
                            tfQuiz.setText("최대 100문제");
                            tfQuiz.setForeground(Color.LIGHT_GRAY);
                        }
                    }
                }
            };
            tfName.addFocusListener(focusListener);
            tfQuiz.addFocusListener(focusListener);
        }
    }
}