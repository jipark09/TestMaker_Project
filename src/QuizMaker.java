import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuizMaker extends JPanel {
    private PnlQuizInfo pnlQuizInfo;
    private TestMaker testMaker;
    private User user;

    private ArrayList<String> followingList;
    private ArrayList<String> textsList;
    private File originalFile; // 원본 사진파일

    private JLabel lblPhoto;
    private JLabel lblText;
    private JButton btnImport;

    private JLabel lblNum;
    private JTextArea taQuiz;

    private JLabel lblPlusF;
    private JLabel lblPlusCh;

    //이전문제 다음문제
    private JLabel lblPrev;
    private JLabel lblNext;
    private JButton btnSave; // 퀴즈 배열에 담김
    private JButton btnInit;
    private JTextArea lineTa;

    private JPanel pnlAdd;
    private JPanel pnlF;
    private JPanel pnlCh;
    private JPanel pnlLineBox = new JPanel();  //선택한 문제지문 담을 변수
    private int chNum = 0;
    private boolean flag = true; // 문제 지문

    private Insets taMargin = new Insets(5,5,5,50);
    private Font titleFont = new Font("맑은 고딕", Font.BOLD, 15);
    private Font normalFont = new Font("맑은 고딕", Font.PLAIN, 15);

    private int quizNum;
    private JCheckBox[] cbAnswers;

    public QuizMaker(TestMaker t, int num) {
        this.testMaker = t;
        this.quizNum = num;
        init();
        setDisplay();
        addListeners();
        setBackground(Color.WHITE);
    }

    public void setChNum(int chNum) {
        this.chNum = chNum;
    }

    public JPanel getPnlF() {
        return pnlF;
    }

    public JPanel getPnlCh() {
        return pnlCh;
    }

    public JTextArea getTaQuiz() {
        return taQuiz;
    }

    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

    public void setTextsList(ArrayList<String> textsList) {
        this.textsList = textsList;
    }

    public JCheckBox[] getCbAnswers() {
        return cbAnswers;
    }

    public JTextArea getTaComment() {
        return pnlQuizInfo.taComment;
    }

    public void setQuizNum(int quizNum) {
        this.quizNum = quizNum;
        lblNum.setText(String.valueOf(quizNum));
    }

    public void setHashTag(List<String> list) {
        switch (list.size()) {
            case 0:
                break;
            case 1:
                pnlQuizInfo.lblTag1.setText(list.get(0));
                break;
            case 2:
                pnlQuizInfo.lblTag1.setText(list.get(0));
                pnlQuizInfo.lblTag2.setText(list.get(1));
                break;
            case 3:
                pnlQuizInfo.lblTag1.setText(list.get(0));
                pnlQuizInfo.lblTag2.setText(list.get(1));
                pnlQuizInfo.lblTag3.setText(list.get(2));
                break;
        }
    }

    public JLabel getLbl(String text) {
        JLabel lbl = new JLabel(text, JLabel.LEFT);
        lbl.setBackground(Color.WHITE);
        lbl.setFont(titleFont);
        lbl.setBorder(new EmptyBorder(5, 0, 5, 5));
        return lbl;
    }
    public JTextArea getTa(int rows, int columns) {
        JTextArea ta = new JTextArea(rows, columns);
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        ta.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5,5,5,5)));
        ta.setFont(normalFont);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    public TestMaker getTestMaker() {
        return testMaker;
    }

    private void init() {

        lblPhoto = Util.setImageSize("img/사진.png", 40, 40);
        lblText = Util.setImageSize("img/텍스트.png", 35, 35);

        btnImport = new JButton("문제 불러오기");
        btnImport.setBackground(Color.decode("#EBF7FF"));

        lblNum = getLbl(String.valueOf(quizNum));
        taQuiz = getTa(1,33);
        if(getQuiz(quizNum).getTitle()==null){

            taQuiz.setText(" 문제를 입력하세요");
            taQuiz.setForeground(Color.LIGHT_GRAY);

        } else {
            // 제목이 NULL이 아닌 건 이미 저장된 문제라는 거니깐 토글버튼 색을 바꿔놓음
            testMaker.getTbtns().get(quizNum-1).setBackground(Color.decode("#D4F4FA"));
            taQuiz.setText(getQuiz(quizNum).getTitle());
            taQuiz.setForeground(Color.BLACK);
        }

        lblPlusF = new JLabel("+ 추가", JLabel.CENTER);
        lblPlusF.setFont(normalFont);
        lblPlusCh = new JLabel("+ 추가", JLabel.CENTER);
        lblPlusCh.setFont(normalFont);

        lblPrev = Util.setImageSize("img/before.jpg",30,30);
        lblNext = Util.setImageSize("img/after.jpg",30,30);
        btnSave = new JButton("저장");
        btnSave.setBackground(Color.decode("#EBF7FF"));
        btnInit= new JButton("초기화");
        btnInit.setBackground(Color.decode("#EBF7FF"));

    }
    public void makeF(String str) {
        //사진파일이거나 ? 아니거나?
        Following f = new Following();
        f.setBackground(Color.WHITE);

        f.pnlLineBox.setPreferredSize(null);

        if(isImageFile(str)){
            JLabel lblImg = new JLabel(new ImageIcon(str));
            f.pnlLineBox.add(lblImg);
        } else {
            JTextArea lineTa = getTa(7,30);
            lineTa.setText(str);
            f.pnlLineBox.add(lineTa);
        }
        pnlF.add(f);
    }

    public void makeCh(String str, int i) {
        Choice choice = new Choice(i+1);
        choice.setBackground(Color.WHITE);

        choice.pnlLineBox.setPreferredSize(null);

        if(isImageFile(str)){
            JLabel lblImg = new JLabel(new ImageIcon(str));
            choice.pnlLineBox.add(lblImg);
        }else {
            JTextArea lineTa = getTa(1, 27);
            lineTa.setText(str);
            choice.pnlLineBox.add(lineTa);
        }
        pnlCh.add(choice);
    }

    private void setDisplay() {
        pnlQuizInfo = new PnlQuizInfo();
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(Color.WHITE);

        pnlAdd = new JPanel();
        pnlAdd.add(lblPhoto);
        pnlAdd.add(lblText);
        pnlAdd.setBackground(Color.WHITE);

        // 처음에는 안보이는 상태
        pnlAdd.setVisible(false);

        pnlNorth.add(pnlAdd, BorderLayout.WEST);
        pnlNorth.add(new JLabel("                     "), BorderLayout.CENTER);
        pnlNorth.add(btnImport, BorderLayout.EAST);
        pnlNorth.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel pnlQuiz = new JPanel();
        pnlQuiz.add(lblNum);
        pnlQuiz.add(taQuiz);
        pnlQuiz.setBorder(new EmptyBorder(10, 0, 20, 0 ));
        pnlQuiz.setBackground(Color.WHITE);

        pnlF = new JPanel(new FlowLayout());
        pnlF.setBackground(Color.WHITE);

        List<String> following = getQuiz(quizNum).getFollowing();
        if(following==null) {
            pnlF.add(new Following()).setBackground(Color.WHITE);
        } else {
            for(int i=0; i<following.size(); i++) {

                String str = following.get(i);
                //사진파일이거나 ? 아니거나?
                Following f = new Following();
                f.setBackground(Color.WHITE);

                f.pnlLineBox.setPreferredSize(null);

                if(isImageFile(str)) {
                    JLabel lblImg = new JLabel(new ImageIcon(str));
                    f.pnlLineBox.add(lblImg);

                } else {
                    JTextArea lineTa = getTa(7,30);
                    lineTa.setText(str);
                    f.pnlLineBox.add(lineTa);
                }
                pnlF.add(f);
            }
        }

        pnlF.setLayout(new BoxLayout(pnlF, BoxLayout.Y_AXIS));

        JPanel pnlFollowing = new JPanel(new BorderLayout());
        pnlFollowing.add(pnlF, BorderLayout.CENTER);
        pnlFollowing.add(lblPlusF, BorderLayout.SOUTH);
        pnlFollowing.setBackground(Color.WHITE);

        JScrollPane sp1 = new JScrollPane(pnlFollowing);
        sp1.setBackground(Color.WHITE);
        sp1.setPreferredSize(new Dimension(200, 250));
        sp1.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "문제 지문"));
        sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp1.getVerticalScrollBar().setUnitIncrement(16);    //스크롤 속도

        pnlCh = new JPanel(new FlowLayout(FlowLayout.CENTER,0, 0));
        pnlCh.setBackground(Color.WHITE);

        //새로운 문제만들기할때 아니면 내문제보기 누를때 (문제가 이미 있을 때, 없을때)
        List<String> texts = getQuiz(quizNum).getTexts();
        if(texts == null) {
            for(int i = 0; i < 5; i++) {
                pnlCh.add(new Choice(i + 1)).setBackground(Color.WHITE);
            }
        } else {
            for(int i=0; i<texts.size(); i++) {

                String str = texts.get(i);
                //사진파일이거나 ? 아니거나?
                Choice choice = new Choice(i+1);
                choice.setBackground(Color.WHITE);

                choice.pnlLineBox.setPreferredSize(null);

                if(isImageFile(str)){
                    JLabel lblImg = new JLabel(new ImageIcon(str));
                    choice.pnlLineBox.add(lblImg);
                } else {
                    JTextArea lineTa = getTa(1, 27);
                    lineTa.setText(str);
                    choice.pnlLineBox.add(lineTa);
                }
                pnlCh.add(choice);
            }
            chNum = texts.size();
        }

        pnlCh.setLayout(new BoxLayout(pnlCh, BoxLayout.Y_AXIS));

        JPanel pnlChoice = new JPanel(new BorderLayout());
        pnlChoice.add(pnlCh, BorderLayout.CENTER);
        pnlChoice.add(lblPlusCh, BorderLayout.SOUTH);
        pnlChoice.setBackground(Color.WHITE);

        JScrollPane sp2 = new JScrollPane(pnlChoice);
        sp2.setBackground(Color.WHITE);
        sp2.setPreferredSize(new Dimension(200,250));
        sp2.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "선지 지문"));
        sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp2.getVerticalScrollBar().setUnitIncrement(16);	//스크롤 속도

        JPanel pnlBlank = new JPanel();
        pnlBlank.add(new JLabel("     "));
        pnlBlank.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlBlank.setBackground(Color.WHITE);

        JPanel pnlCCenter = new JPanel(new BorderLayout());
        pnlCCenter.add(sp1, BorderLayout.NORTH);
        pnlCCenter.add(pnlBlank, BorderLayout.CENTER);
        pnlCCenter.add(sp2,BorderLayout.SOUTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlQuiz, BorderLayout.NORTH);
        pnlCenter.add(pnlCCenter, BorderLayout.CENTER);
        pnlCenter.add(new JLabel("    "), BorderLayout.SOUTH);
        pnlCenter.setBackground(Color.WHITE);


        JPanel pnlBtns = new JPanel();
        pnlBtns.add(btnSave);
        pnlBtns.add(btnInit);
        pnlBtns.setBackground(Color.WHITE);
        pnlBtns.setBorder(new EmptyBorder(0, 150, 0, 150));

        JPanel pnlSouth = new JPanel();
        pnlSouth.add(lblPrev, BorderLayout.WEST);
        pnlSouth.add(pnlBtns, BorderLayout.CENTER);
        pnlSouth.add(lblNext, BorderLayout.EAST);
        pnlSouth.setBackground(Color.WHITE);

        JPanel pnlQuizMaker = new JPanel(new BorderLayout());
        pnlQuizMaker.add(pnlNorth, BorderLayout.NORTH);
        pnlQuizMaker.add(pnlCenter, BorderLayout.CENTER);
        pnlQuizMaker.add(pnlSouth, BorderLayout.SOUTH);
        pnlQuizMaker.setBorder(new EmptyBorder(taMargin));
        pnlQuizMaker.setBackground(Color.WHITE);

        JPanel pnlTotal = new JPanel(new BorderLayout());
        pnlTotal.setBackground(Color.WHITE);
        pnlTotal.add(pnlQuizMaker, BorderLayout.CENTER);

        pnlQuizInfo.setBorder(new MatteBorder(0, 5, 0, 0, Color.decode("#F6F6F6")));
        pnlTotal.add(pnlQuizInfo, BorderLayout.EAST);
        pnlTotal.setBorder(new EmptyBorder(0, 10, 0, 0));

        add(pnlTotal);


    }
    private void addListeners() {
        // 문제입력칸 클릭 시 "문제를 입력하세요" 사라짐
        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                if(e.getSource() == taQuiz) {
                    if(taQuiz.getText().equals(" 문제를 입력하세요")) {
                        taQuiz.setText("");
                    }
                    taQuiz.setForeground(Color.BLACK);
                }

            }
            @Override
            public void focusLost(FocusEvent e) {
                if(e.getSource() == taQuiz) {

                    if (taQuiz.getText().trim().equals("")) {
                        taQuiz.setText(" 문제를 입력하세요");
                        taQuiz.setForeground(Color.LIGHT_GRAY);
                    }
                }
            }
        };
        taQuiz.addFocusListener(focusListener);


        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MyTest(testMaker.getOwner(),QuizMaker.this);
                testMaker.setVisible(false);
            }
        });

        lblPhoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("이미지", "jpg", "gif", "png", "bmp"));
                int choice = chooser.showOpenDialog(null);

                if(choice == JFileChooser.APPROVE_OPTION) {
                    originalFile = chooser.getSelectedFile();
                    //String originalFilePath = originalFile.getAbsolutePath();
                    String originalFileName = originalFile.getName(); // 원본사진파일
                    String userId = testMaker.getOwner().getLblNo() + "_Img"; // 유저아이디 + Img
                    File userImg = new File(userId); // 유저아이디 + Img 파일(폴더) 생성

                    // 만약 이 폴더가 없으면
                    if(!userImg.exists()) {
                        userImg.mkdir(); // 폴더 생성
                    }
                    String targetFilePath = userId + "/" + originalFileName; // 유저이미지 폴더에 집어넣을 사진
                    File targetFile = new File(targetFilePath);

                    try {
                        Files.copy(originalFile.toPath(), targetFile.toPath()); // 원본사진을 유저이미지 폴더에 저장
                    } catch (IOException e2) {}
                    JLabel lblImg = new JLabel(new ImageIcon(targetFilePath));
                    //Util.setImageSize(f.getName(), 500, 500);

                    pnlLineBox.setPreferredSize(null);
                    pnlLineBox.removeAll();
                    pnlLineBox.add(lblImg);
                    //다시 검은색으로
                    pnlLineBox.setBorder(new LineBorder(Color.BLACK));
                    pnlAdd.setVisible(false);
                    testMaker.pack();
                }
            }
        });


        lblText.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if(flag == true) {
                    lineTa = getTa(7,30);
                } else {
                    lineTa = getTa(1, 27);
                }
                pnlLineBox.setPreferredSize(null);
                pnlLineBox.removeAll();
                pnlLineBox.add(lineTa);
                lineTa.requestFocus();
                //다시 검은색으로
                pnlLineBox.setBorder(new LineBorder(Color.BLACK));

                pnlAdd.setVisible(false);
                testMaker.pack();
            }
        });

        //문제 지문 추가
        lblPlusF.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pnlF.add(new Following()).setBackground(Color.WHITE);
                testMaker.pack(); //owner로 테스트메이커를받아서 걔를 팩해보자!!
            }


            @Override
            public void mouseEntered(MouseEvent e) {
                setHandCursor(lblPlusF);
            }
        });

        //선지 지문 추가
        lblPlusCh.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(chNum <= 4) {
                    pnlCh.add(new Choice(chNum + 1)).setBackground(Color.WHITE);
                    testMaker.pack();
                } else {
                    JOptionPane.showMessageDialog(
                            QuizMaker.this,
                            "선지 지문은 5개까지 입력 가능합니다."
                    );
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHandCursor(lblPlusCh);
            }
        });

        MouseListener cardListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                int idx = quizNum;

                if(e.getSource()==lblPrev) {
                    if(quizNum == 1) {
                        idx = testMaker.getTbtns().size() - 1;
                    }else {
                        idx = quizNum - 2;
                    }
                } else {
                    if(quizNum == testMaker.getTbtns().size()) {
                        idx = 0;
                    }
                }

                testMaker.getTbtns().get(idx).doClick();
                testMaker.getTbtns().get(idx).setSelected(true);
                testMaker.pack();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHandCursor(lblPrev);
                setHandCursor(lblNext);

            }
        };
        lblPrev.addMouseListener(cardListener);
        lblNext.addMouseListener(cardListener);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btnSave) {

                    //문제입력칸 비면 알림창
                    if(taQuiz.getText().trim().equals("") || taQuiz.getText().trim().equals("문제를 입력하세요")) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "문제를 입력해주세요"
                        );
                        taQuiz.requestFocus();
                        return;
                    }
                    //선지지문 패널 하나도 없을때
                    if(chNum == 0) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "선지지문은 하나라도 입력되어야 합니다."
                        );
                        return;
                    }

                    //배점 입력안되면 알림창
                    if(pnlQuizInfo.tfPoint.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "배점을 넣어주세요"
                        );
                        pnlQuizInfo.tfPoint.requestFocus();
                        return;
                    } else {
                        try {
                            int point = 0;
                            point = Integer.parseInt(pnlQuizInfo.tfPoint.getText());
                            if (point < 1 || point > 100) {
                                JOptionPane.showMessageDialog(
                                        QuizMaker.this,
                                        "1부터 100까지 숫자만 입력 가능합니다."
                                );
                                pnlQuizInfo.tfPoint.requestFocus();
                                return;
                            }
                        } catch (NumberFormatException nee) {
                            JOptionPane.showMessageDialog(
                                    QuizMaker.this,
                                    "1부터 100까지 숫자만 입력 가능합니다."
                            );
                            pnlQuizInfo.tfPoint.requestFocus();
                            return;
                        }
                    }
                    //답체크박스 하나라도 체크안하면 알림창
                    if(!isCheckboxSelected(cbAnswers)) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "답을 하나 이상 선택해 주세요."
                        );
                        return;
                    }

                    //해설 입력안되면 알림창
                    if(pnlQuizInfo.taComment.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "해설을 입력해주세요"
                        );
                        pnlQuizInfo.taComment.requestFocus();
                        return;
                    }
                    String hash1 = pnlQuizInfo.lblTag1.getText();
                    String hash2 = pnlQuizInfo.lblTag2.getText();
                    String hash3 = pnlQuizInfo.lblTag3.getText();

                    // 해시태그 하나도 선택 안되면 알림창
                    if((hash1.equals("#태그1") || hash1.equals("")) && (hash2.equals("#태그2") || hash2.equals("")) && (hash3.equals("#태그3") || hash3.equals(""))) {

                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "해시태그를 하나 이상 선택해 주세요."
                        );
                        return;
                    }

                    quizzesAdd(QuizMaker.this);

                    //토글버튼 색 바꾸기
                    testMaker.getTbtns().get(quizNum-1).setBackground(Color.decode("#D4F4FA"));
                    testMaker.pack();

                    followingList.clear();
                    textsList.clear();
                } else if(e.getSource() == btnInit){
                    //모든 칸 비우기
                    taQuiz.setText(" 문제를 입력하세요");
                    taQuiz.setForeground(Color.LIGHT_GRAY);
                    pnlF.removeAll();
                    pnlF.add(new Following()).setBackground(Color.WHITE);
                    pnlCh.removeAll();
                    pnlCh.add(new Choice(1)).setBackground(Color.WHITE);
                    chNum = 1;

                    //pnlQuizInfo 창 초기화
                    getQuiz(quizNum).setPoint(0);
                    pnlQuizInfo.tfPoint.setText("0");

                    for (JCheckBox cb : cbAnswers) {
                        if(cb.isSelected()){
                            cb.setSelected(false);
                        }
                    }
                    pnlQuizInfo.taComment.setText("");
                    pnlQuizInfo.lblTag1.setText("#태그1");
                    pnlQuizInfo.lblTag2.setText("#태그2");
                    pnlQuizInfo.lblTag3.setText("#태그3");
                    pnlQuizInfo.lblTag1.setForeground(Color.GRAY);
                    pnlQuizInfo.lblTag2.setForeground(Color.GRAY);
                    pnlQuizInfo.lblTag3.setForeground(Color.GRAY);


                    testMaker.pack();
                }
            }
        };
        btnSave.addActionListener(actionListener);
        btnInit.addActionListener(actionListener);
    }
    public boolean isCheckboxSelected(JCheckBox[] checkboxes) {
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    //메소드
    public void setHandCursor(Component c) {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        c.setCursor(handCursor);
    }
    public boolean isImageFile(String fileName) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        int idx = fileName.lastIndexOf(".");
        if(idx>0) {
            String extension = fileName.substring(idx).toLowerCase();  // 문자열의 끝에서 4글자 추출 후 소문자로 변환
            for (String imageExtension : imageExtensions) {
                if (extension.equals(imageExtension)) {
                    return true;  // 추출한 확장자가 사진 파일 확장자 중 하나인 경우
                }
            }
        }
        return false;  // 추출한 확장자가 사진 파일 확장자가 아닌 경우
    }

    // quizzes 배열 정렬
    public class OrderByQuizNum implements Comparator<Quiz> {
        @Override
        public int compare(Quiz o1, Quiz o2) {
            int result = o1.getNum() - o2.getNum();
            return result;
        }
    }

    //testMaker.getQuizzes
    public List<Quiz> getQuizzes() {
        return testMaker.getQuizzes();
    }
    // num에 대한 정보를 가져옴
    public Quiz getQuiz(int num) {
        return getQuizzes().get(getQuizzes().indexOf(new Quiz(num)));
    }

    // QuizMaker안에 있는 값들을 퀴즈객체로 바꾸기
    public Quiz makeNewQuiz(QuizMaker quizMaker) {

        int num = quizMaker.quizNum;
        int point = quizMaker.pnlQuizInfo.getTfPoint();
        String title = quizMaker.taQuiz.getText();

        followingList = new ArrayList<>();
        for(int i=0; i<pnlF.getComponentCount(); i++) {
            Following f =  (Following) pnlF.getComponent(i);
            followingList.add(f.getStr());
        }
        ArrayList<String> following = new ArrayList<>();
        following.addAll(quizMaker.followingList);


        textsList = new ArrayList<>();
        for(int i = 0; i<pnlCh.getComponentCount(); i++) {
            Choice ch = (Choice) pnlCh.getComponent(i);
            textsList.add(ch.getStr());
        }
        ArrayList<String> texts = new ArrayList<>();
        texts.addAll(quizMaker.textsList);

        ArrayList<Integer> answers = quizMaker.pnlQuizInfo.getAnswer();
        String explain = quizMaker.pnlQuizInfo.getComment();
        List<String> hashTag = quizMaker.pnlQuizInfo.getHashTag();
        Quiz quiz = new Quiz(num, point, title, following, texts, answers, explain, hashTag);

        return quiz;
    }


    // QuizMaker안에 있는 값들을 퀴즈객체로 quizzes list에 추가시키기
    public void quizzesAdd(QuizMaker quizMaker) {

        Quiz quiz = makeNewQuiz(quizMaker);
        boolean result = true;
        for(String str : quiz.getTexts()) {
            if(str.equals("") && result) {
                JOptionPane.showMessageDialog(
                        QuizMaker.this,
                        "텍스트를 입력해주세요."
                );
                result = false;
            } else {
                for (int i = 0; i <= getQuizzes().size(); i++) {
                    // 1
                    if (getQuizzes().contains(new Quiz(quiz.getNum()))) {
                        setQuiz(quiz);
                    } else {
                        getQuizzes().add(quiz);
                    }
                }
                Collections.sort(getQuizzes(), new OrderByQuizNum());
            }
        }
    }

    // num이 같으면 덮어씌우기
    public void setQuiz(Quiz quiz) {
        getQuiz(quiz.getNum()).setNum(quiz.getNum());
        getQuiz(quiz.getNum()).setPoint(quiz.getPoint());
        getQuiz(quiz.getNum()).setTitle(quiz.getTitle());
        getQuiz(quiz.getNum()).setFollowing(quiz.getFollowing());
        getQuiz(quiz.getNum()).setTexts(quiz.getTexts());
        getQuiz(quiz.getNum()).setAnswers(quiz.getAnswers());
        getQuiz(quiz.getNum()).setExplain(quiz.getExplain());
        getQuiz(quiz.getNum()).setHashTag(quiz.getHashTag());
    }

    public void setLblRemainPoints(int num) {
        pnlQuizInfo.lblRemainPoints.setText(String.valueOf(num));
    }
    public String getLblReaminPoints() { return pnlQuizInfo.lblRemainPoints.getText(); }

    //inner-class
    public class Following extends JPanel {
        private JLabel lblX;
        private JPanel pnlLineBox;

        public Following() {
            init();
            setDisplay();
            addListeners();
        }

        private void init() {
            lblX = new JLabel("X");
        }

        public String getStr() {
            JPanel pnl = (JPanel)this.getComponent(0);
            String info = "";
            try {
                Component component = pnl.getComponent(0);
                if(component instanceof JTextArea) {
                    JTextArea ta = (JTextArea)component;
                    info = ta.getText();

                }else if(component instanceof JLabel){
                    JLabel lbl = (JLabel)component;
                    info = lbl.getIcon().toString();
                }
            } catch(ArrayIndexOutOfBoundsException ae) {
            }

            return info;

        }

        private void setDisplay() {
            pnlLineBox = new JPanel();
            pnlLineBox.setBackground(Color.WHITE);
            pnlLineBox.setBorder(new LineBorder(Color.BLACK));
            pnlLineBox.setPreferredSize(new Dimension(400, 100));
            add(pnlLineBox);
            add(lblX);
        }

        private void addListeners() {

            pnlLineBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {
                        lineTa = getTa(7,30);
                        pnlLineBox.setPreferredSize(null);
                        //이미 뭐가 있으면 삭제하고 JtextArea추가
                        if(pnlLineBox.getComponentCount()>0) {
                            pnlLineBox.removeAll();
                        }
                        pnlLineBox.add(lineTa);
                        pnlLineBox.setBorder(new LineBorder(null));
                        lineTa.requestFocus();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pnlAdd.setVisible(true);
                    //원래 선택된 라인박스 테두리->검은색 , 지금 선택한 라인박스 테두리 ->하늘색
                    QuizMaker.this.pnlLineBox.setBorder(new LineBorder(Color.BLACK));
                    QuizMaker.this.pnlLineBox = pnlLineBox;
                    pnlLineBox.setBorder(new LineBorder(Color.CYAN,3));
                    flag = true;
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setHandCursor(pnlLineBox);
                }
            });

            lblX.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    pnlF.remove(Following.this);
                    pnlF.repaint();
                    testMaker.pack();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setHandCursor(lblX);
                }
            });
        }
    }

    private class Choice extends JPanel {
        private JLabel lblNum;
        private JPanel pnlLineBox;
        private JLabel lblX;
        private int num;

        public Choice(int num) {
            this.num = num;
            init();
            setDisplay();
            addListeners();
            chNum++;
            cbAnswers[chNum-1].setVisible(true);
        }

        private void init() {
            lblNum = getLbl(String.valueOf(num));
            lblX = new JLabel("X");
        }

        private String getStr() {
            JPanel pnl = (JPanel)this.getComponent(1);
            String info = "";
            try {
                Component component = pnl.getComponent(0);
                if(component instanceof JTextArea) {
                    JTextArea ta = (JTextArea)component;
                    info = ta.getText();

                } else if(component instanceof JLabel){
                    JLabel lbl = (JLabel)component;
                    info = lbl.getIcon().toString();
                }
            } catch(ArrayIndexOutOfBoundsException ae) {
//                JOptionPane.showMessageDialog(
//                        QuizMaker.this,
//                        "선지지문은 하나라도 입력되어야 합니다."
//                );
            }

            return info;

        }

        private void setDisplay() {
            pnlLineBox = new JPanel();
            pnlLineBox.setBorder(new LineBorder(Color.BLACK));
            pnlLineBox.setBackground(Color.WHITE);
            pnlLineBox.setPreferredSize(new Dimension(400, 30));
            add(lblNum);
            add(pnlLineBox);
            add(lblX);
        }
        private void addListeners() {
            lblX.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int num = (Choice.this.num) -1 ;
                    for(int i=num; i<pnlCh.getComponentCount(); i++) {
                        Choice c = (Choice)pnlCh.getComponent(i);
                        c.lblNum.setText(String.valueOf(i));
                        c.num--;
                    }
                    cbAnswers[chNum-1].setSelected(false);
                    cbAnswers[chNum-1].setVisible(false);
                    chNum--;
                    pnlCh.remove(Choice.this);
                    //이거 있어야 첫번째줄 지울때도 지워짐
                    pnlCh.repaint();

                    testMaker.pack();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setHandCursor(lblX);
                }
            });

            pnlLineBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {
                        lineTa = getTa(1,27);
                        pnlLineBox.setPreferredSize(null);
                        //이미 뭐가 있으면 삭제하고 JtextArea추가
                        if(pnlLineBox.getComponentCount()>0) {
                            pnlLineBox.removeAll();
                        }
                        pnlLineBox.add(lineTa);
                        pnlLineBox.setBorder(new LineBorder(null));
                        lineTa.requestFocus();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pnlAdd.setVisible(true);
                    //원래 선택된 라인박스 테두리->검은색 , 지금 선택한 라인박스 테두리 ->하늘색
                    QuizMaker.this.pnlLineBox.setBorder(new LineBorder(Color.BLACK));
                    QuizMaker.this.pnlLineBox = pnlLineBox;
                    pnlLineBox.setBorder(new LineBorder(Color.CYAN,3));
                    flag = false;
                }
            });
        }
    }

    public static int point = 100;

    public static void setPoint(int point) {
        QuizMaker.point = point;
    }

    public class PnlQuizInfo extends JPanel {

        public JTextArea taComment;
        private JLabel lblPoint;
        private JLabel lblRemain;
        private JTextField tfPoint;
        private JLabel lblRemainPoints;
        private int prevPoint; // 이전값 즉 testMaker.setPoint 하기 전!

        private JLabel lblAnswer;
        private JLabel lblComment;
        private JScrollPane scroll;
        private JLabel lblHashtag;
        private JLabel lblTag1;
        private JLabel lblTag2;
        private JLabel lblTag3;

        public PnlQuizInfo() {
            init();
            setDisplay();
            addListeners();
            setPreferredSize(new Dimension(380,750));
            setBorder(new EmptyBorder(30,10,10,50));
            setBackground(Color.WHITE);
        }

        public User getUser() {
            return user = testMaker.getUser();
        }

        public int getTfPoint() {
            int point = 0;

            try {
                point =  Integer.parseInt(tfPoint.getText());

            } catch (NumberFormatException e) {
            }
            return point;
        }

        public void setTag(JLabel lblTag, String strTag) {
            lblTag.setText(strTag);
        }

        public String getComment() {
            return taComment.getText();
        }

        private JLabel getLblTag(String text) {
            JLabel lbl = new JLabel(text, JLabel.CENTER);
            lbl.setBackground(Color.WHITE);
            lbl.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setPreferredSize(new Dimension(60,25));
            return lbl;
        }

        public ArrayList<Integer> getAnswer() {
            ArrayList<Integer> answerList = new ArrayList<>();

            for(int i = 0; i < cbAnswers.length; i++) {
                if(cbAnswers[i].isSelected()) {
                    answerList.add(Integer.parseInt(cbAnswers[i].getText()));
                }
            }
            return answerList;
        }

        public void hashOverlapCond() {
            if(lblTag1.getText().equals(lblTag2.getText()) && !lblTag1.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        this,
                        "해시태그는 중복으로 넣을 수 없습니다."
                );
                lblTag2.setText("");
            }
            if(lblTag1.getText().equals(lblTag3.getText()) && !lblTag1.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        this,
                        "해시태그는 중복으로 넣을 수 없습니다."
                );
                lblTag3.setText("");
            }
            if(!lblTag2.getText().trim().equals("#")) {
                if (lblTag2.getText().equals(lblTag3.getText()) && !lblTag2.getText().equals("")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "해시태그는 중복으로 넣을 수 없습니다."
                    );
                    lblTag3.setText("");
                }
            }
        }

        private void setTagBlank(JLabel tag, String str) {
            if(tag.getText().equals(str)) {
                tag.setText("");
            }
        }

        public List<String> getHashTag() {
            List<String> hashTagList = new ArrayList<>();

            setTagBlank(lblTag1, "#태그1");
            setTagBlank(lblTag2, "#태그2");
            setTagBlank(lblTag3, "#태그3");

            hashTagList.add(lblTag1.getText());
            hashTagList.add(lblTag2.getText());
            hashTagList.add(lblTag3.getText());

            return hashTagList;
        }

        private void init() {
            lblPoint = getLbl("배점");
            lblRemain = getLbl("남은 점수");
            lblRemain.setHorizontalAlignment(JLabel.RIGHT);

            tfPoint = new JTextField(4);
            tfPoint.setFont(normalFont);
            tfPoint.setBorder(new LineBorder(Color.GRAY));
            lblRemainPoints = new JLabel(String.valueOf(point), JLabel.RIGHT);
            lblRemainPoints.setVerticalAlignment(JLabel.BOTTOM);
            lblRemainPoints.setBorder(new EmptyBorder(5,0,10,10));
            lblRemainPoints.setBackground(Color.WHITE);

            // 배점 0이 아니면 가지고 오게
            if(getQuiz(quizNum).getPoint() != 0) {
                point -= getQuiz(quizNum).getPoint();
                tfPoint.setText(String.valueOf(getQuiz(quizNum).getPoint()));
                // 1번부터 열리니깐 1번만 확인 다른거는 클릭하는 순간 어차피 바뀜 (토글버튼 리스너에서)
                try {
                    prevPoint=(getQuiz(1).getPoint());
                } catch (NullPointerException e) {
                    prevPoint=0;
                }
                testMaker.setRemainPoints(point);
            }
//            lblRemainPoints.setText(String.valueOf(point));

            lblAnswer = getLbl("답");
            lblComment = getLbl("해설");
            taComment = getTa(13,20);
            scroll = new JScrollPane(taComment);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            //null값이 아니면 문제의 해설을 불러옴
            if(getQuiz(quizNum).getExplain() != null){
                taComment.setText(getQuiz(quizNum).getExplain());
            }

            lblHashtag = getLbl("해시태그");
            lblTag1 = getLblTag("#태그1");
            lblTag2 = getLblTag("#태그2");
            lblTag3 = getLblTag("#태그3");

            //null값이 아니면 태그를 불러옴
            List<String> strs = getQuiz(quizNum).getHashTag();
            if(strs != null) {

                if(!(strs.get(0).equals("#태그1"))) {
                    lblTag1.setText(strs.get(0));
                    lblTag1.setForeground(Color.BLACK);
                }
                if(!(strs.get(1).equals("#태그2"))) {
                    lblTag2.setText(strs.get(1));
                    lblTag2.setForeground(Color.BLACK);
                }
                if(!(strs.get(2).equals("#태그3"))) {
                    lblTag3.setText(strs.get(2));
                    lblTag3.setForeground(Color.BLACK);
                }
            }
        }

        private void setDisplay() {
            JPanel pnlPoint = new JPanel(new GridLayout(2,0));
            pnlPoint.add(lblPoint);
            pnlPoint.add(lblRemain);
            pnlPoint.add(tfPoint);
            pnlPoint.add(lblRemainPoints);
            pnlPoint.setBackground(Color.WHITE);
            pnlPoint.setBorder(new EmptyBorder(0,0,40,0));

            JPanel pnlCheckBox = new JPanel(new GridLayout(1,0));
            cbAnswers = new JCheckBox[5];
            for(int i=0; i<5; i++) {
                cbAnswers[i] = new JCheckBox(String.valueOf(i + 1));
                cbAnswers[i].setBackground(Color.WHITE);
                cbAnswers[i].setFont(normalFont);
                pnlCheckBox.add(cbAnswers[i]);
            }
            for(int i = 1; i < 5; i++) {
                cbAnswers[i].setVisible(false);
            }
            //null값 아니면 정답체크해놓기
            if(getQuiz(quizNum).getAnswers() != null){
                List<Integer> answer = getQuiz(quizNum).getAnswers();
                for(int num : answer){
                    cbAnswers[num-1].setSelected(true);
                }
            }
            pnlCheckBox.setBackground(Color.WHITE);
            pnlCheckBox.setBorder(new LineBorder(Color.GRAY, 1));

            JPanel pnlAnswer = new JPanel(new BorderLayout());
            pnlAnswer.add(lblAnswer, BorderLayout.NORTH);
            pnlAnswer.add(pnlCheckBox, BorderLayout.CENTER);
            pnlAnswer.setBackground(Color.WHITE);
            pnlAnswer.setBorder(new EmptyBorder(0,0,40,0));

            JPanel pnlComment = new JPanel(new BorderLayout());
            pnlComment.add(lblComment, BorderLayout.NORTH);
            pnlComment.add(scroll, BorderLayout.CENTER);		//테두리에 경계가 있다. 길어지면 스크롤이 생긴다.
            pnlComment.setBackground(Color.WHITE);
            pnlComment.setBorder(new EmptyBorder(40,0,40,0));

            JPanel pnlTags = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlTags.add(lblTag1);
            pnlTags.add(lblTag2);
            pnlTags.add(lblTag3);
            pnlTags.setBackground(Color.WHITE);

            JPanel pnlHash = new JPanel(new GridLayout(0,1));
            pnlHash.add(lblHashtag);
            pnlHash.add(pnlTags);
            pnlHash.setBackground(Color.WHITE);
            pnlHash.setBorder(new EmptyBorder(20,0,0,0));

            JPanel pnlNorth = new JPanel(new GridLayout(0,1));
            pnlNorth.add(pnlPoint);
            pnlNorth.add(pnlAnswer);

            JPanel pnlMain = new JPanel(new BorderLayout());
            pnlMain.add(pnlNorth, BorderLayout.NORTH);
            pnlMain.add(pnlComment,BorderLayout.CENTER);
            pnlMain.add(pnlHash,BorderLayout.SOUTH);
            pnlMain.setBackground(Color.WHITE);
            pnlMain.setBorder(new EmptyBorder(20,0,20,0));

            add(pnlMain, BorderLayout.CENTER);
        }

        private void addListeners() {

            MouseListener mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getSource() == lblTag1) {
                        new HashSearch(PnlQuizInfo.this, lblTag1);
                    }
                    if(e.getSource() == lblTag2) {
                        new HashSearch(PnlQuizInfo.this, lblTag2);
                    }
                    if(e.getSource() == lblTag3) {
                        new HashSearch(PnlQuizInfo.this, lblTag3);
                    }
                }

            };
            lblTag1.addMouseListener(mouseListener);
            lblTag2.addMouseListener(mouseListener);
            lblTag3.addMouseListener(mouseListener);


            tfPoint.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updatePoint();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    removePoint();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    //updatePoint();
                }
            });

        }

        private void removePoint() {
            if (tfPoint.getText().equals("")) {
                point += prevPoint;
                lblRemainPoints.setText(Integer.toString(point));
                prevPoint = 0;
            }
        }

        private void updatePoint() {
            String tfStr = tfPoint.getText();

            // tfPoint가 비어있을 때
            if (tfStr.equals("")) {
                // 원래 있던 Point로 바꾸기
                lblRemainPoints.setText(Integer.toString(point));
            } else {
                try {
                    int tfInt = Integer.parseInt(tfStr);
                    point += prevPoint;
                    point -= tfInt;
                    prevPoint = tfInt;

                    if (point < 0) {
                        JOptionPane.showMessageDialog(
                                QuizMaker.this,
                                "점수가 0미만이 되었습니다."
                        );
                        point += prevPoint;
                        lblRemainPoints.setText(String.valueOf(point));
                        testMaker.setRemainPoints(point);
                        SwingUtilities.invokeLater(() -> tfPoint.setText(""));
                        point -= prevPoint;
                        return;
                    }
                    lblRemainPoints.setText(String.valueOf(point));
                    testMaker.setRemainPoints(point);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            QuizMaker.this,
                            "배점은 숫자만 가능합니다."
                    );
                    SwingUtilities.invokeLater(() -> tfPoint.setText(""));
                }
            }
        }
    }
}