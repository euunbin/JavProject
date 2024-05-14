package JavProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayClick extends JFrame {

    public PayClick() {
        setTitle("PayClick");
        setSize(300, 200); // 프레임 크기 변경
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton cardButton = new JButton("카드");
        cardButton.addActionListener(new PaymentActionListener());

        JButton cashButton = new JButton("현금");
        cashButton.addActionListener(new PaymentActionListener());

        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(100, 150); 
        cardButton.setPreferredSize(buttonSize);
        cashButton.setPreferredSize(buttonSize);

        // 수평 박스 생성하여 버튼 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        buttonPanel.add(cardButton);
        buttonPanel.add(cashButton);

        // 프레임의 중앙에 버튼 패널 추가
        add(buttonPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null); // 화면 중앙에 프레임 배치
    }

    private class PaymentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 버튼 이름으로 결제 종류를 판별
            String paymentType = e.getActionCommand();

            // 진행 중 메시지
            JOptionPane progressDialog = new JOptionPane(paymentType + " 결제 진행 중", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = progressDialog.createDialog(null, paymentType + " 결제 진행 중");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(false); 
            dialog.setVisible(true);

            // 스레드 생성하여 0.5초마다 "." 추가
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 4; i++) { // 4초 동안 반복
                        try {
                            Thread.sleep(500); // 0.5초 대기
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.setMessage(paymentType + " 결제 진행 중" + "....".substring(0, i + 1));
                    }

                    // 결제 완료 메시지
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dispose(); // 결제 진행 중 대화상자 닫기
                            JOptionPane.showMessageDialog(null, paymentType + " 결제 완료!");
                        }
                    });
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PayClick().setVisible(true);
            }
        });
    }
}