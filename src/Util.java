import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.Closeable;
import java.io.IOException;

public class Util {
    public static void closeAll(Closeable... c) {
        for(Closeable closeable : c) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 이미지 크기조절
    public static JLabel setImageSize(String imgName, int width, int height) {
        ImageIcon icon = new ImageIcon(imgName);
        Image img = icon.getImage();
        Image changeImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);
        return new JLabel(changeIcon);
    }
    public static ImageIcon setImageIcon(String imgName, int width, int height) {
        ImageIcon icon = new ImageIcon(imgName);
        Image img = icon.getImage();
        Image changeImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);
        return changeIcon;
    }

    // 공백확인
    public static boolean isEmpty(JTextComponent input) {
        String text = input.getText().trim();
        return (text.length() == 0) ? true : false;
    }

    // 표 사이즈 일정하게
    public static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 20; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}
