import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class LoginForm extends JFrame {

    private UserDataSet users;
    private JLabel lblLogo;
    private JTextField tfId;
    private JPasswordField pfPw;
    private JButton btnLogin;
    private JLabel lblFind;
    private JLabel lblJoin;
    private JLabel lblFileLoad;

    private JPanel pnlIP;

    private File file = new File("users.user");

    public LoginForm() {
        users = new UserDataSet();
        dataLoad();
        init();
        setDisplay();
        addListeners();
        showFrame();
    }

    public UserDataSet getUsers() {
        return users;
    }

    public String getTfId() {
        return tfId.getText();
    }

    public void dataLoad() {
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(file));

            users.setUsers((ArrayList<User>)(ois.readObject()));

        } catch (FileNotFoundException fe) {
            users = new UserDataSet();

            if(file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {}
        catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "데이터 파일을 불러오던 도중 에러가 발생했습니다. 프로그램을 종료합니다.",
                    "에러",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);

        } finally {
            Util.closeAll();
        }
    }

    private void init() {
        // 사이즈 통합
        Font lblF = new Font("맑은 고딕", Font.PLAIN, 10);
        Dimension tfSize = new Dimension(250, 35);

        lblLogo = new JLabel("흰둥이");

        tfId = new JTextField("  아이디");
        tfId.setPreferredSize(tfSize);
        tfId.setBorder(new LineBorder(Color.LIGHT_GRAY));
        tfId.setForeground(Color.LIGHT_GRAY);

        pfPw = new JPasswordField();
        pfPw.setEchoChar((char)0);
        pfPw.setText("  비밀번호");
        pfPw.setPreferredSize(tfSize);
        pfPw.setBorder(new LineBorder(Color.LIGHT_GRAY));
        pfPw.setForeground(Color.LIGHT_GRAY);

        lblFind = new JLabel("아이디/비밀번호 찾기");
        lblFind.setFont(lblF);
        lblJoin = new JLabel("회원가입");
        lblJoin.setFont(lblF);
        lblFileLoad = new JLabel("users 데이터 불러오기");
        lblFileLoad.setFont(lblF);

        btnLogin = new JButton("로그인");
        btnLogin.setBackground(Color.decode("#EBF7FF"));
        btnLogin.setPreferredSize(new Dimension(250 , 50));

    }

    private void setDisplay() {

        JPanel pnlNorth = new JPanel(new GridLayout(0, 1));
        lblLogo =  Util.setImageSize("img/1163624.png", 200 , 200);
        pnlNorth.add(lblLogo);
        pnlNorth.setBorder(new EmptyBorder(15, 20, 20, 20));
        pnlNorth.setBackground(Color.WHITE);

        JPanel pnlMain = new JPanel(new GridLayout(0 ,1));
        pnlMain.setBackground(Color.WHITE);

        pnlIP = new JPanel(new GridLayout(0, 1 ));
        pnlIP.add(tfId);
        pnlIP.add(pfPw);
        pnlIP.setBackground(Color.WHITE);
        pnlIP.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel pnlLogin = new JPanel();
        pnlLogin.add(btnLogin);
        pnlLogin.setBackground(Color.WHITE);

        pnlMain.add(pnlIP);
        pnlMain.add(pnlLogin);
        pnlMain.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel pnlSouth = new JPanel();
        pnlSouth.add(lblFind);
        pnlSouth.add(new JLabel(" | "));
        pnlSouth.add(lblJoin);
        pnlSouth.add(new JLabel(" | "));
        pnlSouth.add(lblFileLoad);
        pnlSouth.setBackground(Color.WHITE);

        add(pnlNorth, BorderLayout.NORTH);
        add(pnlMain, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);

    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private void addListeners() {
        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                if(e.getSource() == tfId) {
                    if(tfId.getText().equals("  아이디")) {
                        tfId.setText("");
                    }
                    tfId.setForeground(Color.BLACK);
                }
                if(e.getSource() == pfPw) {
                    if(pfPw.getText().equals("  비밀번호")) {
                        pfPw.setText("");
                    }
                    pfPw.setEchoChar('*');
                    pfPw.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(e.getSource() == tfId) {

                    if (tfId.getText().trim().equals("")) {
                        tfId.setText("  아이디");
                        tfId.setForeground(Color.LIGHT_GRAY);
                    }
                } else {
                    if(pfPw.getText().trim().equals("")) {
                        pfPw.setEchoChar((char)0);
                        pfPw.setText("  비밀번호");
                        pfPw.setForeground(Color.LIGHT_GRAY);
                    }
                }
            }
        };
        tfId.addFocusListener(focusListener);
        pfPw.addFocusListener(focusListener);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == lblJoin) {
                    setVisible(false);
                    new JoinForm(LoginForm.this);
                    tfId.setText("");
                    pfPw.setText("");
                }
                if(e.getSource() == lblFind) {
                    setVisible(false);
                    new FindIdPw(LoginForm.this);
                    tfId.setText("");
                    pfPw.setText("");
                }
                if(e.getSource() == lblFileLoad) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("users", "user"));

                    int choice = fileChooser.showOpenDialog(null);
                    if(choice != JFileChooser.APPROVE_OPTION) {
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "파일을 선택하지 않았습니다."
                        );
                        return;
                    }
                    File selectedFile = fileChooser.getSelectedFile();
                    File ioProjectDir = new File("");
                    String extension = getFileExtension(selectedFile);

                    if (!extension.equals("user")) {
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "파일 확장자가 맞지 않습니다. user 데이터파일을 올려주세요",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {

                        FileInputStream fis = null;
                        FileOutputStream fos = null;

                        try {
                            fis = new FileInputStream(selectedFile);
                            fos = new FileOutputStream(ioProjectDir.getAbsolutePath() + "/" + selectedFile.getName());

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }

                        } catch (IOException e2) {
                            e2.printStackTrace();
                        } finally {
                            Util.closeAll(fis, fos);
                        }
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "데이터를 불러왔습니다. 다시 재시작 해주세요."
                        );
                        System.exit(0);
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(e.getSource() == btnLogin) {
                    btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if(e.getSource() == lblJoin) {
                    lblJoin.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if(e.getSource() == lblFind) {
                    lblFind.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if(e.getSource() == lblFileLoad) {
                    lblFileLoad.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        };
        lblJoin.addMouseListener(mouseListener);
        lblFind.addMouseListener(mouseListener);
        btnLogin.addMouseListener(mouseListener);
        lblFileLoad.addMouseListener(mouseListener);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 아이디칸이 비었을 경우
                if (tfId.getText().equals("  아이디")) {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "아이디를 입력하세요.",
                            "로그인폼",
                            JOptionPane.WARNING_MESSAGE);

                    // 존재하는 아이디일 경우
                } else if (users.contains(new User(tfId.getText()))) {

                    // 비밀번호칸이 비었을 경우
                    if(String.valueOf(pfPw.getPassword()).equals("  비밀번호")) {
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "비밀번호를 입력하세요.",
                                "로그인폼",
                                JOptionPane.WARNING_MESSAGE);

                        // 비밀번호가 일치하지 않을 경우
                    } else if (!users.getUser(tfId.getText()).getPw().equals(String.valueOf(pfPw.getPassword()))) {
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "비밀번호가 일치하지 않습니다.");

                        // 다 완료될 경우
                    } else {
                        dataSave();
                        MainFrame mainFrame = new MainFrame(LoginForm.this);
                        setVisible(false);
                        mainFrame.setVisible(true);
                        tfId.setText("");
                        pfPw.setText("");
                    }
                    // 존재하지 않는 Id일 경우
                } else {
                    JOptionPane.showMessageDialog(
                            LoginForm.this,
                            "존재하지 않는 Id입니다."

                    );
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
    public void dataSave() {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));

            oos.writeObject(users.getUsers());
            oos.flush();
            oos.reset();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "데이터 저장중 에러가 발생했습니다. 잠시후 다시 시도하세요.",
                    "에러",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        } finally {
            Util.closeAll(oos);
        }
    }

    private void showFrame() {
        setTitle("Login");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}