import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.*;

// quiz의 List + quizzies 추가 + quizzes 삭제
public class TestMaker extends JFrame {

    private UserDataSet users;
    private User user;
    private MainFrame owner;
    private TestDataSet testList;

    public int remainPoints = 100;
    private JLabel lblBack;
    private JLabel info; //도움말 아이콘
    private JLabel lblInfo;
    private JTextArea taInfo;

    private JButton btnChange;
    private JButton btnPreview; // 문제 미리 보기
    private JButton btnComplete; // testList 문제 저장 후 test.dat에 담김 => 파일 전달

    //과목수
    private int subjects;
    //과목이름
    private String[] subjectName;

    private Test test;
    private List<Quiz> quizzes = new ArrayList<>(); // 다 담은 문제들 배열
    private ArrayList<Quizzes> quizList; // Quizzes들 배열

    private JComboBox cbSubjects;
    private CardLayout card;
    private JPanel pnlQuizzes;
    private CardLayout quizCard;
    private JPanel pnlRight; //퀴즈메이커 담을 오른쪽 패널
    private JScrollPane sp;    //테스트메이커 스크롤페인

    private List<JToggleButton> tbtns = new LinkedList<>(); //토글버튼 리스트
    private ButtonGroup group = new ButtonGroup();    //토글버튼 중복선택방지

    private JFrame beforeFrame;

    public TestMaker(Test t, MainFrame owner) {
        test = t;
        this.owner = owner;
        users = owner.getOwner().getUsers(); // DataSet (User들의 정보)
        user = users.getUser(owner.getLblNo()); // 해당 아이디 유저
        testList = user.getTestList(); // 해당 아이디의 유저에 들어있는 testList
        for(int i = 0; i < test.getSubjects().size(); i++) {
            quizzes.addAll(test.getSubjects().get(i).getQuizzes());
        }

        QuizMaker.setPoint(remainPoints);
        init();
        setDisplay();
        addListeners();
        setFrame();
    }

    public User getUser() {
        return user = owner.getOwner().getUsers().getUser(owner.getLblNo());
    }

    @Override
    public MainFrame getOwner() {
        return owner;
    }

    public int getRemainPoints() {
        return remainPoints;
    }
    public void setRemainPoints(int remainPoints) {
        this.remainPoints = remainPoints;
    }

    public List<JToggleButton> getTbtns() {
        return tbtns;
    }

    public Test getTest() {
        return test;
    }

    public JTextArea getTaInfo() {
        return taInfo;
    }

    public JComboBox getCbSubjects() {
        return cbSubjects;
    }

    public void setSubjectName(String[] subjectName) {
        this.subjectName = subjectName;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setBeforeFrame(JFrame beforeFrame) {
        this.beforeFrame = beforeFrame;
    }

    public String[] sNameChange(int idx, String subject) {
        subjectName[idx] = subject;
        return subjectName;
    }

    public JButton getBtnComplete() {
        return btnComplete;
    }

    private void init() {
//        Dimension btnSize = new Dimension(90, 25);
        //quizzes = new LinkedList<Quiz>();

        lblBack = Util.setImageSize("img/뒤로.jpg",40,30);
        info = Util.setImageSize("img/info.png", 30, 30);
        lblInfo = new JLabel("시험정보");
        lblInfo.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btnChange = new JButton("수정");
        btnChange.setBackground(Color.decode("#EBF7FF"));
        btnChange.setPreferredSize(new Dimension(75, 25));
        taInfo = new JTextArea(5, 20);
        taInfo.setText(test.toString());
        taInfo.setEditable(false);
        taInfo.setBorder(new LineBorder(Color.BLACK, 1));
        btnPreview = new JButton("미리보기");
        btnPreview.setBackground(Color.decode("#EBF7FF"));
//        btnPreview.setPreferredSize(btnSize);
        btnPreview.setPreferredSize(new Dimension(90, 25));
        btnComplete = new JButton("파일 내보내기");
        btnComplete.setBackground(Color.decode("#EBF7FF"));
//        btnComplete.setPreferredSize(btnSize);
        btnComplete.setPreferredSize(new Dimension(120, 25));

        //과목 개수
        subjects = test.getSubjects().size();
        //과목별 이름 배열
        subjectName = new String[subjects];
        //초기화
        for (int i = 0; i < subjects; i++) {
            subjectName[i] = test.getSubjects().get(i).getName();
        }
        cbSubjects = new JComboBox(subjectName);
        cbSubjects.setBackground(Color.WHITE);

        //카드레이아웃(테스트메이커)
        card = new CardLayout();
        pnlQuizzes = new JPanel(card);
        //카드레이아웃(퀴즈메이커붙이려고)
        quizCard = new CardLayout();
        pnlRight = new JPanel(quizCard);

        //Quizzes(과목별 토글버튼모음 패널)을 pnlQuizzes에 추가함(식별자 : 과목이름)
        quizList = new ArrayList<>();
        for (int i = 0; i < subjects; i++) {
            quizList.add(new Quizzes(i));
            pnlQuizzes.add(quizList.get(i), subjectName[i]);
            pnlQuizzes.setBackground(Color.WHITE);
        }
        //pnlQuizzes를 스크롤패인으로 만듦
        sp = new JScrollPane(pnlQuizzes);
        sp.setPreferredSize(new Dimension(300, 300));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    }

    private void setDisplay() {
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(Color.WHITE);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);

        JPanel pnlSouth = new JPanel();
        pnlSouth.setBackground(Color.WHITE);

        JPanel pnlInfo = new JPanel(new BorderLayout());
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.add(lblInfo, BorderLayout.WEST);
        pnlInfo.add(btnChange, BorderLayout.EAST);
        pnlInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        pnlNorth.add(pnlInfo, BorderLayout.NORTH);
        pnlNorth.add(taInfo, BorderLayout.CENTER);
        pnlNorth.setBorder(new EmptyBorder(10, 0, 30, 0));

        //저장됨 + 파란색 박스 패널
        JPanel pnlColorBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlColorBtn.setBackground(Color.WHITE);
        JLabel label1 = new JLabel("저장됨");
        JLabel label2 = new JLabel();
        label2.setOpaque(true);
        label2.setBackground(Color.decode("#D4F4FA")); //구름색
        label2.setPreferredSize(new Dimension(30,10));
        pnlColorBtn.add(label1);
        pnlColorBtn.add(label2);

        JPanel pnlCNorth = new JPanel(new BorderLayout());
        pnlCNorth.setBackground(Color.WHITE);
        pnlCNorth.add(cbSubjects, BorderLayout.CENTER);
        pnlCNorth.add(pnlColorBtn, BorderLayout.SOUTH);
        pnlCenter.add(pnlCNorth,BorderLayout.NORTH);
        pnlCenter.add(sp, BorderLayout.CENTER);

        pnlSouth.add(btnPreview);
        pnlSouth.add(btnComplete);

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));


        pnlLeft.add(pnlNorth, BorderLayout.NORTH);
        pnlLeft.add(pnlCenter, BorderLayout.CENTER);
        pnlLeft.add(pnlSouth, BorderLayout.SOUTH);

        JPanel pnlMaker = new JPanel(new BorderLayout());
        pnlMaker.add(pnlLeft, BorderLayout.WEST); // 테스트메이커
        pnlMaker.add(pnlRight, BorderLayout.CENTER); // 퀴즈메이커
        pnlRight.setBorder(new MatteBorder(0, 5, 0, 0, Color.decode("#F6F6F6")));


        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(lblBack, BorderLayout.WEST); // 왼쪽 정렬
        labelPanel.add(info, BorderLayout.EAST); // 오른쪽 정렬
        labelPanel.setBackground(Color.WHITE);

        add(labelPanel, BorderLayout.NORTH);
        add(pnlMaker, BorderLayout.CENTER);
    }

    private void addListeners() {

        MouseListener mListener = new MouseAdapter()  {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == lblBack) {
//                    dispose();
                    changeTestState();
//                    beforeFrame.setVisible(true);
                }

                if(e.getSource() == info) {
                    new Information(TestMaker.this);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                if(e.getSource() == lblBack) {
                    lblBack.setCursor(handCursor);
                }
                if(e.getSource() == info) {
                    info.setCursor(handCursor);
                }
            }
        };
        lblBack.addMouseListener(mListener);
        info.addMouseListener(mListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                dispose();
                changeTestState();
//                beforeFrame.setVisible(true);
            }
        });

        //콤보박스 카드레이아웃
        cbSubjects.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int idx = cbSubjects.getSelectedIndex();

                //name을 식별자로 가지는 카드를 보여준다.
                showTestMakerCard(idx);
            }
        });

        btnComplete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "";
                int idx = 0;

                //저장된 문제 수
                int saved = 0;
                for (JToggleButton tbtn : tbtns) {
                    //모든 문제가 저장되었을때 등록할 수 있음
                    if(tbtn.getBackground().equals(Color.decode("#D4F4FA"))) {
                        saved ++;
                        //흰둥이면 거기로 돌아가게
                    }else {
                        idx = tbtns.indexOf(tbtn);
                    }
                }
                tbtns.get(0).doClick();
                tbtns.get(idx).doClick();

                if(saved == tbtns.size()) {

                    //남은 배점이 0일때 저장
                    QuizMaker qm = (QuizMaker) pnlRight.getComponent(0);
                    if(qm.getLblReaminPoints().equals("0")) {

                        JFileChooser chooser = new JFileChooser("."); //현재폴더위치를 열어라는 의미
                        chooser.setFileFilter(new FileNameExtensionFilter("시험 파일 (*.dat)", "dat"));
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        int choice = chooser.showSaveDialog(TestMaker.this); //부모창이 없으면 null

                        if(choice == JFileChooser.APPROVE_OPTION) {

                            File file = chooser.getSelectedFile();
                            File f = new File(file+".dat");

                            boolean flag = true;
                            while(f.exists()) {
                                int choice1 = JOptionPane.showConfirmDialog(null, "동일한 이름의 파일이 이미 존재합니다. 덮어쓰시겠습니까?","파일 이름 중복",JOptionPane.YES_NO_OPTION);
                                if(choice1 == JOptionPane.YES_OPTION) {
                                    break;
                                } else {
                                    choice = chooser.showSaveDialog(TestMaker.this);
                                    if(choice == JFileChooser.APPROVE_OPTION) {
                                        file = chooser.getSelectedFile();
                                        f = new File(file + ".dat");
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            user.write(test, f);
                            if(flag) {
                                msg = "출제가 완료되었습니다.";
                            }else {
                                msg = "파일 추출이 취소되었습니다.";
                            }
                        }else {
                            msg = "파일 추출이 취소되었습니다.";
                        }

                    }else {
                        msg = "아직 남은 배점이 있습니다.";
                    }

                }else {
                    msg = "모든 문제를 저장해주세요.";
                }

                JOptionPane.showMessageDialog(
                        TestMaker.this,
                        msg,
                        "Information",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        });

        btnChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InputInfo(owner,TestMaker.this);
            }
        });

        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExamFrame(test, test.getSubjects().get(cbSubjects.getSelectedIndex()), getTbtnIdx());
            }
        });

    }

    public void changeTestState() {
        //닫힐 때 모든 문제가 저장되어있는지 검사 -> 출제완료 or 출제중
        int saved = 0;
        JToggleButton unSaved = new JToggleButton();
        for (JToggleButton tbtn : tbtns) {
            //모든 문제가 저장되었을때 등록할 수 있음
            if (tbtn.getBackground().equals(Color.decode("#D4F4FA"))) {
                saved++;
                //흰둥이면 거기로 돌아가게
            } else {
                unSaved = tbtn;
            }
        }
        if (saved == tbtns.size()) {
            testList.getTest(test.getName()).setState("출제완료");
            dispose();
            beforeFrame.setVisible(true);
        } else {
            int choice = JOptionPane.showConfirmDialog(TestMaker.this, "저장되지 않은 문제가 있습니다. 창을 닫으시겠습니까?", "닫기", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    beforeFrame.setVisible(true);
                } else {
                    unSaved.doClick();
                }
            }
        }
    }

    public int getTbtnIdx() {
        for(JToggleButton btn : tbtns) {
            if(btn.isSelected() == true) {
                return tbtns.indexOf(btn);
            }
        }
        return 0;
    }

    private void setFrame() {
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    public void cardChange() {
        pnlQuizzes.removeAll();
        for (int i = 0; i < subjects; i++) {
            pnlQuizzes.add(quizList.get(i), subjectName[i]);
        }
    }
    // 과목 인덱스를 넣으면 테스트메이커카드가 바뀜
    public void showTestMakerCard(int subjectIdx){
        card.show(pnlQuizzes, subjectName[subjectIdx]);
        this.pack();
    }

    //퀴즈번호를 넣으면 몇번째 과목인지 나옴(과목의 인덱스)
    public int whereIsTheQuiz(int quizNum){
        for (Subject sub : test.getSubjects()){
            if(sub.getQuizzes().contains(new Quiz(quizNum))){
                return test.getSubjects().indexOf(sub);
            }
        }
        //없다
        return -1;
    }


    //inner-class
    private class Quizzes extends JPanel {

        private List<JPanel> tbtnPnlList;    //토글버튼 + X레이	블이 담긴 패널리스트
        private int subjectIdx;    //이 과목인덱스
        //private JLabel lblAdd;  //추가레이블

        private Quizzes(int subjectIdx) {
            this.subjectIdx = subjectIdx;
            init();
            setDisplay();
            addListeners();
            setBackground(Color.WHITE);
        }

        private void init() {
            tbtnPnlList = new LinkedList<>();

            int num = test.getQuizzes(subjectIdx).size();
            int startNum = 1;

            if (subjectIdx != 0) {
                for (int i = 0; i < subjectIdx; i++) {
                    startNum += test.getQuizzes(i).size();
                }
            }

            //과목별 퀴즈개수에 맞게 토글버튼을 생성하고 -> X레이블생성
            for (int i = startNum; i < num + startNum; i++) {
                //토글버튼 생성
                JToggleButton tbtn = new JToggleButton(String.valueOf(i) + "번");
                tbtn.setPreferredSize(new Dimension(230, 30));
                tbtn.setBackground(Color.WHITE);
                group.add(tbtn);
                tbtns.add(tbtn);

                JPanel pnl = new JPanel();
                pnl.setBackground(Color.WHITE);
                pnl.add(tbtn);

                tbtnPnlList.add(pnl);    //리스트에 담기
                //pnlRight(카드레이아웃)에 퀴즈메이커 추가하기, 식별자 : 버튼.getText

                pnlRight.add(new QuizMaker(TestMaker.this, i), tbtn.getText());
            }
        }

        //토글버튼패널리스트대로 패널만들어줌
        private void setDisplay() {
            JPanel pnlEntire = new JPanel(new GridLayout(0, 1));
            pnlEntire.setBackground(Color.WHITE);
            for (JPanel p : tbtnPnlList) {
                pnlEntire.add(p);
            }
            add(pnlEntire);
        }


        private void addListeners() {

            //토글버튼 카드레이아웃
            ItemListener iListener = new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    //선택된거, 선택없어진거
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        JToggleButton tbtn = (JToggleButton) e.getSource();
                        int idx = tbtns.indexOf(tbtn);
                        //버튼누를때 과목콤보박스바뀌기
                        showTestMakerCard(whereIsTheQuiz(idx+1));
                        getCbSubjects().setSelectedIndex(whereIsTheQuiz(idx+1));
                        //토글버튼 카드레이아웃
                        quizCard.show(pnlRight, tbtn.getText());
                        QuizMaker qm = (QuizMaker)pnlRight.getComponent(tbtns.indexOf(tbtn));
                        qm.setLblRemainPoints(remainPoints);
                    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        JToggleButton tbtn = (JToggleButton) e.getSource();
                        //토글버튼의 인덱스 +1 은 퀴즈의 번호
                        int quizNum = tbtns.indexOf(tbtn) + 1;

                        QuizMaker quizMaker = (QuizMaker) pnlRight.getComponent(quizNum-1);
                        //toString으로 비교
                        String edited = quizMaker.makeNewQuiz(quizMaker).toString();
                        String saved = quizzes.get(quizNum-1).toString();
                        if(!(edited.equals(saved))) {
                            tbtns.get(quizNum-1).setBackground(Color.WHITE);
                        }

                    }
                }
            };

            for (JPanel pnl : tbtnPnlList) {
                JToggleButton tbtn = (JToggleButton) pnl.getComponent(0);
                tbtn.addItemListener(iListener);
            }
        }
    }
}