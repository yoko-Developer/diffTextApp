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
        setTitle("Diff 比較ツール 💗");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 左右に並べる入力欄
        textAreaA = createPlaceholderTextArea("Text A");
        textAreaB = createPlaceholderTextArea("Text B");

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("入力欄"));
        inputPanel.add(new JScrollPane(textAreaA));
        inputPanel.add(new JScrollPane(textAreaB));
        add(inputPanel, BorderLayout.NORTH);

        // 結果欄（大きめ＆スクロール）
        resultPane = new JTextPane();
        resultPane.setEditable(false);
        resultPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane resultScroll = new JScrollPane(resultPane);
        resultScroll.setPreferredSize(new Dimension(750, 400));
        resultScroll.setBorder(BorderFactory.createTitledBorder("Diff 結果"));
        add(resultScroll, BorderLayout.CENTER);

        // ボタン（中央・小さめ）
        JButton compareButton = new JButton("💗 比較する 💗");
        compareButton.setPreferredSize(new Dimension(150, 40));
        compareButton.setBackground(new Color(255, 182, 193)); // LightPink
        compareButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(compareButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ボタン押下時の動作
        compareButton.addActionListener(e -> compareText());
    }

    private JTextArea createPlaceholderTextArea(String placeholder) {
        JTextArea area = new JTextArea(10, 30);
        area.setFont(new Font("Yu Gothic", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(placeholder);
        area.setForeground(Color.GRAY);

        // プレースホルダっぽい動き
        area.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(Color.GRAY);
                }
            }
        });

        return area;
    }

    private void compareText() {
        String[] linesA = textAreaA.getText().split("\n");
        String[] linesB = textAreaB.getText().split("\n");

        StyledDocument doc = resultPane.getStyledDocument();
        StyleContext context = new StyleContext();
        Style pinkStyle = context.addStyle("pink", null);
        StyleConstants.setForeground(pinkStyle, Color.PINK);
        Style normalStyle = context.addStyle("normal", null);
        StyleConstants.setForeground(normalStyle, Color.BLACK);

        resultPane.setText("");

        int max = Math.max(linesA.length, linesB.length);
        for (int i = 0; i < max; i++) {
            String lineA = i < linesA.length ? linesA[i] : "";
            String lineB = i < linesB.length ? linesB[i] : "";

            try {
                doc.insertString(doc.getLength(), String.format("行 %d:\n", i + 1), normalStyle);
                insertCharDiff(lineA, lineB, doc, normalStyle, pinkStyle);
                doc.insertString(doc.getLength(), "\n\n", normalStyle);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertCharDiff(String a, String b, StyledDocument doc, Style normal, Style pink) {
        int maxLen = Math.max(a.length(), b.length());

        try {
            doc.insertString(doc.getLength(), " A: ", normal);
            for (int i = 0; i < maxLen; i++) {
                String ch = i < a.length() ? String.valueOf(a.charAt(i)) : " ";
                Style style = (i < b.length() && i < a.length() && a.charAt(i) == b.charAt(i)) ? normal : pink;
                doc.insertString(doc.getLength(), ch, style);
            }
            doc.insertString(doc.getLength(), "\n", normal);

            doc.insertString(doc.getLength(), " B: ", normal);
            for (int i = 0; i < maxLen; i++) {
                String ch = i < b.length() ? String.valueOf(b.charAt(i)) : " ";
                Style style = (i < b.length() && i < a.length() && a.charAt(i) == b.charAt(i)) ? normal : pink;
                doc.insertString(doc.getLength(), ch, style);
            }
            doc.insertString(doc.getLength(), "\n", normal);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiffTextApp().setVisible(true));
    }
}
