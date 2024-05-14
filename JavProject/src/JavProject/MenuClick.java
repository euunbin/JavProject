package JavProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuClick extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;
    private JPanel optionsPanel;
    private int price;
    private String menuName;
    private int iceQuantity = 0;
    private int syrupQuantity = 0;
    private int shotQuantity = 0;
    private boolean sizeUpgraded = false;
    private boolean isHotSelected = false;

    public MenuClick(String menuName, int price) {
        this.menuName = menuName;
        this.price = price;

        setTitle(menuName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 800);

        createComponents(); // GUI 구성 요소 생성 및 배치
        setVisible(true); // 창 표시
    }

    private void createComponents() {
        // 이미지 라벨 생성 및 설정
        ImageIcon icon = new ImageIcon("images/" + menuName.toLowerCase() + ".jpg");
        Image image = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 이미지와 텍스트를 담을 패널 생성
        JPanel imageTextPanel = new JPanel(new BorderLayout());
        imageTextPanel.add(imageLabel, BorderLayout.NORTH);
        imageTextPanel.add(new JLabel(menuName, SwingConstants.CENTER), BorderLayout.CENTER);
        imageTextPanel.add(new JLabel(price + "원", SwingConstants.CENTER), BorderLayout.SOUTH);
        imageTextPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // 옵션을 추가하는 패널
        optionsPanel = createOptionsPanel();

     // 장바구니에 음료 추가
        JButton orderButton = new JButton("주문하기");
     // 주문 버튼의 ActionListener 수정
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 주문 정보 작성
                StringBuilder message = new StringBuilder();
                message.append("- 메뉴:").append(menuName).append("\n");
                message.append("- 옵션:\n");
                message.append("  - 온도: ").append(isHotSelected ? "Hot" : "Ice").append("\n");
                if (syrupQuantity > 0) {
                    message.append("  - 시럽 추가: ").append(syrupQuantity).append("번\n");
                }
                if (shotQuantity > 0) {
                    message.append("  - 샷 추가: ").append(shotQuantity).append("번\n");
                }
                if (sizeUpgraded) {
                    message.append("  - 사이즈 업그레이드\n");
                }
                message.append("  - 얼음 추가: ").append(iceQuantity).append("개\n");
                message.append("- 가격: ").append(calculateTotalPrice()).append("원");

                // 이미지 크기 조절
                ImageIcon originalIcon = new ImageIcon("images/" + menuName.toLowerCase() + ".jpg");
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                //장바구니에 음료 추가
                CartClick cartClickInstance = CartClick.getInstance();
                cartClickInstance.addToCartInternal(message.toString(), scaledIcon, calculateTotalPrice());

                // 주문 정보 출력
                JOptionPane.showMessageDialog(null, "장바구니에 메뉴가 추가되었습니다.");

                dispose(); 

            }
        });






        // 컨텐츠 패널에 구성 요소 추가
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(imageTextPanel, BorderLayout.NORTH);
        contentPane.add(optionsPanel, BorderLayout.CENTER);
        contentPane.add(orderButton, BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    // 옵션을 추가하는 패널 생성 메서드
    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel(new GridLayout(6, 1)); // 6개의 옵션을 세로로 배열

        // 온도 선택 라디오 버튼 패널
        JPanel temperaturePanel = new JPanel();
        temperaturePanel.setBorder(BorderFactory.createTitledBorder("온도 선택"));

        JRadioButton iceRadioButton = new JRadioButton("Ice");
        JRadioButton hotRadioButton = new JRadioButton("Hot");

        ButtonGroup temperatureGroup = new ButtonGroup();
        temperatureGroup.add(iceRadioButton);
        temperatureGroup.add(hotRadioButton);
        temperaturePanel.add(iceRadioButton);
        temperaturePanel.add(hotRadioButton);

        iceRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isHotSelected = false;
            }
        });

        hotRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isHotSelected = true;
            }
        });

        optionsPanel.add(temperaturePanel);

        // 시럽 추가 옵션
        JPanel syrupPanel = new JPanel(new FlowLayout());
        JLabel syrupLabel = new JLabel("시럽 추가: ");
        JButton syrupAddButton = new JButton("+");
        JButton syrupMinusButton = new JButton("-");
        JTextField syrupTextField = new JTextField("0", 5);
        syrupTextField.setEditable(false);
        syrupAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syrupQuantity++;
                syrupTextField.setText(Integer.toString(syrupQuantity));
            }
        });
        syrupMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (syrupQuantity > 0) {
                    syrupQuantity--;
                    syrupTextField.setText(Integer.toString(syrupQuantity));
                }
            }
        });
        syrupPanel.add(syrupLabel);
        syrupPanel.add(syrupMinusButton);
        syrupPanel.add(syrupTextField);
        syrupPanel.add(syrupAddButton);
        optionsPanel.add(syrupPanel);

        // 샷 추가 옵션
        JPanel shotPanel = new JPanel(new FlowLayout());
        JLabel shotLabel = new JLabel("샷 추가: ");
        JButton shotAddButton = new JButton("+");
        JButton shotMinusButton = new JButton("-");
        JTextField shotTextField = new JTextField("0", 5);
        shotTextField.setEditable(false);
        shotAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shotQuantity++;
                shotTextField.setText(Integer.toString(shotQuantity));
            }
        });
        shotMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shotQuantity > 0) {
                    shotQuantity--;
                    shotTextField.setText(Integer.toString(shotQuantity));
                }
            }
        });
        shotPanel.add(shotLabel);
        shotPanel.add(shotMinusButton);
        shotPanel.add(shotTextField);
        shotPanel.add(shotAddButton);
        optionsPanel.add(shotPanel);

        // 얼음 수량을 조절하는 패널
        JPanel iceQuantityPanel = new JPanel(new FlowLayout());
        JLabel iceLabel = new JLabel("얼음 수량: ");
        JButton iceAddButton = new JButton("+");
        JButton iceMinusButton = new JButton("-");
        JTextField iceTextField = new JTextField("0", 5);
        iceTextField.setEditable(false);
        iceAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iceQuantity++;
                iceTextField.setText(Integer.toString(iceQuantity));
            }
        });
        iceMinusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iceQuantity > 0) {
                    iceQuantity--;
                    iceTextField.setText(Integer.toString(iceQuantity));
                }
            }
        });
        iceQuantityPanel.add(iceLabel);
        iceQuantityPanel.add(iceMinusButton);
        iceQuantityPanel.add(iceTextField);
        iceQuantityPanel.add(iceAddButton);
        optionsPanel.add(iceQuantityPanel);

        // 사이즈 추가 옵션
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("사이즈 업그레이드: ");
        JCheckBox sizeCheckbox = new JCheckBox();
        sizeCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeUpgraded = sizeCheckbox.isSelected();
            }
        });
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeCheckbox);
        optionsPanel.add(sizePanel);

        return optionsPanel;
    }

    // 총 가격을 계산하는 메서드
    private int calculateTotalPrice() {
        int totalPrice = price; // 기본 가격으로 초기화
        return totalPrice;
    }
}
