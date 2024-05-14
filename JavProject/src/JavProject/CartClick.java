package JavProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CartClick extends JFrame {

    private static CartClick instance; // 싱글톤 인스턴스
    private JPanel menuPanel;
    private JCheckBox selectAllCheckBox;
    private List<String> cartItems;
    private List<Integer> cartPrices;

    // 싱글톤 패턴을 위한 private 생성자
CartClick() {
        setTitle("장바구니");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);

        cartItems = new ArrayList<>();
        cartPrices = new ArrayList<>();


        // 상단 패널 설정
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel titleLabel = new JLabel(" 주문메뉴");
        titleLabel.setFont(titleLabel.getFont().deriveFont(25.0f));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // 전체선택 패널 설정
        JPanel selectAllPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectAllCheckBox = new JCheckBox("전체선택");
        selectAllPanel.add(selectAllCheckBox);
        topPanel.add(selectAllPanel, BorderLayout.WEST);

        // 버튼 생성
        JButton deleteSelectedButton = new JButton("삭제");

        // 버튼 위치 & 크기 설정
        deleteSelectedButton.setBounds(600, 18, 85, 25);

        // 프레임 버튼 추가
        topPanel.add(deleteSelectedButton);

        // 스크롤 패널 설정
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(15, 1, 0, 1));
        menuPanel.setPreferredSize(new Dimension(600, 2000));
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        // "전체선택" 체크박스의 액션 리스너
        selectAllCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = selectAllCheckBox.isSelected();
                Component[] components = menuPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        JPanel boxPanel = (JPanel) component;
                        Component[] boxComponents = boxPanel.getComponents();
                        for (Component boxComponent : boxComponents) {
                            if (boxComponent instanceof JCheckBox) {
                                JCheckBox checkBox = (JCheckBox) boxComponent;
                                checkBox.setSelected(isSelected);
                            }
                        }
                    }
                }
            }
        });

        // "선택삭제" 버튼의 액션 리스너
        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean hasSelectedItems = false; // 선택된 항목이 있는지 여부를 나타내는 플래그
                Component[] components = menuPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        JPanel menuBox = (JPanel) component;
                        JCheckBox checkBox = (JCheckBox) menuBox.getComponent(1); // 체크박스 가져오기
                        if (checkBox.isSelected()) {
                            hasSelectedItems = true;
                            menuPanel.remove(menuBox); 
                        }
                    }
                }
                menuPanel.revalidate();
                menuPanel.repaint();

                // 선택된 항목이 없는 경우 알림을 표시
                if (!hasSelectedItems) {
                    JOptionPane.showMessageDialog(null, "장바구니에서 삭제하실 메뉴를 선택해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 주문하기
        JButton orderButton = new JButton("주문하기");
        orderButton.setFont(orderButton.getFont().deriveFont(16.0f));
        orderButton.setPreferredSize(new Dimension(680, 40));
     // 주문하기 버튼의 액션 리스너
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalPrice = calculateTotalBasicPrice(); 
                String message = "총 금액: " + totalPrice + "원\n결제하시겠습니까?";
                int result = JOptionPane.showConfirmDialog(null, message, "주문 확인", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    new PayClick().setVisible(true); 
                } else {
                    // 아니오
                }
            }
        });

        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        orderPanel.add(orderButton);
        topPanel.add(orderPanel, BorderLayout.SOUTH);
        getContentPane().add(topPanel);
        setVisible(true);
    }

    // 싱글톤 인스턴스를 반환하는 메서드
    public static CartClick getInstance() {
        if (instance == null) {
            instance = new CartClick();
        }
        return instance;
    }
   

 // 장바구니에 메뉴를 추가하는 내부 메서드
    void addToCartInternal(String menu, ImageIcon icon, int price) {
        // 메뉴 정보를 표시하는 JLabel 생성
        JLabel menuLabel = new JLabel(menu);

        // 이미지 아이콘 설정
        menuLabel.setIcon(icon);

        // 박스 패널 생성
        JPanel boxPanel = new JPanel(new BorderLayout());
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 체크박스 생성
        JCheckBox checkBox = new JCheckBox();

        // 메뉴 정보와 이미지, 체크박스를 포함하는 패널 생성
        boxPanel.add(menuLabel, BorderLayout.CENTER);
        boxPanel.add(checkBox, BorderLayout.WEST);

        // 장바구니에 박스 패널을 추가
        menuPanel.add(boxPanel);
        cartItems.add(menu);
        cartPrices.add(price); 

        // 장바구니 패널을 다시 그리기
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    
    private int calculateTotalBasicPrice() {
        int totalPrice = 0;
        Component[] components = menuPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel menuBox = (JPanel) component;
                JCheckBox checkBox = (JCheckBox) menuBox.getComponent(1); // 체크된 항목만 총액 계산하도록
                if (checkBox.isSelected()) {
                    int index = menuPanel.getComponentZOrder(menuBox);
                    totalPrice += cartPrices.get(index);
                }
            }
        }
        return totalPrice;
    }

}