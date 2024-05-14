package JavProject;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class MembershipClick extends JFrame {
    private JTextField memberIdField;
    private JButton saveButton;
    private Map<Long, Member> members;

    public MembershipClick() {
        setTitle("Cafe Membership App");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel messageLabel = new JLabel("번호를 입력하세요");
        panel.add(messageLabel, gbc);

        memberIdField = new JTextField(11); // 길이를 11로 지정
        ((AbstractDocument) memberIdField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                currentText += text;
                if (currentText.length() <= 11 && currentText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        gbc.gridy++;
        panel.add(memberIdField, gbc);

        saveButton = new JButton("적립");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        members = new HashMap<>();

        // 적립 버튼 클릭 시 이벤트 처리
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberIdText = memberIdField.getText();
                if (!memberIdText.isEmpty()) {
                    long memberId = Long.parseLong(memberIdText); 
                    if (members.containsKey(memberId)) {
                        Member member = members.get(memberId);
                        JOptionPane.showMessageDialog(null, "다시 만나서 반갑습니다!\n 500원이 적립되었습니다. 현재 누적 포인트: " + member.getPoints(), "알림", JOptionPane.INFORMATION_MESSAGE);
                        accumulatePoints(memberId, 500);
                    } else {
                        members.put(memberId, new Member(memberId, 0));
                        JOptionPane.showMessageDialog(null, "처음 뵙겠습니다!\n 500원이 적립되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        accumulatePoints(memberId, 500);
                    }
                    memberIdField.setText(""); // 입력 필드 초기화
                } else {
                    JOptionPane.showMessageDialog(null, "회원 번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // 적립 처리 메서드
    private void accumulatePoints(long memberId, int points) {
        Member member = members.get(memberId);
        if (member != null) {
            member.accumulatePoints(points); // 현재 누적 포인트에 새로운 포인트를 더함
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MembershipClick app = new MembershipClick();
                app.setVisible(true);
            }
        });
    }
}