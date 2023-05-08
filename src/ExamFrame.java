import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

public class ExamFrame extends CustomFrame {

    private JLabel examTitleLabel;

    private CustomTextLabel titleLabel;
    private CustomIconLabel pauseLabel;
    public TimerLabel timerLabel;

    private JList<DefaultListModel> list;
    private DefaultListModel<String> model;

    private CustomIconLabel nextButton;
    private CustomIconLabel previousButton;
    private CustomIconLabel submitButton;
    private CustomIconLabel exitButton;

    private CardLayout card;
    private JPanel cardPanel;
    private Thread thread;

    private Test test;
    private Subject subject;
    private int idx;



    public ExamFrame(Test test, Subject subject, int idx) {
        init(test, subject, idx);
        setDisplay();
        setAction();
        setFrame();
    }

    private void init(Test test, Subject subject, int idx) {
        this.test = test;
        this.subject = subject;
        this.idx = idx;

        examTitleLabel = new CustomTextLabel(subject.getName(), JLabel.CENTER);

        titleLabel = new CustomTextLabel(test.getName(), JLabel.CENTER);
        titleLabel.setFontSize(30);
        titleLabel.setBorder(new EmptyBorder(15,0,15,0));

        pauseLabel = new CustomIconLabel("", new ImageIcon("img/pause_button.png"));
        pauseLabel.setPressIcon(new ImageIcon("img/pause_press.png"));

        timerLabel = new TimerLabel(this, test.getTimeOut());
        timerLabel.setPreferredSize(new Dimension(150, 80));
        thread = new Thread(timerLabel);
        thread.start();

        model = new DefaultListModel<>();
        for(int i = 0; i < subject.getQuizzes().size(); i++) {
            model.addElement("문제 - " + (i + 1) + "번");
        }

        list = new JList(model);
        list.setSelectedIndex(0);
        list.setOpaque(false);
        list.setCellRenderer(new TransparentListCellRenderer());
        list.setDragEnabled(true);
        list.setBorder(null);
        list.setFont(new Font("휴먼엑스포", Font.PLAIN, 15));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        nextButton = new CustomIconLabel("",new ImageIcon("img/next_button.png"));
        nextButton.setPressIcon(new ImageIcon("img/next_press.png"));
        previousButton = new CustomIconLabel("", new ImageIcon("img/previous_button.png"));
        previousButton.setPressIcon(new ImageIcon("img/previous_press.png"));

        submitButton = new CustomIconLabel("", new ImageIcon("img/submit_button.png"));
        exitButton = new CustomIconLabel("", new ImageIcon("img/exit_button.png"));

        card = new CardLayout();
    }

    private void setDisplay() {
        JPanel centerPanel = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.YELLOW);
                Image img = getToolkit().getImage("img/back.jpg").getScaledInstance(1600, 900, Image.SCALE_SMOOTH);
                g.drawImage(new ImageIcon(img).getImage(),0,0,null);
                setOpaque(false);
                Graphics2D g2=(Graphics2D)g;
                g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
                g2.drawLine(300, 80, 300, 900);
                g2.drawLine(0, 80, 1600, 80);
                super.paint(g);
            }
        };

        JPanel timerPanel = new JPanel();
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel);
        timerPanel.add(pauseLabel);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(timerPanel, BorderLayout.EAST);

        JPanel quizPanel = new JPanel(new BorderLayout(30,10));
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(170, 300));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        quizPanel.add(examTitleLabel, BorderLayout.NORTH);
        quizPanel.add(scrollPane, BorderLayout.EAST);
        quizPanel.setOpaque(false);
        quizPanel.setBorder(new EmptyBorder(0,90,50,0));

        JPanel printPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new BorderLayout());


        JPanel topButton = new JPanel();
        topButton.add(previousButton);
        topButton.add(nextButton);
        topButton.setOpaque(false);
        topButton.setBorder(new EmptyBorder(20,0,0,20));

        JPanel CheckBoxPanel = new JPanel();
        CheckBoxPanel.setOpaque(false);

        JPanel topButtonPanel = new JPanel(new GridLayout(0,1));
        topButtonPanel.add(topButton);
        topButtonPanel.add(CheckBoxPanel);
        topButtonPanel.setOpaque(false);


        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.add(exitButton);
        bottomButtonPanel.add(submitButton);
        bottomButtonPanel.setOpaque(false);
        bottomButtonPanel.setBorder(new EmptyBorder(0,0,20,20));

        buttonPanel.add(topButtonPanel, BorderLayout.NORTH);
        buttonPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        buttonPanel.setOpaque(false);

        printPanel.setOpaque(false);
        printPanel.setBorder(new EmptyBorder(0,20,0,0));

        cardPanel = new JPanel(card);

        for(int i = 0; i < subject.getQuizzes().size(); i++) {
            cardPanel.add(String.valueOf(i), new ExamPanel(subject, i));
        }
        cardPanel.setOpaque(false);

        printPanel.add(cardPanel);
        printPanel.add(buttonPanel, BorderLayout.EAST);

        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(quizPanel, BorderLayout.WEST);
        centerPanel.add(printPanel);
        add(centerPanel);
    }

    protected void setFrame() {
        setSize(1600, 900);
        super.setFrame();
        setVisible(true);
    }

    private void setAction() {
        ListSelectionListener listSelectionListener = e -> {
            if(!e.getValueIsAdjusting()) {
                change(list.getSelectedIndex());
            }
        };

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                if (label == nextButton && list.getSelectedIndex() < list.getModel().getSize() - 1) {
                    change(list.getSelectedIndex() + 1);
                }

                if (label == previousButton && list.getSelectedIndex() > 0) {
                    change(list.getSelectedIndex() - 1);
                }
            }
        };

        nextButton.addMouseListener(mouseListener);
        previousButton.addMouseListener(mouseListener);
        list.addListSelectionListener(listSelectionListener);
        pauseLabel.addMouseListener(mouseListener);
        submitButton.addMouseListener(mouseListener);
        exitButton.addMouseListener(mouseListener);


    }

    private void change(int index) {
        list.setSelectedIndex(index);
        card.show(cardPanel, String.valueOf(index));
    }

    private class ExamPanel extends JPanel {
        public ExamPanel(Subject subject, int index) {
            try {
                JPanel quizTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JPanel panel = new JPanel(new BorderLayout());
                JPanel numPanel = new JPanel(new GridLayout(0,1, 0, 20));

                setLayout(new BorderLayout());
                JLabel quizTitleLabel = new CustomTextLabel(index + 1 + "번. " + subject.getQuizzes().get(index).getTitle(), JLabel.LEFT);

                JLabel[] printLabel = new JLabel[subject.getQuizzes().get(index).getFollowing().size()];

                for(int i = 0; i < subject.getQuizzes().get(index).getFollowing().size(); i++) {
                    if(subject.getQuizzes().get(index).getFollowing().get(i).contains(".png") || subject.getQuizzes().get(index).getFollowing().get(i).contains(".jpg")) {
                        Image img = getToolkit().getImage(subject.getQuizzes().get(index).getFollowing().get(i))
                                .getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(img); // 사이즈 조정
                        printLabel[i] = new JLabel(icon);
                    } else {
                        printLabel[i] = new CustomTextLabel(subject.getQuizzes().get(index).getFollowing().get(i));
                    }
                }

                JLabel[] numLabel = new JLabel[subject.getQuizzes().get(index).getTexts().size()];

                JPanel[] numButtonPanel = new NumButtonPanel[subject.getQuizzes().get(index).getTexts().size()];
                for (int i = 0; i < subject.getQuizzes().get(index).getTexts().size(); i++) {
                    numButtonPanel[i] = new NumButtonPanel(subject.getQuizzes().get(index), i);

                    for(int y = 0; y < subject.getQuizzes().get(index).getTexts().size(); y++) {
                        if(subject.getQuizzes().get(index).getTexts().get(y).contains(".png") || subject.getQuizzes().get(index).getTexts().get(y).contains(".jpg")) {
                            Image img = getToolkit().getImage(subject.getQuizzes().get(index).getTexts().get(y))
                                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            ImageIcon icon = new ImageIcon(img); // 사이즈 조정
                            numLabel[y] = new JLabel(icon);
                        } else {
                            numLabel[y] = new CustomTextLabel(subject.getQuizzes().get(index).getTexts().get(y));
                        }
                    }
                }
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setPreferredSize(new Dimension(500, 330));
                scrollPane.setViewportView(numPanel);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);

                quizTitlePanel.setOpaque(false);
                imgPanel.setOpaque(false);
                panel.setOpaque(false);
                numPanel.setOpaque(false);


                quizTitlePanel.add(quizTitleLabel);

                JPanel topPanel = new JPanel(new BorderLayout());
                topPanel.add(quizTitlePanel, BorderLayout.WEST);
                topPanel.setOpaque(false);


                panel.add(topPanel, BorderLayout.NORTH);

                for(JLabel label : printLabel) {
                    imgPanel.add(label);
                }
                panel.add(imgPanel);

                add(panel);
                add(scrollPane, BorderLayout.SOUTH);

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
            //setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public static class TransparentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setForeground(Color.WHITE);
            setOpaque(isSelected);
            return this;
        }
    }
}
class CustomIconLabel extends JLabel {
    public CustomIconLabel(String text, ImageIcon img) {
        setIcon(img);
        setText(text);
        setFont(new Font("휴먼엑스포", Font.PLAIN,15));
        setForeground(Color.WHITE);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setPressIcon(ImageIcon img) {
        Icon icon = getIcon();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setIcon(img);
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                setIcon(icon);
            }
        });
    }
    public void setEnterIcon(ImageIcon img) {
        Icon icon = getIcon();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setIcon(img);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(icon);
            }
        });
    }
}


class CustomTextLabel extends JLabel {
    public CustomTextLabel(String text) {
        setText(text);
        setFontSize(15);
        setForeground(Color.WHITE);
    }
    public CustomTextLabel(String text, int alignment) {
        this(text);
        setHorizontalAlignment(alignment);
    }

    public void setFontSize(int size) {
        super.setFont(new Font("휴먼엑스포", Font.PLAIN, size));
    }
}

// 타이머
class TimerLabel extends JLabel implements Runnable {
    private SimpleDateFormat sdf = new SimpleDateFormat("mm : ss");
    private Date date;
    private boolean stop;
    private ExamFrame owner;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getTime() {
        return (int) (date.getTime() / 1000L);
    }

    public TimerLabel(ExamFrame owner, int second) {
        this.owner = owner;
        date = new Date(second * 1000L);
        stop = true;
        setForeground(Color.WHITE);
        setText(sdf.format(date));
        setOpaque(false);
        setFont(new Font("휴먼엑스포", Font.PLAIN, 30));
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);	// 1초

                if (date.getTime() > 0) {
                    date.setTime(date.getTime() - 1000);
                    setText(sdf.format(date));
                } else {
                    //owner.timeOut();
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
