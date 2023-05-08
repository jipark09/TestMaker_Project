import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class HashSearch extends JFrame implements Serializable {

    private MyTest.HashLine hashLine;
    private QuizMaker.PnlQuizInfo pnlQuizInfo;
    private ArrayList<Hash> hashList;
    private Vector<String> tableList;
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private boolean flag = false;

    private JLabel lblSearch;
    private JTextField tfSearch;
    private JButton btnAdd;
    private JButton btnSelect;
    private JButton btnDelete;

    // JTable 선택한 행, 태그
    private int selectedRow;
    private String tag;

    private ArrayList<Test> testList;

    //생성자 호출: MyTest
    public HashSearch(MyTest.HashLine hashLine, JTextField jTextField) {
        this.hashLine = hashLine;
        hashList = hashLine.getUser().getHashList();
        testList = hashLine.getUser().getTestList().getTests();
        init();
        setDisplay();
        addListener();
        setMyTest(jTextField);
        btnAdd.setVisible(false);
        showFrame();
    }
    // 생성자 호출: QuizMaker
    public HashSearch(QuizMaker.PnlQuizInfo pnlQuizInfo, JLabel lblTag) {
        this.pnlQuizInfo = pnlQuizInfo;
        hashList = pnlQuizInfo.getUser().getHashList();
        testList = pnlQuizInfo.getUser().getTestList().getTests();
        init();
        setDisplay();
        addListener();
        setQuizMaker(lblTag);
        showFrame();
    }

    public Vector<String> getTableList() {
        return tableList;
    }

    private void init() {
        tableList = new Vector<String>();
        lblSearch = new JLabel("검색");
        tfSearch = new JTextField(15);
        tfSearch.setText("#");
        tfSearch.setForeground(Color.LIGHT_GRAY);
        tfSearch.requestFocus();

        //타이틀 없음
        Vector<String> vector = new Vector<String>();
        vector.add("");

        //defaultTableModel 생성
        model = new DefaultTableModel(vector, 0) {
            public boolean isCellEditable(int r, int c) {
                return (c != 0) ? true : false;
            }
        };

        table = new JTable(model);


        // 데이터를 Vector String 리스트 형식으로 추가
        makeRow(hashList);

        btnAdd = new JButton("추가");
        btnAdd.setBackground(Color.decode("#EBF7FF"));
        btnSelect = new JButton("선택");
        btnSelect.setBackground(Color.decode("#EBF7FF"));
        btnDelete = new JButton("삭제");
        btnDelete.setBackground(Color.decode("#EBF7FF"));

        rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
    }

    // 데이터를 Vector String 리스트 형식으로 추가 메서드
    private void makeRow(ArrayList<Hash> dataList) {
        for(Hash data : dataList) {
            tableList = new Vector<String>();
            tableList.add(data.getTag());
            model.addRow(tableList);
        }
    }

    private void setDisplay() {
        JPanel pnlNorth = new JPanel();
        pnlNorth.add(lblSearch);
        pnlNorth.add(tfSearch);
        pnlNorth.add(btnAdd);
        pnlNorth.setBackground(Color.WHITE);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300, 250));
        pnlCenter.add(scroll);

        JPanel pnlSouth = new JPanel();
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.add(btnSelect);
        pnlSouth.add(btnDelete);

        add(pnlNorth, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    // 테이블 행 변경 후 호출
    private void tableAction() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    tag = model.getValueAt(modelRow, 0).toString();
                }
            }
        });

    }

    private void setQuizMaker(JLabel lblTag) {
        tableAction();

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblTag.setForeground(Color.BLACK);
                pnlQuizInfo.setTag(lblTag, tag);
                pnlQuizInfo.hashOverlapCond();
                //dataSave();
                dispose();
            }
        });
    }

    private void setMyTest(JTextField jTextField) {
        tableAction();

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashLine.setTag(jTextField, tag);
                hashLine.hashOverlapCond();
                //dataSave();
                dispose();
            }
        });


    }

    private void search() {
        String text = tfSearch.getText();

        if (text.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            // (?i) 정규식 => 대소문자 구분안함
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void addListener() {
        tfSearch.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });

        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {

                if (e.getSource() == tfSearch) {
                    tfSearch.setForeground(Color.BLACK);

                }
            }

            // 포커스를 잃었을 때
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() == tfSearch) {
                    if(tfSearch.getText().trim().equals("#") || tfSearch.getText().trim().equals("")) {
                        tfSearch.setText("#");
                        tfSearch.setForeground(Color.LIGHT_GRAY);

                        if(flag == true) {
                            model.setNumRows(0);
                            makeRow(hashList);
                        }

                    }
                }
            }
        };
        tfSearch.addFocusListener(focusListener);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btnAdd) {
                    if(tfSearch.getText().equals("#") || tfSearch.getText().trim().equals("")) {
                        message("Error: 해시태그를 입력해 주세요.");

                    } else if(!tfSearch.getText().contains("#") || tfSearch.getText().indexOf("#") != 0) {
                        message("Error: 구문통일 오류 (ex) #국어)");

                    } else if(tfSearch.getText().lastIndexOf("#") != 0) {
                        message("Error: '#'은 하나만 입력가능합니다. (ex) #국어)");

                    } else if(hashList.contains(new Hash(tfSearch.getText()))) {
                        //message("해시태그 중복");
                    } else {
                        add();
                    }
                }
                if(e.getSource() == btnDelete) {
                    delete();
                }
            }
        };
        btnAdd.addActionListener(actionListener);
        btnDelete.addActionListener(actionListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //dataSave();
                dispose();

            }
        });
    }

    private void message(String sentence) {
        JOptionPane.showMessageDialog(
                this,
                sentence
        );
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    //테이블에서 값 추가하는 메서드
    private void add() {
        String text = tfSearch.getText();

        hashList.add(new Hash(text));
        model.setNumRows(0);
        makeRow(hashList);
        tfSearch.setText("#");
    }

    //테이블에서 값 삭제하는 메서드
    private void delete() {
        int row = table.getSelectedRow();
        Object value = table.getValueAt(row, 0); // 테이블 선택한 행의 str

        if(row == -1) {
            return;
        }

        ArrayList<String> usedHash = new ArrayList<>();
        for(Test test : testList) {
            for(Subject subject : test.getSubjects()) {
                for(Quiz quiz : subject.getQuizzes()) {

                    if(quiz.getHashTag()!=null) {
                        usedHash.addAll(quiz.getHashTag());
                    }
                }
            }
        }

        if(usedHash.contains(value)) {
            JOptionPane.showMessageDialog(
                    this,
                    "사용 중인 해시태그 입니다."
            );
        } else {
            model.removeRow(row);
            hashList.remove(row);
        }
    }
}