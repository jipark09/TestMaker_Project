import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoadPreview extends JFrame {
    private JLabel lblBack;

    private JLabel lblTag1;
    private JLabel lblTag2;
    private JLabel lblTag3;

    private JLabel lblBefore;
    private JLabel lblAfter;
    private JButton btnLoad;

    private JLabel lblPage;
    private JLabel lbl;
    private JLabel lblTotalPage;

    private MyTest owner;
    private Subject subject;
    private int quizIdx;
    private QuizMaker quizMaker;


    //총문제수
    private int total = 0;
    //퀴즈의 과목별 인덱스
    private int quizIdxinSub;


    public LoadPreview(MyTest owner, int quizIdx) {
        this.owner = owner;
        this.quizIdx = quizIdx;

        init();
        setDisplay();
        addListener();
        showFrame();
    }

    public Subject getSubject() {
        return subject;
    }

    public int getQuizIdxinSub() {
        return quizIdxinSub;
    }

    public List<Subject> getSubjects() {
        return owner.getTest().getSubjects();
    }
    public List<Quiz> getQuizzes(int subjectIdx) {
        return owner.getTest().getQuizzes(subjectIdx);
    }
    private void init() {

        subject = getSubjects().get(0);
        total = getQuizzes(0).size();
        quizIdxinSub = quizIdx;

        //과목 수가 1보다 클때
        if(getSubjects().size()!=1) {
            for(int i=1; i<getSubjects().size(); i++) {
                if(quizIdx >= total) {
                    subject = getSubjects().get(i);
                    quizIdxinSub -= total;
                }
                total += owner.getTest().getQuizzes(i).size();
            }
        }


        lblBack = Util.setImageSize("뒤로.jpg",30,30);
        lblBack.setBackground(Color.WHITE);

        lblTag1 = setLblTag("#태그1");
        lblTag2 = setLblTag("#태그2");
        lblTag3 = setLblTag("#태그3");

        lblBefore = Util.setImageSize("before.jpg",30,30);
        lblBefore.setBackground(Color.WHITE);

        lblAfter = Util.setImageSize("after.jpg",30,30);
        lblAfter.setBackground(Color.WHITE);

        btnLoad = new JButton("불러오기");
        btnLoad.setBackground(new Color(157, 195, 230));
        btnLoad.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btnLoad.setForeground(Color.BLACK);
        btnLoad.setBorder(new LineBorder(Color.WHITE));
        btnLoad.setPreferredSize(new Dimension(120,40));

        lblPage = new JLabel(String.valueOf(quizIdx+1));
        lblPage.setBackground(Color.WHITE);

        lbl = new JLabel(" / ");
        lbl.setBackground(Color.WHITE);

        lblTotalPage = new JLabel(String.valueOf(total));
        lblTotalPage.setBackground(Color.WHITE);
    }

    private void setDisplay() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(5,20,20,20));

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);

        JPanel pnlTNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTNorth.setBackground(Color.WHITE);
        pnlTNorth.add(lblBack);
        pnlTNorth.setBorder(new EmptyBorder(0,0,10,0));

        JPanel pnlTCenter = new JPanel(new BorderLayout());
        pnlTCenter.setBackground(Color.WHITE);

        JPanel pnlTag = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTag.setBackground(Color.WHITE);
        pnlTag.add(lblTag1);
        pnlTag.add(lblTag2);
        pnlTag.add(lblTag3);

        pnlTCenter.add(new ExamPanel(subject, quizIdxinSub), BorderLayout.CENTER);
        pnlTCenter.add(pnlTag, BorderLayout.SOUTH);

        pnlTop.add(pnlTNorth, BorderLayout.NORTH);
        pnlTop.add(pnlTCenter, BorderLayout.CENTER);

        pnlMain.add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(new EmptyBorder(10,0,20,0));
        pnlCenter.setBackground(Color.WHITE);

        JPanel pnlBefore = new JPanel();
        pnlBefore.setBackground(Color.WHITE);
        pnlBefore.add(lblBefore, FlowLayout.LEFT);

        JPanel pnlLoad = new JPanel();
        pnlLoad.setBackground(Color.WHITE);
        pnlLoad.add(btnLoad, new FlowLayout(FlowLayout.CENTER));

        JPanel pnlAfter = new JPanel();
        pnlAfter.setBackground(Color.WHITE);
        pnlAfter.add(lblAfter, new FlowLayout(FlowLayout.RIGHT));

        pnlCenter.add(pnlBefore, BorderLayout.WEST);
        pnlCenter.add(pnlLoad, BorderLayout.CENTER);
        pnlCenter.add(pnlAfter, BorderLayout.EAST);

        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(lblPage);
        pnlBottom.add(lbl);
        pnlBottom.add(lblTotalPage);

        pnlMain.add(pnlBottom, BorderLayout.SOUTH);

        add(pnlMain);
    }

    private void addListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                owner.setVisible(true);
            }
        });

        MouseListener mListener = new MouseAdapter()  {

            @Override
            public void mousePressed(MouseEvent e) {
                JLabel lbl = (JLabel) e.getSource();
                if(lbl == lblBack) {
                    dispose();
                    owner.setVisible(true);
                } else if (lbl == lblAfter) {
                    if(quizIdx+1 != total) {
                        owner.getPreview(quizIdx +1);
                        dispose();
                    }else {
                        owner.getPreview(0);
                        dispose();
                    }

                } else if (lbl == lblBefore) {
                    if(quizIdx-1 != -1) {
                        owner.getPreview(quizIdx -1);
                        dispose();
                    }else {
                        owner.getPreview(total-1);
                        dispose();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                JLabel lbl =(JLabel)e.getSource();
                if(e.getSource() == lbl) {
                    lbl.setCursor(handCursor);
                }
            }
        };
        lblBack.addMouseListener(mListener);
        lblAfter.addMouseListener(mListener);
        lblBefore.addMouseListener(mListener);

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizMaker qm = owner.getQuizMaker();
                Quiz quiz = subject.getQuizzes().get(quizIdxinSub);

                qm.getTaQuiz().setText(quiz.getTitle());
                qm.getTaQuiz().setForeground(Color.BLACK);

                ArrayList<String> following = new ArrayList<>();
                following.addAll(quiz.getFollowing());

                qm.getPnlF().removeAll();
                qm.setFollowingList(following);
                if (following != null) {
                    for (int i = 0; i < following.size(); i++) {
                        qm.makeF(following.get(i));
                    }
                }

                ArrayList<String> texts = new ArrayList<>();
                texts.addAll(quiz.getTexts());

                qm.setChNum(0);
                qm.getPnlCh().removeAll();
                qm.setTextsList(texts);

                for(int i=0; i<texts.size(); i++) {
                    qm.makeCh(texts.get(i),i);
                }
                qm.setChNum(texts.size());

                for(JCheckBox cb : qm.getCbAnswers()) {
                    cb.setSelected(false);
                }
                for(Integer answer : quiz.getAnswers()) {
                    qm.getCbAnswers()[answer].setSelected(true);
                }

                qm.getTaComment().setText(quiz.getExplain());

                qm.setHashTag(quiz.getHashTag());

                qm.repaint();
                qm.revalidate();

                dispose();
                owner.getQuizMaker().getTestMaker().setVisible(true);

            }
        });
    }

    public void setTag(ArrayList<String> tags) {

        if(tags != null) {

            if(!(tags.get(0).equals("#태그1"))) {
                lblTag1.setText(tags.get(0));
                lblTag1.setForeground(Color.BLACK);
            }
            if(!(tags.get(1).equals("#태그2"))) {
                lblTag2.setText(tags.get(1));
                lblTag2.setForeground(Color.BLACK);
            }
            if(!(tags.get(2).equals("#태그3"))) {
                lblTag3.setText(tags.get(2));
                lblTag3.setForeground(Color.BLACK);
            }
        }

    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private JLabel setLblTag(String text) {
        JLabel lbl = new JLabel(text, JLabel.CENTER);
        lbl.setBackground(Color.WHITE);
        lbl.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setPreferredSize(new Dimension(60,25));
        return lbl;
    }

    private class ExamPanel extends JPanel {
        public ExamPanel(Subject subject, int index) {
            try {
                JPanel quizTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JPanel imgPanel = new JPanel(new GridLayout(0,1));
                JPanel panel = new JPanel(new BorderLayout());
                JPanel numPanel = new JPanel(new GridLayout(0,1, 0, 20));

                setLayout(new BorderLayout());
                JLabel quizTitleLabel = new CustomTextLabel(index + 1 + "번. " + subject.getQuizzes().get(index).getTitle(), JLabel.LEFT);
                //for 선지있을때
                if(subject.getQuizzes().get(index).getFollowing().size()!=0) {
                    for(int i = 0; i < subject.getQuizzes().get(index).getFollowing().size(); i++) {
                        JLabel printLabel = new JLabel();
                        if(subject.getQuizzes().get(index).getFollowing().get(i).contains(".png") || subject.getQuizzes().get(index).getFollowing().get(i).contains(".jpg")) {
                            Image img = getToolkit().getImage(subject.getQuizzes().get(index).getFollowing().get(0))
                                    .getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                            ImageIcon icon = new ImageIcon(img); // 사이즈 조정
                            printLabel = new JLabel(icon);
                        } else {
                            printLabel.setText(subject.getQuizzes().get(index).getFollowing().get(i));
                        }
                        imgPanel.add(printLabel);
                    }
                }


//                if(subject.getQuizzes().get(index).getFollowing().size()!=0) {
//                    Image img = getToolkit().getImage(subject.getQuizzes().get(index).getFollowing().get(0))
//                            .getScaledInstance(400, 400, Image.SCALE_SMOOTH);
//                    ImageIcon icon = new ImageIcon(img); // 사이즈 조정
//                    JLabel printLabel = new JLabel(icon);
//
//                    imgPanel.add(printLabel);
//                }
                //subject.getQuizzes().get(0).getFollowing().get(0)

                JLabel[] numLabel = new CustomTextLabel[subject.getQuizzes().get(index).getTexts().size()];
                JPanel[] numButtonPanel = new NumButtonPanel[subject.getQuizzes().get(index).getTexts().size()];
                for (int i = 0; i < subject.getQuizzes().get(index).getTexts().size(); i++) {
                    numButtonPanel[i] = new NumButtonPanel(subject.getQuizzes().get(index), i);
                    numLabel[i] = new CustomTextLabel(subject.getQuizzes().get(index).getTexts().get(i));
                }

                quizTitlePanel.setOpaque(false);
                imgPanel.setOpaque(false);
                panel.setOpaque(false);
                numPanel.setOpaque(false);

                quizTitlePanel.add(quizTitleLabel);

                JPanel topPanel = new JPanel(new BorderLayout());
                topPanel.add(quizTitlePanel, BorderLayout.WEST);
                topPanel.setOpaque(false);


                panel.add(topPanel, BorderLayout.NORTH);
                panel.add(imgPanel);

                add(panel);
                add(numPanel, BorderLayout.SOUTH);

                for (int i = 0; i < numLabel.length; i++) {
                    JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                    panel1.add(numButtonPanel[i]);
                    panel1.add(numLabel[i]);
                    panel1.setOpaque(false);
                    numPanel.add(panel1);
                }

                numPanel.setPreferredSize(new Dimension(500, 200));
                setOpaque(false);
                setBorder(new EmptyBorder(0,0,20,0));

            } catch (NullPointerException e) {

            }

        }
    }
    private class NumButtonPanel extends JPanel {
        public NumButtonPanel(Quiz quiz, int i) {
            setLayout(null);
            setPreferredSize(new Dimension(20,20));
            Image img = getToolkit().getImage("img/"+(i+1)+".png").getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            Image img2 = getToolkit().getImage("img/check.png").getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            JLabel button = new JLabel(new ImageIcon(img));
            button.setBounds(0,0,20,20);
            JLabel check = new JLabel(new ImageIcon(img2));
            check.setBounds(0,0,20,20);
            setOpaque(false);
            add(button);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    class CustomTextLabel extends JLabel {
        public CustomTextLabel(String text) {
            setText(text);
            setFontSize(15);
            setForeground(Color.BLACK);
        }
        public CustomTextLabel(String text, int alignment) {
            this(text);
            setHorizontalAlignment(alignment);
        }

        public void setFontSize(int size) {
            super.setFont(new Font("휴먼엑스포", Font.PLAIN, size));
        }
    }
}