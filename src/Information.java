import javax.swing.*;

public class Information extends JDialog {
    private MainFrame mainFrame;
    private TestMaker testMaker;
    private MyTest myTest;

    private JTextArea textArea;

    public Information(MainFrame mainFrame) {
        super(mainFrame, "도움말", false);
        this.mainFrame = mainFrame;
        set();
        textArea.setText(
                " \n 1. 새 문제 출제하기\n  " +
                        ": 시험분류, 시험이름, 과목수, 제한시간을 올바르게 입력하면 새 시험을 만들 수 있습니다.\n  " +
                        ": 과목수는 5개 이하로 만들 수 있고, 한 과목당 최대 100문제까지 만들 수 있습니다.\n  " +
                        ": 문제 수는 추후에 변경이 불가합니다.\n\n" +
                        " 2. 내 문제 보기\n  " +
                        ": 이때까지 만들었던 시험들을 볼 수 있습니다.\n  " +
                        ": 시험이름, 등록일시, 상태로 시험을 검색할 수 있으며,\n  " +
                        ": 해시태그로 시험문제를 검색할 수 있습니다.\n  "

        );
        setFrame();

    }
    public Information(TestMaker testMaker) {
        super(testMaker, "도움말", false);
        this.testMaker = testMaker;
        set();
        textArea.setText(
                "  1. 시험정보\n  " +
                        ":  '수정'버튼을 누르면 시험이름, 과목이름, 제한시간을 수정할 수 있습니다.\n\n  " +
                        " 2. 시험과목별 문제\n  " +
                        ":  콤보박스를 클릭하면 과목을 선택할 수 있고, 번호를 클릭하면 해당 번호의 문제를 만들 수 있습니다.\n  " +
                        ":  문제를 만드는 창 아래에 있는 '저장'버튼을 누르면 저장이 되고, 해당 번호의 버튼 색이 하늘색을 변경됩니다.\n\n  " +
                        " 3. 미리보기\n  " +
                        ":  '저장'을 하고 나면, 해당 문제를 푸는 창을 미리 볼 수 있습니다.\n\n  " +
                        " 4. 파일 내보내기\n  " +
                        ":  (문제지문을 제외한) 모든 칸을 채워야 '저장'을 할 수 있으며,\n  " +
                        ":  해당 시험의 모든 문제가 저장되어있고, 남은 점수가 0이면 원하는 폴더에 시험문제 파일을 만들 수 있습니다.\n\n  " +
                        " 5. 문제 불러오기\n  " +
                        ":  해시태그 or 문제로 검색해서 해당 테스트를 미리보기 창으로 볼 수 있고,\n  " +
                        ":  '불러오기'를 선택하면 문제를 불러올 수 있습니다.\n\n  " +
                        " 6. 문제지문, 선지지문\n  " +
                        ":  더블클릭을 누르면 텍스트를 쓸 수 있습니다.\n  " +
                        ": 사진을 첨부하고 싶다면 해당 위치를 선택하고, 문제 번호 위의 사진버튼을 클릭해주세요.\n  " +
                        ":  문제지문은 없어도 저장이 가능하지만, 선지지문은 1개 이상의 선지가 있어야 합니다.\n\n  " +
                        " 7. 정답, 해시태그\n  " +
                        ":  정답은 복수체크가 가능합니다.\n  " +
                        ":  해시태그는 문제검색을 위해 하나 이상 입력해주세요.\n\n" +
                        "  8. 문제지문 사진 첨부\n" +
                        "  :  외부에서 불러온 사진은 본인 Id_img폴더에 저장됩니다.\n"
        );
        setFrame();

    }
    public Information(MyTest myTest) {
        super(myTest, "도움말", false);
        this.myTest = myTest;
        set();
        textArea.setText(
                " 1. 시험 이름 검색\n " +
                        ":  시험 이름을 검색하면 해당하는 시험 목록이 나옵니다.\n\n" +
                        " 2. 등록 일시 검색\n" +
                        ":  등록 일시로 검색할 수 있으며 달력을 눌러서 해당 날짜를 선택할 수 있습니다.\n\n" +
                        " 3. 상태 검색\n" +
                        ":  해당 문제가 '출제중'인지 '출제완료'인지 검색할 수 있습니다.\n" +
                        ":  '출제중'인 문제는 시험 창에서 등록(파일 업로드) 버튼을 누르지 않은 문제입니다.\n" +
                        ":  '출제완료'인 문제는 시험 창에서 등록(파일 업로드) 버튼을 누른 문제입니다.\n\n" +
                        " 4. 시험 문제(해시태그) 검색\n " +
                        ":  새 시험창에서 넣었던 문제 해시태그를 검색하여 해당 문제를 찾을 수 있습니다.\n" +
                        ":  총 3개의 해시태그를 넣어 검색할 수 있으며 중복으로 넣을 순 없습니다.\n\n" +
                        " 5. 시험 문제(문제) 검색\n " +
                        ":  글자를 입력하면 그 글자가 포함된 시험문제의 시험이름,과목이름,문제번호,문제,해시태그를 볼 수 있다. \n" +
                        " 6. 수정\n" +
                        ":  시험 목록이 뜨면 해당하는 행을 더블클릭하여 문제 창을 열고 수정할 수 있습니다.\n" +
                        ":  또한 행을 클릭하고 수정버튼을 눌러도 창을 열고 수정할 수 있습니다.\n\n" +
                        " 7. 삭제\n" +
                        ":  삭제하고 싶은 문제를 클릭하고 삭제버튼을 누르면 시험을 삭제할 수 있습니다."

        );
        setFrame();
    }

    private void set() {
        textArea = new JTextArea(20, 50);
        textArea.setLineWrap(true); // 줄 바꿈 활성화
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        add(panel);
    }

    private void setFrame() {
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
