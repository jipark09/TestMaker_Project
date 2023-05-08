import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// inner 클래스의 메서드를 꺼내올 수 있는지 물어보기
public class MyTest extends JFrame {
    private MainFrame owner;
    private QuizMaker quizMaker;
    private TestDataSet testList;
    private User user;
    private StateLine stateLine;

    private JLabel lblBack;
    private JLabel lblInfo; //도움말 아이콘

    private String[] strs;
    private JComboBox<String> comSearch;
    private JTextField tfSearch;
    private JButton btnSearch;
    private JButton btnTotal;
    private JButton btnRevice;
    private JButton btnDelete;

    private JPanel pnlSearch;
    private JPanel pnlSearchLine;

    // DateLine 이너 클래스 변수
    private JTextField tfStart;
    private JTextField tfEnd;
    private JButton btnStart;
    private JButton btnEnd;

    private JTable table;
    private TestTableModel model;
    private TestTableModel2 model2; // 해시
    private JPanel pnlTable;

    private ArrayList<Test> arrList;
    private ArrayList<Test> filteredData;
    private Test test; // 테이블 행 누르면 그 해당하는 Test

    // 내 문제 검색
    public MyTest(MainFrame owner) {
        this.owner = owner;
        testList = owner.getOwner().getUsers().getUser(owner.getLblNo()).getTestList();
        arrList = testList.getTests();
        filteredData = new ArrayList<>();

        init();
        setDisplay();
        addListener();
        setTitle("내 문제 검색");
        showFrame();
    }
    // 문제 불러오기
    public MyTest(MainFrame owner, QuizMaker quizMaker) {
        this.owner = owner;
        this.quizMaker = quizMaker;
        testList = owner.getOwner().getUsers().getUser(owner.getLblNo()).getTestList();
        arrList = testList.getTests();
        filteredData = new ArrayList<>();
        initTestMaker();
        setTestMaker();
        addListenerTest();
        setTitle("내 문제 불러오기");
        showFrame();

    }
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public Test getTest() {
        return test;
    }

    public QuizMaker getQuizMaker() {
        return quizMaker;
    }

    private void init() {
        lblBack = Util.setImageSize("img/뒤로.jpg",30,30);
        lblInfo = Util.setImageSize("img/info.png", 30, 30);

        strs = new String[]{"시험 이름", "등록 일시", "상태", "시험 문제(해시태그)", "시험 문제(문제)"};
        comSearch = new JComboBox<String>(strs);
        comSearch.setBackground(Color.WHITE);

        btnTotal = new JButton("모든 시험 보기");
        btnTotal.setBackground(Color.decode("#EBF7FF"));
        btnRevice = new JButton("수정");
        btnRevice.setBackground(Color.decode("#EBF7FF"));
        btnDelete = new JButton("삭제");
        btnDelete.setBackground(Color.decode("#EBF7FF"));
        model2 = new TestTableModel2();

    }
    private void initTestMaker() {
        lblBack = Util.setImageSize("img/뒤로.jpg",30,30);
        lblInfo = Util.setImageSize("img/info.png", 30, 30);

        strs = new String[]{"시험 문제(해시태그)", "시험 문제(문제)"};
        comSearch = new JComboBox<>(strs);
        comSearch.setBackground(Color.WHITE);
        model2 = new TestTableModel2();
    }

    private void setDisplay() {

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(lblBack, BorderLayout.WEST); // 왼쪽 정렬
        labelPanel.add(lblInfo, BorderLayout.EAST); // 오른쪽 정렬
        labelPanel.setBackground(Color.WHITE);

        pnlSearch = new JPanel();
        pnlSearch.add(comSearch);
        pnlSearchLine = new NameLine(true);
        pnlSearch.add(pnlSearchLine);
        pnlSearch.setBorder(new TitledBorder("검색하기"));
        pnlSearch.setBackground(Color.WHITE);

        pnlTable = new TablePanel();

        JPanel pnlBtn = new JPanel();
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.add(btnTotal);
        pnlBtn.add(btnRevice);
        pnlBtn.add(btnDelete);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(labelPanel, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);
        add(pnlTable, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);

    }
    private void setTestMaker() {
        JPanel pnlBack = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBack.setBackground(Color.WHITE);
        pnlBack.add(lblBack);

        pnlSearch = new JPanel();
        pnlSearch.add(comSearch);
        pnlSearchLine = new HashLine();
        pnlSearch.add(pnlSearchLine);
        pnlSearch.setBorder(new TitledBorder("검색"));
        pnlSearch.setBackground(Color.WHITE);
        pnlTable = new TablePanel();
        table.setModel(model2);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(pnlBack, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);
        add(pnlTable, BorderLayout.CENTER);
    }

    private void setLine(JPanel pnl) {
        pnlSearch.remove(1);
        pnlSearchLine = pnl;
        pnlSearch.add(pnlSearchLine);
        pack();
        revalidate();
    }

    private void addListener() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int choice = JOptionPane.showConfirmDialog(
                        MyTest.this,
                        "마이페이지로 넘어갑니다.",
                        "이동",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    owner.setVisible(true);
                }
            }
        });

        MouseListener mListener = new MouseAdapter()  {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == lblBack) {
                    dispose();
                    owner.setVisible(true);
                }
                if(e.getSource() == lblInfo) {
                    new Information(MyTest.this);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                if(e.getSource() == lblBack) {
                    lblBack.setCursor(handCursor);
                }
                if(e.getSource() == lblInfo) {
                    lblInfo.setCursor(handCursor);
                }
            }
        };
        lblBack.addMouseListener(mListener);
        lblInfo.addMouseListener(mListener);

        comSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = (comSearch.getSelectedItem().toString());
                if(str.equals("상태")) {
                    stateLine = new StateLine();
                    btnTotal.setVisible(true);
                    setLine(stateLine);
                    model = new TestTableModel();
                    table.setModel(model);
                }
                if(str.equals("시험 이름")) {
                    btnTotal.setVisible(true);
                    setLine(new NameLine(true));
                    model = new TestTableModel();
                    table.setModel(model);
                }
                if(str.equals("등록 일시")) {
                    btnTotal.setVisible(true);
                    setLine(new DateLine());
                    model = new TestTableModel();
                    table.setModel(model);
                }
                if(str.equals("시험 문제(해시태그)")) {
                    btnTotal.setVisible(false);
                    setLine(new HashLine());
                    model2 = new TestTableModel2();
                    table.setModel(model2);
                }
                if(str.equals("시험 문제(문제)")) {
                    btnTotal.setVisible(false);
                    setLine(new NameLine(false));
                    model2 = new TestTableModel2();
                    table.setModel(model2);
                }
            }
        });
        btnTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.totalTest();
            }
        });


        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlTable.remove(table.getSelectedRow());
                pack();
            }
        });
        // 테이블 더블클릭 이벤트
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 선택 모델이 변경될 때 처리할 로직
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 더블클릭
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    table.getValueAt(row, col);
                    TestMaker tm = new TestMaker(testList.getTest(test.getName()), owner);
                    tm.setBeforeFrame(MyTest.this);
                    // 해시태그에 검색하는거에 따라 창을 찾는 걸로 해놔서 다른 검색은 getIdxNum을 인식을 못해서 저 예외가 뜸!
                    // 그래서 예외처리해서 다른 검색은 1번으로 열리게 함!
                    try {
                        tm.getTbtns().get(model2.getIdxNum(table.getSelectedRow()) - 1).doClick();
                    } catch (IndexOutOfBoundsException e1) {
                        tm.getTbtns().get(0).doClick(); //1번 무조건 선택되어있게
                    }
                    dispose();

                    for (Quiz q : testList.getTest(test.getName()).getQuizzes(0)) {
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
//                int row = table.getSelectedRow();
//                int col = table.getSelectedColumn();
//                table.getValueAt(row, col);

//                table.setToolTipText(table.);



            }
        });

        btnRevice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                table.getValueAt(row, col);
                TestMaker tm = new TestMaker(testList.getTest(test.getName()), owner);
                tm.setBeforeFrame(MyTest.this);
                // 해시태그에 검색하는거에 따라 창을 찾는 걸로 해놔서 다른 검색은 getIdxNum을 인식을 못해서 저 예외가 뜸!
                // 그래서 예외처리해서 다른 검색은 1번으로 열리게 함!
                try {
                    tm.getTbtns().get(model2.getIdxNum(table.getSelectedRow()) - 1).doClick();
                } catch (IndexOutOfBoundsException e1) {
                    tm.getTbtns().get(0).doClick(); //1번 무조건 선택되어있게
                }
                dispose();
            }
        });

    }
    public void getPreview(int quizIdx) {
        LoadPreview loadPreview = new LoadPreview(MyTest.this, quizIdx);
        loadPreview.setTag((ArrayList<String>)loadPreview.getSubject().getQuizzes().get(loadPreview.getQuizIdxinSub()).getHashTag());
    }

    private void addListenerTest() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                quizMaker.getTestMaker().setVisible(true);
            }
        });

        comSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = (comSearch.getSelectedItem().toString());
                if(str.equals("시험 문제(해시태그)")) {
                    setLine(new HashLine());
                    model2 = new TestTableModel2();
                    table.setModel(model2);
                }
                if(str.equals("시험 문제(문제)")) {
                    setLine(new NameLine(false));
                    model2 = new TestTableModel2();
                    table.setModel(model2);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 더블클릭
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    table.getValueAt(row, col);

                    int quizIdx = model2.getIdxNum(getSelectedRow()) - 1;

                    // 검색한 후: 해당하는 번호로 켜짐 -> idx: 해당하는 번호(선택한 셀 - 1) (일단은 임의로 1과목만 함)
                    try {
                        getPreview(quizIdx);
                        // 검색하기 전: 선택한 셀 - 1을 하면 -1이 되기때문에 이 예외가 뜸 그래서 -1하면 안되고 무조건 첫번째 문제가 뜸
                    } catch (IndexOutOfBoundsException e1) {
                        getPreview(0);
                    }
                    setVisible(false);
                }

            }
        });
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
                quizMaker.getTestMaker().setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                if(e.getSource() == lblBack) {
                    lblBack.setCursor(handCursor);
                }
            }
        });

    }

    public void setTfStart(String year, String month, String day) {
        String date = year + "." + month + "." + day;

        tfStart.setText(date);
        tfStart.setForeground(Color.BLACK);

    }
    public void setTfEnd(String year, String month, String day) {
        String date = year + "." + month + "." + day;

        tfEnd.setText(date);
        tfEnd.setForeground(Color.BLACK);

    }

    // 이름
    private class NameLine extends JPanel{
        private boolean isTestName = true;
        public NameLine(boolean isTestName) {
            this.isTestName = isTestName;
            set();
        }
        private void set() {
            tfSearch = new JTextField(20);
            btnSearch = new JButton("검색");
            btnSearch.setBackground(Color.decode("#EBF7FF"));
            setBackground(Color.WHITE);

            add(tfSearch);
            add(btnSearch);

            btnSearch.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String searchText = tfSearch.getText();
                    if(isTestName){
                        model.searchName(searchText);
                    }else {
                        model2.searchTitle(searchText);
                    }
                }
            });
        }
    }

    public class DateLine extends JPanel{
        private JLabel lblStart;
        private JLabel lblWave;
        private JLabel lblEnd;

        private int clickCheck = 0; // 처음 달력부터

        public DateLine() {
            init();
            setDisplay();
            addListener();
        }

        private void init() {
            lblStart = new JLabel("시작 날짜");
            lblWave = new JLabel(" ~ ");
            lblEnd = new JLabel("마지막 날짜");
            tfStart = new JTextField(10);
            tfStart.setText("예) 2023.02.10");
            tfStart.setForeground(Color.LIGHT_GRAY);

            tfEnd = new JTextField(10);
            tfEnd.setText("예) 2023.03.10");
            tfEnd.setForeground(Color.LIGHT_GRAY);
            ImageIcon icon = Util.setImageIcon("img/icon.png", 20, 20);
            btnStart = new JButton(icon);
            btnStart.setBackground(Color.WHITE);
            btnEnd = new JButton(icon);
            btnEnd.setBackground(Color.WHITE);
            btnSearch = new JButton("검색");
            btnSearch.setBackground(Color.decode("#EBF7FF"));

        }
        private void setDisplay() {
            add(lblStart);
            add(tfStart);
            add(btnStart);
            add(lblWave);
            add(lblEnd);
            add(tfEnd);
            add(btnEnd);
            add(btnSearch);
            setBackground(Color.WHITE);

        }

        private void addListener() {
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == btnStart) {
                        if(clickCheck == 0) {
                            new Calendar(MyTest.this);

                        } else if(clickCheck != 0) {
                            clickCheck = 0;
                            new Calendar(MyTest.this);
                        }
                    }
                    if(e.getSource() == btnEnd) {
                        if(clickCheck == 1) {
                            new Calendar(MyTest.this);
                        } else if(clickCheck != 1) {
                            clickCheck = 1;
                            new Calendar(MyTest.this);
                        }
                    }
                    if(e.getSource() == btnSearch) {
                        String startDateString = tfStart.getText();
                        String endDateString = tfEnd.getText();

                        model.searchDate(startDateString, endDateString);
                    }
                }
            };
            btnStart.addActionListener(actionListener);
            btnEnd.addActionListener(actionListener);
            btnSearch.addActionListener(actionListener);

            FocusListener focusListener = new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {

                    if(e.getSource() == tfStart) {
                        if(tfStart.getText().equals("예) 2023.02.10")) {
                            tfStart.setText("");
                        }
                        tfStart.setForeground(Color.BLACK);
                    }
                    if(e.getSource() == tfEnd) {
                        if(tfEnd.getText().equals("예) 2023.03.10")) {
                            tfEnd.setText("");
                        }
                        tfEnd.setForeground(Color.BLACK);
                    }

                }
                // 포커스를 잃었을 때
                @Override
                public void focusLost(FocusEvent e) {
                    if(e.getSource() == tfStart) {

                        if (tfStart.getText().trim().equals("")) {
                            tfStart.setText("예) 2023.02.10");
                            tfStart.setForeground(Color.LIGHT_GRAY);
                        }
                    } else {
                        if(tfEnd.getText().trim().equals("")) {
                            tfEnd.setText("예) 2023.03.10");
                            tfEnd.setForeground(Color.LIGHT_GRAY);
                        }
                    }
                }
            };
            tfStart.addFocusListener(focusListener);
            tfEnd.addFocusListener(focusListener);

        }

        // DateLine inner-Class
        public class Calendar extends JDialog implements ActionListener {

            private MyTest owner;

            private String days[] = { "일", "월", "화", "수", "목", "금", "토" };
            private java.util.Calendar now = java.util.Calendar.getInstance();
            private int year;
            private int month;

            private JButton lastMonth;
            private JButton nextMonth;

            private JComboBox<Integer> yearCombo;
            private DefaultComboBoxModel<Integer> yearModel;
            private JComboBox<Integer> monthCombo;
            private DefaultComboBoxModel<Integer> monthModel;

            private JLabel lblYear;
            private JLabel lblMonth;

            // 중앙 중앙 지역
            private JPanel cntCenter;

            public Calendar(MyTest owner) {
                super(owner, "Swing.Calendar", true);
                this.owner = owner;
                init();
                setDisplay();
                addListeners();
                showFrame();

            }

            private void init() {

                lblYear = new JLabel("년 ");
                lblMonth = new JLabel("월");

                lastMonth = new JButton("◀");
                nextMonth = new JButton("▶");
                yearCombo = new JComboBox<Integer>();
                yearModel = new DefaultComboBoxModel<Integer>();
                monthCombo = new JComboBox<Integer>();
                monthModel = new DefaultComboBoxModel<Integer>();

                year = now.get(java.util.Calendar.YEAR);// 2021년
                month = now.get(java.util.Calendar.MONTH) + 1; // 0월 == 1월
                for(int i = year; i <= year + 50; i++){yearModel.addElement(i);}
                for(int i = 1; i<= 12; i++) { monthModel.addElement(i); }

            }
            private void setDisplay() {
                // 상단 지역
                JPanel bar = new JPanel();
                bar.add(lastMonth);

                bar.add(yearCombo);
                yearCombo.setModel(yearModel);
                yearCombo.setSelectedItem(year);
                bar.add(lblYear);

                bar.add(monthCombo);
                monthCombo.setModel(monthModel);
                monthCombo.setSelectedItem(month);
                bar.add(lblMonth);
                bar.add(nextMonth);
                bar.setBackground(new Color(157, 195, 230));

                // 중앙 상단 지역
                JPanel cntNorth = new JPanel(new GridLayout(0, 7));
                cntNorth.setBackground(Color.WHITE);
                for(int i = 0; i < days.length; i++) {
                    JLabel dayOfWeek = new JLabel(days[i], JLabel.CENTER);

                    if(i == 0) {
                        dayOfWeek.setForeground(Color.red);

                    } else if(i == 6) {
                        dayOfWeek.setForeground(Color.blue);
                    }
                    cntNorth.add(dayOfWeek);
                }

                // 중앙 중앙 지역
                cntCenter = new JPanel(new GridLayout(0, 7));
                cntCenter.setBackground(Color.WHITE);
                dayPrint(year, month);

                // 중앙 지역
                JPanel center = new JPanel(new BorderLayout());
                center.setBackground(Color.WHITE);
                center.add("North", cntNorth);
                center.add("Center", cntCenter);
                center.setBorder(new EmptyBorder(15,10,10,10));

                add("North", bar);
                add("Center", center);
            }
            public void addListeners() {
                yearCombo.addActionListener(this);
                monthCombo.addActionListener(this);
                lastMonth.addActionListener(this);
                nextMonth.addActionListener(this);
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dispose();
                    }
                });
            }

            // 이벤트 처리
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = e.getSource();
                if(obj instanceof JButton){
                    JButton eventBtn = (JButton)obj;
                    int yy = (Integer)yearCombo.getSelectedItem();
                    int mm = (Integer)monthCombo.getSelectedItem();

                    if(eventBtn.equals(lastMonth)) {	//전달
                        if(mm == 1 && yy == year ) {
                        } else if(mm == 1){
                            yy--; mm = 12;
                        } else {
                            mm--;
                        }

                    } else if(eventBtn.equals(nextMonth)){	//다음달
                        if(mm == 12){
                            yy++; mm = 1;
                        }else{
                            mm++;
                        }
                    }
                    yearCombo.setSelectedItem(yy);
                    monthCombo.setSelectedItem(mm);

                } else if(obj instanceof JComboBox){	//콤보박스 이벤트 발생시
                    createDayStart();
                }
            }

            private void createDayStart() {
                cntCenter.setVisible(false);	//패널 숨기기
                cntCenter.removeAll();	//날짜 출력한 라벨 지우기
                dayPrint((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem());
                cntCenter.setVisible(true);	//패널 재출력
            }

            // 날짜 출력
            public void dayPrint(int y, int m) {
                now.set(y, m-1, 1);
                int week = now.get(java.util.Calendar.DAY_OF_WEEK); // 1일에 대한 요일
                int lastDate = now.getActualMaximum(java.util.Calendar.DAY_OF_MONTH); // 1월에 대한 마지막 요일
                for(int i = 1; i < week; i++) { // 1월 1일 전까지 공백을 표시해라
                    cntCenter.add(new JLabel(""));
                }
                for(int i = 0; i <= lastDate - 1; i++) { // 1월 마지막 날까지 숫자를 적어라, 그리고 토요일 일요일은 색깔을 입혀라
                    JLabel lblDay = new JLabel();
                    lblDay.setHorizontalAlignment(JLabel.CENTER);
                    if((week + i) % 7 == 0) {
                        cntCenter.add(lblDay).setForeground(Color.blue);
                        lblDay.setText(1 + i + "");
                    } else if((week + i) % 7 == 1) {
                        cntCenter.add(lblDay).setForeground(Color.red);
                        lblDay.setText(1 + i + "");
                    } else {
                        cntCenter.add(lblDay);
                        lblDay.setText(1 + i + "");
                    }
                    lblDay.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent me) {
                            JLabel mouseClick = (JLabel)me.getSource();
                            String str = mouseClick.getText();

                            String y = "" + yearCombo.getSelectedItem();
                            String m = "" + monthCombo.getSelectedItem();

                            // 받은 "요일"이 1자리면 0을 붙여라
                            if(str.equals("")) {
                            }
                            if(str.length() == 1) {
                                str = "0" + str;
                            }
                            // 받은 "월"이 1자리면 0을 붙여라
                            if(m.length() == 1) {
                                m = "0" + m;
                            }

                            // 클릭 횟수에 따라 시작일 마지막일 날짜 붙임
                            if(clickCheck == 0) {
                                owner.setTfStart(y, m, str);
                                clickCheck++;

                            } else if(clickCheck == 1) {
                                owner.setTfEnd(y, m, str);
                                clickCheck--;
                            }
                            dispose();

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            lblDay.setBorder(null);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            lblDay.setBorder(new LineBorder(new Color(157, 195, 230)));
                            lblDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                    });
                }

            }
            private void showFrame() {
                setSize(400,300);
                setLocationRelativeTo(null);
                setResizable(false);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setVisible(true);
            }
        }
    }

    // 해시태그
    public class HashLine extends JPanel {
        private JTextField tfHash1;
        private JTextField tfHash2;
        private JTextField tfHash3;

        private HashLine() {
            set();
            addListener();
        }
        public void setTag(JTextField jTextField, String strTag) {
            jTextField.setText(strTag);
        }
        public User getUser() {
            return owner.getOwner().getUsers().getUser(owner.getLblNo());
        }
        private void set() {
            String mark = "#";
            Color gray = Color.LIGHT_GRAY;
            tfHash1 = new JTextField(8);
            tfHash1.requestFocus();
            tfHash1.setText(mark);
            tfHash1.setForeground(gray);

            tfHash2 = new JTextField(8);
            tfHash2.setText(mark);
            tfHash2.setForeground(gray);

            tfHash3 = new JTextField(8);
            tfHash3.setText(mark);
            tfHash3.setForeground(gray);

            add(tfHash1);
            add(tfHash2);
            add(tfHash3);
            btnSearch = new JButton("검색");
            btnSearch.setBackground(Color.decode("#EBF7FF"));
            add(btnSearch);
            setBackground(Color.WHITE);
        }

        // 해시태그 중북으로 넣는거 ㄴㄴ
        public void hashOverlapCond() {
            if(tfHash1.getText().equals(tfHash2.getText()) && !tfHash1.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        this,
                        "해시태그는 중복으로 넣을 수 없습니다."
                );
                tfHash2.setText("");
            }
            if(tfHash1.getText().equals(tfHash3.getText()) && !tfHash1.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        this,
                        "해시태그는 중복으로 넣을 수 없습니다."
                );
                tfHash3.setText("");
            }
            if(!tfHash2.getText().trim().equals("#")) {
                if(tfHash2.getText().equals(tfHash3.getText()) && !tfHash2.getText().equals("")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "해시태그는 중복으로 넣을 수 없습니다."
                    );
                    tfHash3.setText("");
                }
            }
        }

        private void addListener() {
            MouseListener mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getSource() == tfHash1) {
                        tfHash1.setForeground(Color.BLACK);
                        new HashSearch(HashLine.this, tfHash1);
                    }
                    if(e.getSource() == tfHash2) {
                        tfHash2.setForeground(Color.BLACK);
                        new HashSearch(HashLine.this, tfHash2);
                    }
                    if(e.getSource() == tfHash3) {
                        tfHash3.setForeground(Color.BLACK);
                        new HashSearch(HashLine.this, tfHash3);
                    }
                }
            };
            tfHash1.addMouseListener(mouseListener);
            tfHash2.addMouseListener(mouseListener);
            tfHash3.addMouseListener(mouseListener);

            btnSearch.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model2.hashSearch(tfHash1.getText(),tfHash2.getText(), tfHash3.getText());
                }
            });
        }
    }

    // 상태
    private class StateLine extends JPanel {
        private JComboBox<String> cmbState;
        private String[] str;

        public StateLine() {
            set();
        }
        private void set() {
            str = new String[]{"출제중", "출제완료"};
            cmbState = new JComboBox<>(str);
            setBackground(Color.WHITE);
            btnSearch = new JButton("검색");
            btnSearch.setBackground(Color.decode("#EBF7FF"));

            add(cmbState);
            add(btnSearch);

            btnSearch.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.searchState();
                }
            });
        }
    }

    private class TablePanel extends JPanel {

        /* Constructor */
        public TablePanel() {
            super(new BorderLayout());
//            MyTest.this.arrList = list;
            setOpaque(true);

            model = new TestTableModel();
            model.addTableModelListener(
                    new TableModelListener() {
                        @Override
                        public void tableChanged(TableModelEvent e) {
                        }
                    }
            );

            // table settings
            table = new JTable(model);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(500,300));
            add(scroll, BorderLayout.CENTER);

        }

        public void remove(int row) {
            AbstractTableModel model = (AbstractTableModel) table.getModel();
            try {
                int choice = JOptionPane.showConfirmDialog(
                        MyTest.this,
                        "선택한 문제를 삭제하시겠습니까?",
                        "시험 삭제",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if(choice == JOptionPane.YES_OPTION) {
                    arrList.remove(row);
                    testList.getTests().remove(row);
                    model.fireTableRowsDeleted(row, row);
                }

            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "삭제할 문제를 선택 해 주세요."
                );
            }
        }

        public int getSelectedIndex() {
            return table.getSelectedRow();
        }
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    // 이름, 날짜, 상태 검색
    class TestTableModel extends AbstractTableModel {

        /* this is header*/
        private String[] colsName = {"시험 이름", "과목 수", "제한 시간(분)", "최종 수정 일시", "상태"};

        public TestTableModel() {
            filteredData.clear();
        }

        public void totalTest() {
            filteredData.clear();
            filteredData.addAll(testList.getTests());
            fireTableDataChanged();
        }

        // 이름 검색
        public void searchName(String searchText) {
            filteredData.clear();
            for (Test test : testList.getTests()) {
                if (test.getName().contains(searchText)) {
                    filteredData.add(test);
                }
            }
            fireTableDataChanged();
            //사이즈조절
            Util.resizeColumnWidth(table);
        }
        //날짜 검색
        public void searchDate(String start, String end) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            filteredData.clear();
            try {
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(formatter.parse(start));
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(formatter.parse(end));
                endDate.add(Calendar.DATE, 1);

                for(Test test : testList.getTests()) {
                    Calendar testDate = Calendar.getInstance();
                    testDate.setTime(test.getToday().getTime());
                    if(testDate.after(startDate) && testDate.before(endDate)) {
                        filteredData.add(test);
                    }
                }
                fireTableDataChanged();
                //사이즈조절
                Util.resizeColumnWidth(table);

            } catch (ParseException e ){}
        }


        // 상태 검색
        public void searchState() {
            filteredData.clear();
            for (Test test : testList.getTests()) {
                if (stateLine.cmbState.getSelectedItem().equals("출제중")) {
                    if(test.getState().equals("출제중")) {
                        filteredData.add(test);
                    }
                } else {
                    if(test.getState().equals("출제완료")) {
                        filteredData.add(test);
                    }
                }
            }
            fireTableDataChanged();
            //사이즈조절
            Util.resizeColumnWidth(table);
        }

        /* If show your table's header, must implements this method */
        @Override
        public String getColumnName(int col) {
            return colsName[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            test = filteredData.get(rowIndex);
            Object cellData = "";
            switch (columnIndex) {
                case 0:
                    cellData = test.getName();
                    break;
                case 1:
                    cellData = test.getSubjects().size();
                    break;
                case 2:
                    cellData = (test.getTimeOut() / 60) + "분";
                    break;
                case 3:
                    cellData = test.getLastEditDate();
                    break;
                case 4:
                    cellData = test.getState();
                    break;
            }
            return cellData;
        }

        @Override
        public int getColumnCount() {
            return colsName.length;
        }

        @Override
        public int getRowCount() {
            return filteredData.size();
        }
    }

    // 해시태그 표 모델
    class TestTableModel2 extends AbstractTableModel {

        /* this is header*/
        private String[] colsName = {"시험 이름", "과목", "문제번호", "문제", "해시태그"};

        ArrayList<Quiz> quizzes;

        public TestTableModel2() {
            filteredData.clear();
        }


        public void hashSearch(String str1, String str2, String str3) {

            // str이 빈 문자열이 아니거나, #이 아닐 경우에만 hash 리스트에 추가
            ArrayList<String> hash = new ArrayList<>();
            if (!str1.equals("") && !str1.equals("#")) {
                hash.add(str1);
            }
            if (!str2.equals("") && !str2.equals("#")) {
                hash.add(str2);
            }
            if (!str3.equals("") && !str3.equals("#")) {
                hash.add(str3);
            }

            //문제도 같이 담아야 열에서 문제의 정보를 받을 수 있고, 더블클릭했을 때 그 문제로 들어갈 수 있음
            quizzes = new ArrayList<>();
            filteredData.clear();
            for(Test test : testList.getTests()) {
                for(Subject subject : test.getSubjects()) {
                    for(Quiz quiz : subject.getQuizzes()) {
                        if(quiz.getHashTag() != null && !(quiz.getHashTag().isEmpty())) {
                            List<String> quizHash = new ArrayList<>(quiz.getHashTag()); //퀴즈의 해시태그List

                            quizHash.retainAll(hash);
                            //검색한 해시태그 개수랑 교집합의 개수가 같으면 보여주면 됨
                            if(quizHash.size()==hash.size()) {
                                filteredData.add(test);
                                quizzes.add(quiz);
                            }
                        }
                    }
                }
            }

            fireTableDataChanged();
            //사이즈조절
            Util.resizeColumnWidth(table);
        }


        //문제 타이틀로 검색하기
        public void searchTitle(String searchText) {
            filteredData.clear();
            quizzes = new ArrayList<>();
            for (Test test : testList.getTests()) {
                for(Subject sub : test.getSubjects()) {
                    for(Quiz quiz : sub.getQuizzes()) {
                        if(quiz.getTitle()!=null) {
                            if (quiz.getTitle().contains(searchText)) {
                                filteredData.add(test);
                                quizzes.add(quiz);
                            }
                        }
                    }
                }
            }

            fireTableDataChanged();
            //사이즈조절

        }

        @Override
        public String getColumnName(int col) {
            return colsName[col];
        };

        public String getSubject(int rowIndex) {
            if(quizzes!=null) {
                List<Subject> subjects = test.getSubjects();
                if(subjects.size()!=1) {
                    for(int i=1; i<subjects.size(); i++){
                        if(subjects.get(i).getQuizzes().contains(quizzes.get(rowIndex))) {
                            return subjects.get(i).getName();
                        }
                    }
                }
                return subjects.get(0).getName();
            } else {
                return "";
            }
        }

        public int getIdxNum(int rowIdx) {
            if(quizzes != null) {
                return quizzes.get(rowIdx).getNum();
            } else  {
                return 0;
            }
        }
        public String getNum(int rowIndex) {
            if(quizzes!=null) {
                return quizzes.get(rowIndex).getNum() + "번";
            } else {
                return "";
            }
        }
        public String getTitle(int rowIndex) {
            if(quizzes!=null) {
                return quizzes.get(rowIndex).getTitle();
            } else {
                return "";
            }
        }
        public List<String> getHash(int rowIndex) {
            if(quizzes!=null) {
                return quizzes.get(rowIndex).getHashTag();
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            test = filteredData.get(rowIndex);
            Object cellData = "";
            switch (columnIndex) {
                case 0:
                    cellData = test.getName();
                    break;
                case 1:
                    cellData = getSubject(rowIndex);
                    break;
                case 2:
                    cellData = getNum(rowIndex);
                    break;
                case 3:
                    cellData = getTitle(rowIndex);
                    break;
                case 4:
                    cellData = getHash(rowIndex);
                    break;
            }

            return cellData;
        }

        @Override
        public int getColumnCount() {
            return colsName.length;
        }

        @Override
        public int getRowCount() {
            return filteredData.size();
        }
    }

    public void remove(int row) {
        AbstractTableModel model = (AbstractTableModel) table.getModel();
        try {
            int choice = JOptionPane.showConfirmDialog(
                    MyTest.this,
                    "선택한 문제를 삭제하시겠습니까?",
                    "시험 삭제",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if(choice == JOptionPane.YES_OPTION) {
                arrList.remove(row);
                testList.getTests().remove(row);
                model.fireTableRowsDeleted(row, row);
            }

        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "삭제할 문제를 선택 해 주세요."
            );
        }
    }
}