import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private LoginForm owner;
    private UserDataSet users;

    private JLabel lblInfo;
    private JLabel lblLogo;
    private JLabel lbl;
    private JLabel lblNo;
    private JLabel lblMyInformation;
    private JLabel lblLogout;
    private JButton btnNewQuiz;
    private JButton btnLookMyQuiz;
    private JButton btnOpenFile;

    public MainFrame(LoginForm owner) {
        this.owner = owner;
        users = owner.getUsers();

        init();
        setDisplay();
        addListener();
        showFrame();
    }

    @Override
    public LoginForm getOwner() {
        return owner;
    }

    public String getLblNo() {
        return lblNo.getText();
    }

    private void init() {
        lblInfo = Util.setImageSize("img/info.png", 30, 30);

        lblLogo = Util.setImageSize("img/1163624.png", 80, 80);
        lbl = new JLabel(" | ");
        lbl.setForeground(Color.GRAY);

        // 새 문제 출제하기
        btnNewQuiz = new JButton("새 문제 출제하기");
        btnNewQuiz.setBackground(new Color(157, 195, 230));
        btnNewQuiz.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btnNewQuiz.setForeground(Color.WHITE);
        btnNewQuiz.setBorder(new LineBorder(Color.WHITE));
        btnNewQuiz.setPreferredSize(new Dimension(370, 60));

        // 파일 불러오기
        btnOpenFile = new JButton("파일 불러오기");
        btnOpenFile.setBackground(new Color(157, 195, 230));
        btnOpenFile.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btnOpenFile.setForeground(Color.WHITE);
        btnOpenFile.setBorder(new LineBorder(Color.WHITE));
        btnOpenFile.setPreferredSize(new Dimension(180, 100));

        // 내 문제 보기
        btnLookMyQuiz = new JButton("내 문제 보기");
        btnLookMyQuiz.setBackground(new Color(157, 195, 230));
        btnLookMyQuiz.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btnLookMyQuiz.setForeground(Color.WHITE);
        btnLookMyQuiz.setBorder(new LineBorder(Color.WHITE));
        btnLookMyQuiz.setPreferredSize(new Dimension(180, 100));

        lblMyInformation = new JLabel("내 정보");
        lblMyInformation.setBackground(Color.WHITE);
        lblMyInformation.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        lblMyInformation.setForeground(Color.GRAY);

        lblLogout = new JLabel("로그아웃");
        lblLogout.setBackground(Color.WHITE);
        lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        lblLogout.setForeground(Color.GRAY);
    }

    private void setDisplay() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        // 정보
        JPanel pnlHigh = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlHigh.add(lblInfo);
        pnlHigh.setBackground(Color.WHITE);

        // 구름
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(Color.WHITE);
        pnlNorth.add(lblLogo, BorderLayout.NORTH);
        lblLogo.setBorder(new EmptyBorder(0, 0, 20, 0));
        lblNo = new JLabel(owner.getTfId());
        lblNo.setForeground(Color.WHITE);
        pnlNorth.add(lblNo);

        JPanel pnl = new JPanel();
        pnl.add(btnNewQuiz);
        pnl.setBackground(Color.WHITE);
        pnlNorth.add(pnl, BorderLayout.CENTER);

        // 파란네모
        JPanel pnlCenter = new JPanel();
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(btnOpenFile);
        pnlCenter.add(btnLookMyQuiz);

        // 회색
        JPanel pnlBottom = new JPanel();
        pnlBottom.setBorder(new EmptyBorder(20,0,0,0));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(lblMyInformation);
        pnlBottom.add(lbl);
        pnlBottom.add(lblLogout);

        pnlMain.add(pnlNorth, BorderLayout.NORTH);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        pnlMain.setBorder(new EmptyBorder(20,30,20,30));

        JPanel pnlTotal = new JPanel(new BorderLayout());
        pnlTotal.add(pnlHigh, BorderLayout.NORTH);
        pnlTotal.add(pnlMain, BorderLayout.CENTER);

        add(pnlTotal);
    }

    private void addListener() {

        MouseListener mListener = new MouseAdapter() {
            TitledBorder setTitled = new TitledBorder(new LineBorder(new Color(80, 126, 176), 3));
            Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
            @Override
            public void mouseEntered(MouseEvent me) {	// 마우스 커서가 들어갈때
                if(btnNewQuiz == me.getSource()) {
                    btnNewQuiz.setBorder(setTitled);
                    btnNewQuiz.setCursor(cursor);
                }
                if(btnOpenFile == me.getSource()) {
                    btnOpenFile.setBorder(setTitled);
                    btnOpenFile.setCursor(cursor);
                }
                if(btnLookMyQuiz == me.getSource()) {
                    btnLookMyQuiz.setBorder(setTitled);
                    btnLookMyQuiz.setCursor(cursor);
                }
                if(lblMyInformation == me.getSource()) {
                    lblMyInformation.setCursor(cursor);
                }
                if(lblLogout == me.getSource()) {
                    lblLogout.setCursor(cursor);
                }
                if(lblInfo == me.getSource()) {
                    lblInfo.setCursor(cursor);
                }
            }
            @Override
            public void mouseExited(MouseEvent me) {			//마우스가 나오면
                btnNewQuiz.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 0)));
                btnOpenFile.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 0)));
                btnLookMyQuiz.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 0)));
            }
            @Override
            public void mousePressed(MouseEvent e) { //눌러졌다, 빨리 실행하고 싶은건 프레스드에 집어넣는다

                if(lblLogout == e.getSource()) {
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "로그아웃 하시겠습니까?",
                            "로그아웃",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if(choice == JOptionPane.YES_OPTION) {
                        dispose();
                        owner.dataSave();
                        new LoginForm();
                    }
                }

                if(lblMyInformation == e.getSource()) {
                    UserInformationForm informationForm = new UserInformationForm(owner, MainFrame.this);
                    informationForm.setTaCheck(users.getUser(lblNo.getText()).toString());
                    setVisible(false);
                    informationForm.setVisible(true);
                }

                if(lblInfo == e.getSource()) {
                    new Information(MainFrame.this);
                }
            }
        };
        btnNewQuiz.addMouseListener(mListener);
        btnOpenFile.addMouseListener(mListener);
        btnLookMyQuiz.addMouseListener(mListener);
        lblMyInformation.addMouseListener(mListener);
        lblLogout.addMouseListener(mListener);
        lblInfo.addMouseListener(mListener);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btnNewQuiz) {
                    new InputInfo(MainFrame.this);
                }else if(e.getSource() == btnLookMyQuiz) {
                    new MyTest(MainFrame.this);
                    setVisible(false);
                }else {
                    //파일불러오기 버튼눌렀을 때
                    JFileChooser chooser = new JFileChooser("."); //현재폴더위치를 열어라는 의미
                    chooser.setFileFilter(new FileNameExtensionFilter("시험 파일 (*.dat)", "dat"));
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int choice = chooser.showOpenDialog(MainFrame.this); //부모창이 없으면 null

                    if(choice == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        //읽어서 안에 test 객체 있는지 확인하고 유저의 테스트리스트에 담기
                        User user = users.getUser(lblNo.getText());

                        FileInputStream fis = null; 	//어디서 읽어오는가?
                        ObjectInputStream ois = null;	//오브젝트를 읽는다.

                        try {
                            fis = new FileInputStream(file);
                            ois = new ObjectInputStream(fis);

                            Test test = (Test)(ois.readObject());
                            //user의 테스트리스트에 추가
                            user.getTestList().addTest(test);
                            //user의 해시리스트에 추가
                            for(int i=0; i<test.getSubjects().size(); i++) {
                                for(Quiz quiz : test.getQuizzes(i)) {
                                    if(quiz.getHashTag().size()>0) {
                                        ArrayList<String> hash = new ArrayList<>(quiz.getHashTag());
                                        for(String hashStr : hash) {
                                            user.getHashList().add(new Hash(hashStr));
                                        }
                                    }
                                }
                            }

                        } catch(IOException ioe) {
                            ioe.printStackTrace();
                        } catch(ClassNotFoundException cnfe) {
                            cnfe.printStackTrace();
                        } finally {
                            Util.closeAll(ois, fis);
                        }

                    }
                }
            }
        };
        btnNewQuiz.addActionListener(actionListener);
        btnLookMyQuiz.addActionListener(actionListener);
        btnOpenFile.addActionListener(actionListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
            int choice = JOptionPane.showConfirmDialog(
                    MainFrame.this,
                    "종료하시겠습니까?",
                    "종료",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if(choice == JOptionPane.YES_OPTION) {
                owner.dataSave();
                System.exit(0);
            }
            }
        });
    }

    private void showFrame() {
        setTitle("마이페이지");
        pack();
        //setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}