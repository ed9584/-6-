import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 교내 스마트 시설 및 비품 민원 시스템 - 자바 데스크톱 GUI 프로토타입
 * (Java Swing 기반 단일 실행 파일)
 */
public class ComplaintSystemPrototype extends JFrame {

    // 데이터 모델
    static class Complaint {
        int id;
        String userName;
        String location;
        String itemCategory;
        String failureReason;
        String content;
        String status; // 접수됨, 처리 중, 완료
        int likeCount;
        String adminComment;
        String createdAt;

        public Complaint(int id, String userName, String location, String itemCategory, String failureReason, String content) {
            this.id = id;
            this.userName = userName;
            this.location = location;
            this.itemCategory = itemCategory;
            this.failureReason = failureReason;
            this.content = content;
            this.status = "접수됨";
            this.likeCount = 0;
            this.adminComment = "-";
            this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        public Complaint(int id, String userName, String location, String itemCategory, String failureReason) {
            this(id, userName, location, itemCategory, failureReason, "");
        }
    }

    private List<Complaint> complaintList = new ArrayList<>();
    private int nextId = 1;

    // UI 컴포넌트 - 유저
    private JTextField txtUserName;
    private JComboBox<String> cbLocation;
    private JComboBox<String> cbItemCategory;
    private JComboBox<String> cbFailureReason;
    private JTextArea txtContent;
    private JTable userTable;
    private DefaultTableModel userTableModel;

    // UI 컴포넌트 - 관리자
    private JTable adminTable;
    private DefaultTableModel adminTableModel;
    private JComboBox<String> cbStatusUpdate;
    private JTextField txtAdminComment;
    private JTextArea txtStatsSummary;

    public ComplaintSystemPrototype() {
        setTitle("스마트 교내 시설/비품 민원 시스템 (자바 PC 데스크톱 프로토타입)");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 기본 샘플 데이터 입력
        initSampleData();

        // 메인 탭 구성
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("학생/사용자 민원 창구", createUserPanel());
        tabbedPane.addTab("관리자 대시보드 & 통계", createAdminPanel());

        add(tabbedPane);
        refreshTables();
    }

    private void initSampleData() {
        Complaint c1 = new Complaint(nextId++, "김철수", "정보공학관 501호", "모니터", "노후화", "화면이 지속적으로 깜빡입니다. 교체가 필요해보입니다.");
        c1.likeCount = 5;

        Complaint c2 = new Complaint(nextId++, "이영희", "도서관 3층 열람실", "의자", "파손", "의자 바퀴 하나가 빠져서 이용 시 위험합니다.");
        c2.likeCount = 12;

        Complaint c3 = new Complaint(nextId++, "박민수", "정보공학관 501호", "모니터", "노후화", "모니터 전원이 들어오지 않습니다.");
        c3.likeCount = 3;

        complaintList.add(c1);
        complaintList.add(c2);
        complaintList.add(c3);
    }

    // [1] 사용자 패널 생성
    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 입력 폼
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("신규 민원 접수"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUserName = new JTextField(10);
        cbLocation = new JComboBox<>(new String[]{"정보공학관 501호", "정보공학관 502호", "도서관 3층 열람실", "학생회관 식당", "공학관 201호"});
        cbItemCategory = new JComboBox<>(new String[]{"모니터", "마우스", "키보드", "책상", "의자", "빔프로젝터", "에어컨"});
        cbFailureReason = new JComboBox<>(new String[]{"노후화", "파손", "초기불량", "기타/미상"});

        txtContent = new JTextArea(3, 20);
        txtContent.setLineWrap(true);
        JScrollPane scrollContent = new JScrollPane(txtContent);

        JButton btnSubmit = new JButton("민원 제출하기");
        btnSubmit.setBackground(new Color(41, 128, 185));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("작성자:"), gbc);
        gbc.gridx = 1; formPanel.add(txtUserName, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("장소:"), gbc);
        gbc.gridx = 3; formPanel.add(cbLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("비품 항목:"), gbc);
        gbc.gridx = 1; formPanel.add(cbItemCategory, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("고장 원인:"), gbc);
        gbc.gridx = 3; formPanel.add(cbFailureReason, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("상세 내용:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(scrollContent, gbc);

        gbc.gridx = 3; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(btnSubmit, gbc);

        // 제출 버튼 이벤트
        btnSubmit.addActionListener(e -> {
            String name = txtUserName.getText().trim();
            String content = txtContent.getText().trim();
            if (name.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "작성자와 상세 내용을 모두 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (content.length() > 300) {
                JOptionPane.showMessageDialog(this, "상세 내용은 300자 이내로 작성해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Complaint complaint = new Complaint(
                    nextId++,
                    name,
                    (String) cbLocation.getSelectedItem(),
                    (String) cbItemCategory.getSelectedItem(),
                    (String) cbFailureReason.getSelectedItem(),
                    content
            );
            complaintList.add(complaint);
            refreshTables();

            txtUserName.setText("");
            txtContent.setText("");
            JOptionPane.showMessageDialog(this, "민원이 성공적으로 접수되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
        });

        // 민원 목록 테이블
        String[] columns = {"ID", "장소", "비품", "고장원인", "상세내용", "상태", "공감(고쳐줘요)", "작성일시"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(userTableModel);
        JScrollPane scrollTable = new JScrollPane(userTable);

        // 공감 버튼
        JButton btnLike = new JButton("👍 고쳐줘요! (공감하기)");
        btnLike.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        btnLike.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "공감할 민원 항목을 목록에서 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int complaintId = (int) userTableModel.getValueAt(selectedRow, 0);
            for (Complaint c : complaintList) {
                if (c.id == complaintId) {
                    c.likeCount++;
                    break;
                }
            }
            refreshTables();
            JOptionPane.showMessageDialog(this, "해당 민원에 공감(고쳐줘요!)을 표시했습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnLike);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollTable, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // [2] 관리자 패널 생성
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 통계 및 노후화 예측 요약 영역
        txtStatsSummary = new JTextArea(5, 40);
        txtStatsSummary.setEditable(false);
        txtStatsSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStatsSummary.setBackground(new Color(245, 245, 245));
        JScrollPane scrollStats = new JScrollPane(txtStatsSummary);
        scrollStats.setBorder(BorderFactory.createTitledBorder("📊 고장 통계 분석 & 비품 노후화 사전 예측 경고"));

        // 관리자용 민원 목록 테이블
        String[] columns = {"ID", "작성자", "장소", "비품", "고장원인", "상태", "공감수", "관리자 답변"};
        adminTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        adminTable = new JTable(adminTableModel);
        JScrollPane scrollAdminTable = new JScrollPane(adminTable);

        // 상태 변경 컨트롤 패널
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("민원 상태 및 처리 답변 업데이트"));

        cbStatusUpdate = new JComboBox<>(new String[]{"접수됨", "처리 중", "완료"});
        txtAdminComment = new JTextField(25);
        JButton btnUpdateStatus = new JButton("상태/답변 반영");

        controlPanel.add(new JLabel("변경 상태:"));
        controlPanel.add(cbStatusUpdate);
        controlPanel.add(new JLabel("처리 답변:"));
        controlPanel.add(txtAdminComment);
        controlPanel.add(btnUpdateStatus);

        btnUpdateStatus.addActionListener(e -> {
            int selectedRow = adminTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "처리할 민원 항목을 목록에서 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int complaintId = (int) adminTableModel.getValueAt(selectedRow, 0);
            String newStatus = (String) cbStatusUpdate.getSelectedItem();
            String comment = txtAdminComment.getText().trim();

            for (Complaint c : complaintList) {
                if (c.id == complaintId) {
                    c.status = newStatus;
                    if (!comment.isEmpty()) {
                        c.adminComment = comment;
                    }
                    break;
                }
            }
            txtAdminComment.setText("");
            refreshTables();
            JOptionPane.showMessageDialog(this, "민원 처리 상태가 업데이트되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(scrollAdminTable, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.SOUTH);

        panel.add(scrollStats, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // 테이블 및 통계 데이터 새로고침
    private void refreshTables() {
        // 1. 유저 테이블 갱신
        userTableModel.setRowCount(0);
        for (Complaint c : complaintList) {
            userTableModel.addRow(new Object[]{
                    c.id, c.location, c.itemCategory, c.failureReason, c.content, c.status, c.likeCount, c.createdAt
            });
        }

        // 2. 관리자 테이블 갱신
        adminTableModel.setRowCount(0);
        for (Complaint c : complaintList) {
            adminTableModel.addRow(new Object[]{
                    c.id, c.userName, c.location, c.itemCategory, c.failureReason, c.status, c.likeCount, c.adminComment
            });
        }

        // 3. 통계 및 예측 업데이트
        updateStats();
    }

    // 고장 원인 통계 및 노후화 사전 예측 로직
    private void updateStats() {
        int total = complaintList.size();
        if (total == 0) {
            txtStatsSummary.setText("등록된 민원이 없습니다.");
            return;
        }

        int agingCount = 0;
        // (장소 + 비품) 조합별 노후화 고장 카운트
        java.util.Map<String, Integer> agingMap = new java.util.HashMap<>();

        for (Complaint c : complaintList) {
            if ("노후화".equals(c.failureReason)) {
                agingCount++;
                String key = c.location + " [" + c.itemCategory + "]";
                agingMap.put(key, agingMap.getOrDefault(key, 0) + 1);
            }
        }

        double agingRatio = ((double) agingCount / total) * 100;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("• 전체 민원 수: %d건 | 노후화 관련 민원: %d건 (%.1f%%)\n", total, agingCount, agingRatio));
        sb.append("--------------------------------------------------------------------------------\n");
        sb.append("⚠️ [비품 노후화 사전 예측 경고 리스트]\n");

        boolean alertFound = false;
        for (java.util.Map.Entry<String, Integer> entry : agingMap.entrySet()) {
            // 동일 장소 및 비품에서 노후화 고장이 2회 이상 발생 시 경고
            if (entry.getValue() >= 2) {
                sb.append(String.format("  🚨 %s: 노후화 신고 %d회 누적 -> '사전 교체 권고' 대상\n", entry.getKey(), entry.getValue()));
                alertFound = true;
            }
        }

        if (!alertFound) {
            sb.append("  ✅ 현재 긴급한 노후화 경고 대상 비품이 없습니다.\n");
        }

        txtStatsSummary.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ComplaintSystemPrototype().setVisible(true);
        });
    }
}
