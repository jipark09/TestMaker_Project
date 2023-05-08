import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserInformationForm extends JDialog {
    private LoginForm owner;
    private MainFrame main;
    private UserDataSet users;

    private JTextArea taCheck;
    private JButton btnBack;
    private JButton btnWithdraw;

    public UserInformationForm(LoginForm owner, MainFrame main) {
        super(owner, "Information", false);
        this.owner = owner;
        this.main = main;
        users = owner.getUsers();

        init();
        setDisplay();
        addListeners();
        showFrame();

    }
    private void init() {
        Dimension btnsize = new Dimension(100, 25);

        taCheck = new JTextArea(10, 30);
        taCheck.setEditable(false);

        btnBack = new JButton("뒤로가기");
        btnBack.setPreferredSize(btnsize);

        btnWithdraw = new JButton("회원탈퇴");
        btnWithdraw.setPreferredSize(btnsize);
    }
    private void setDisplay() {

        LineBorder lBorder = new LineBorder(Color.GRAY, 1);
        TitledBorder border = new TitledBorder(lBorder, "정보 확인");
        taCheck.setBorder(border);

        JPanel pnlSouth = new JPanel();
        pnlSouth.add(btnBack);
        pnlSouth.add(btnWithdraw);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.add(new JScrollPane(taCheck), BorderLayout.NORTH);
        pnlMain.add(pnlSouth, BorderLayout.CENTER);

        add(pnlMain,BorderLayout.CENTER);
    }
    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int choice = JOptionPane.showConfirmDialog(
                        UserInformationForm.this,
                        "내 정보를 나가시겠습니까?",
                        "내 정보",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    main.setVisible(true);
                }
            }
        });

        btnWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                int choice = JOptionPane.showConfirmDialog(
                        UserInformationForm.this,
                        "회원정보를 삭제하시겠습니까?",
                        "탈퇴",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(
                            UserInformationForm.this,
                            "회원 정보가 삭제되었습니다. 안녕히가세요."
                    );
                    dispose();
                    users.withdraw(main.getLblNo());
                    owner.setVisible(true);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                main.setVisible(true);
            }
        });
    }
    public void setTaCheck(String userInfo) {
        taCheck.setText(userInfo);
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }
}