package JavProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CafeKiosk extends JFrame {

    // 메뉴 설정 부분
    String[][] menuData = {
            {"아메리카노", "카페라떼", "바닐라라떼", "카페모카"},
            {"딸기스무디", "얼그레이티", "자몽에이드", "아이스티"},
            {"샌드위치", "쿠키", "케이크", "와플"}
    };

    int[][] menuPrices = {
            {4000, 5000, 5500, 5500},
            {6000, 4500, 5500, 4000},
            {7000, 2000, 6000, 5000}
    };

    JLabel[][] menuLabels;

    public CafeKiosk() {
        setTitle("카페 주문 프로젝트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);

        // 상단 패널 설정
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel titleLabel = new JLabel("카페 주문 프로젝트");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // 장바구니 이미지 설정
        JLabel cartLabel = new JLabel();
        ImageIcon cartIcon = new ImageIcon("images/장바구니.jpg");
        Image cartImage = cartIcon.getImage().getScaledInstance(cartIcon.getIconWidth() * 7 / 100, cartIcon.getIconHeight() * 7 / 100, Image.SCALE_SMOOTH);

        cartLabel.setIcon(new ImageIcon(cartImage));

        // 카트 클릭 시 CartClick 열기
        cartLabel.addMouseListener(new MouseAdapter() {
            private CartClick cartClickInstance;

			@Override
			//한 창의 장바구니 페이지로만 사용되도록
            public void mouseClicked(MouseEvent e) {
                if (cartClickInstance == null) {
                    cartClickInstance = CartClick.getInstance();
                } else {
                    cartClickInstance.setVisible(true);
                }
            }
        });

        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartPanel.add(cartLabel);
        topPanel.add(cartPanel, BorderLayout.SOUTH);

        // 메뉴 탭 설정
        JTabbedPane tabbedPane = new JTabbedPane();

        menuLabels = new JLabel[menuData.length][];

        for (int i = 0; i < menuData.length; i++) {
            menuLabels[i] = new JLabel[menuData[i].length];
            JPanel menuPanel = new JPanel(new GridLayout(0, 3));
            for (int j = 0; j < menuData[i].length; j++) {
                menuLabels[i][j] = createMenuLabel(menuData[i][j], menuPrices[i][j], "images/" + menuData[i][j].toLowerCase() + ".jpg");
                menuLabels[i][j].setName(i + "_" + j);
             // 메뉴 클릭 이벤트 등록
                menuLabels[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel label = (JLabel) e.getSource();
                        String[] parts = label.getName().split("_");
                        int categoryIndex = Integer.parseInt(parts[0]);
                        int itemIndex = Integer.parseInt(parts[1]);
                        String menuName = menuData[categoryIndex][itemIndex];
                        int menuPrice = menuPrices[categoryIndex][itemIndex];
                        // 메뉴 클릭 시 MenuClick 열기
                        new MenuClick(menuName, menuPrice).setVisible(true);
                    }
                });


                menuPanel.add(menuLabels[i][j]);
            }
            JScrollPane menuScrollPane = new JScrollPane(menuPanel);
            tabbedPane.addTab(getCategoryName(i), menuScrollPane);
        }

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // 메뉴 레이블 생성 메서드
    private JLabel createMenuLabel(String menuName, int price, String imagePath) {
        JLabel label = new JLabel("<html><center><b>" + menuName + "</b><br>" + price + "원</center><br></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(image));
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        return label;
    }

    private String getCategoryName(int index) {
        switch (index) {
            case 0:
                return "커피";
            case 1:
                return "논커피";
            case 2:
                return "사이드";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CafeKiosk::new);
    }
}