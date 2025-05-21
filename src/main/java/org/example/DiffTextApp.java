package org.example;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class DiffTextApp extends JFrame {

    private JTextArea textAreaA;
    private JTextArea textAreaB;
    private JTextPane resultPane;

    public DiffTextApp() {
        setTitle("テキスト比較ツール 💗");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 上部の入力エリア（AとB）
        textAreaA = new JTextArea(10, 30);
        textAreaB = new JTextArea(10, 30);
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(new JScrollPane(textAreaA));
        inputPanel.add(new JScrollPane(textAreaB));
        add(inputPanel, BorderLayout.NORTH);

        // 結果エリア
        resultPane = new JTextPane();
        resultPane.setEditable(false);
        add(new JScrollPane(resultPane), BorderLayout.CENTER);

        // ボタン
        JButton compareButton = new JButton("💗 比較する 💗");
        compareButton.addActionListener(e -> compareText());
        add(compareButton, BorderLayout.SOUTH);
    }

    private void compareText() {
        String[] linesA = textAreaA.getText().split("\n");
        String[] linesB = textAreaB.getText().split("\n");

        StyledDocument doc = resultPane.getStyledDocument();
        StyleContext context = new StyleContext();
        Style pinkStyle = context.addStyle("pink", null);
        StyleConstants.setForeground(pinkStyle, Color.PINK);

        doc.removeUndoableEditListener(e -> {}); // 一応リスナクリア
        resultPane.setText(""); // 初期化

        int max = Math.max(linesA.length, linesB.length);
        for (int i = 0; i < max; i++) {
            String lineA = i < linesA.length ? linesA[i] : "";
            String lineB = i < linesB.length ? linesB[i] : "";

            String lineNumber = String.format("行 %d: ", i + 1);
            String output;

            if (lineA.equals(lineB)) {
                output = lineNumber + "同じだよ💮\n";
                try {
                    doc.insertString(doc.getLength(), output, null);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            } else {
                output = lineNumber + "ちがうよ！\n  A: " + lineA + "\n  B: " + lineB + "\n";
                try {
                    doc.insertString(doc.getLength(), output, pinkStyle); // ピンク！
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiffTextApp().setVisible(true));
    }
}
