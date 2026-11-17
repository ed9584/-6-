package com.school;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class App extends Application {

    private Stage window;
    private Scene loginScene;
    private Scene signUpScene;
    private Scene studentScene; 
    private Scene adminScene;   

    private ObservableList<ComplaintData> dbList = FXCollections.observableArrayList();
    private int currentId = 2;

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;

        // ★ 테스트용 데이터에 '관리자 답변' 값 추가
        dbList.add(new ComplaintData("2", "12 중앙도서관", "의자", "파손", "처리 중", "12", 
                "#위험 #부상우려", "첨부된 파일 없음", "의자 바퀴 하나가 빠져 있어서 앉으면 넘어질 뻔했습니다.", "해당 열람실의 의자 전체 점검 후 파손된 비품은 수거 조치했습니다."));
        dbList.add(new ComplaintData("1", "23 정보공학관", "모니터", "노후화", "접수됨", "3", 
                "#긴급 #수업지연", "broken_monitor.jpg", "모니터 전원이 아예 들어오지 않습니다. 빠른 교체 부탁드립니다.", ""));

        // ==========================================
        // 1. 로그인 화면 구성
        // ==========================================
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.TOP_CENTER);
        loginLayout.setPadding(new Insets(120, 0, 0, 0));
        loginLayout.getStyleClass().add("bg-pane");

        ImageView logoView = new ImageView();
        try {
            logoView.setImage(new Image(getClass().getResourceAsStream("/logo.png")));
            logoView.setFitWidth(120);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {}

        Label loginTitle = new Label("동의대학교 민원 시스템");
        loginTitle.getStyleClass().add("title-label");

        VBox loginCard = new VBox(15);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(35));
        loginCard.getStyleClass().add("card");
        loginCard.setMaxWidth(360);

        TextField idField = new TextField();
        idField.setPromptText("학번 또는 사번 (admin 입력 시 관리자)");
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("비밀번호");

        Button loginBtn = new Button("로그인");
        loginBtn.getStyleClass().add("primary-btn");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Button goToSignUpBtn = new Button("회원가입");
        goToSignUpBtn.getStyleClass().add("secondary-btn");
        goToSignUpBtn.setMaxWidth(Double.MAX_VALUE);

        loginCard.getChildren().addAll(idField, pwField, loginBtn, goToSignUpBtn);
        loginLayout.getChildren().addAll(logoView, loginTitle, loginCard);
        
        loginScene = new Scene(loginLayout, 900, 700);
        loadCSS(loginScene);

        // ==========================================
        // 2. 회원가입 화면 구성
        // ==========================================
        VBox signUpLayout = new VBox(20);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.getStyleClass().add("bg-pane");
        Label signUpTitle = new Label("회원가입");
        signUpTitle.getStyleClass().add("title-label");

        VBox signUpCard = new VBox(15);
        signUpCard.setPadding(new Insets(35));
        signUpCard.getStyleClass().add("card");
        signUpCard.setMaxWidth(360);

        TextField newNameField = new TextField(); newNameField.setPromptText("이름");
        TextField newIdField = new TextField(); newIdField.setPromptText("학번 또는 사번");
        PasswordField newPwField = new PasswordField(); newPwField.setPromptText("비밀번호");
        Button submitSignUpBtn = new Button("가입완료"); submitSignUpBtn.getStyleClass().add("primary-btn");
        Button cancelSignUpBtn = new Button("취소"); cancelSignUpBtn.getStyleClass().add("secondary-btn");

        submitSignUpBtn.setMaxWidth(Double.MAX_VALUE); cancelSignUpBtn.setMaxWidth(Double.MAX_VALUE);
        signUpCard.getChildren().addAll(newNameField, newIdField, newPwField, submitSignUpBtn, cancelSignUpBtn);
        signUpLayout.getChildren().addAll(signUpTitle, signUpCard);
        signUpScene = new Scene(signUpLayout, 900, 700);
        loadCSS(signUpScene);

        // ==========================================
        // 3. 학생용 메인 화면 (탭: 민원 신청 / 민원 목록)
        // ==========================================
        TabPane studentTabPane = new TabPane();
        
        // --- 탭 1: 민원 신청 폼 ---
        Tab formTab = new Tab("민원 신청");
        formTab.setClosable(false);
        VBox formLayout = new VBox(15);
        formLayout.setPadding(new Insets(30));
        formLayout.getStyleClass().add("bg-pane");
        
        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(20));
        formCard.getStyleClass().add("card");
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15); formGrid.setVgap(15);

        ComboBox<String> buildingBox = new ComboBox<>();
        buildingBox.getItems().addAll(
            "1 대학본관", "2 법정관", "3 상경관", "5 국제관", "6 동의스포츠센터",
            "7 상영관(제2학생회관)", "8 수덕전(학생회관)", "9 제1인문관", "10 제2인문관", "11 효민체육관",
            "12 중앙도서관", "13 성파글로벌관", "14 제2효민생활관", "15 의료보건관", "16 생활과학관",
            "17 음악관", "18 창의관", "19 지천관", "20 산학협력관", "21 건윤관",
            "22 공학관", "23 정보공학관", "24 제1효민생활관", "25 학생군사교육단", "26 행복기숙사(미래생활관)"
        );
        buildingBox.setValue("23 정보공학관"); buildingBox.setMaxWidth(Double.MAX_VALUE);
        TextField roomField = new TextField(); roomField.setPromptText("호실 입력 (예: 501호)");
        
        ComboBox<String> itemBox = new ComboBox<>();
        itemBox.getItems().addAll("모니터", "마우스", "키보드", "책상", "의자", "빔프로젝터", "에어컨", "기타");
        itemBox.setValue("모니터");
        
        ComboBox<String> reasonBox = new ComboBox<>();
        reasonBox.getItems().addAll("노후화", "파손", "초기불량", "기타/미상");
        reasonBox.setValue("노후화");

        TextField tagField = new TextField(); tagField.setPromptText("예: #파손 #긴급 #수업지연");
        
        Button photoBtn = new Button("사진 첨부");
        Label photoLabel = new Label("첨부된 파일 없음");
        photoLabel.setStyle("-fx-text-fill: gray;");
        HBox photoBox = new HBox(10, photoBtn, photoLabel);
        photoBox.setAlignment(Pos.CENTER_LEFT);

        photoBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("이미지 파일", "*.png", "*.jpg", "*.jpeg"));
            File file = fc.showOpenDialog(window);
            if(file != null) photoLabel.setText(file.getName());
        });

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("상세 내용을 작성해주세요.");
        contentArea.setPrefRowCount(4); contentArea.setWrapText(true);

        formGrid.add(new Label("장소(건물)"), 0, 0); formGrid.add(buildingBox, 1, 0);
        formGrid.add(new Label("호실/세부장소"), 2, 0); formGrid.add(roomField, 3, 0);
        formGrid.add(new Label("비품 항목"), 0, 1); formGrid.add(itemBox, 1, 1);
        formGrid.add(new Label("고장 원인"), 2, 1); formGrid.add(reasonBox, 3, 1);
        formGrid.add(new Label("해시태그"), 0, 2); formGrid.add(tagField, 1, 2, 3, 1);
        formGrid.add(new Label("현장 사진"), 0, 3); formGrid.add(photoBox, 1, 3, 3, 1);
        formGrid.add(new Label("상세 내용"), 0, 4); formGrid.add(contentArea, 1, 4, 3, 1);

        formCard.getChildren().add(formGrid);
        Button submitBtn = new Button("민원 제출하기"); submitBtn.getStyleClass().add("primary-btn");
        Button stdLogoutBtn = new Button("로그아웃"); stdLogoutBtn.getStyleClass().add("secondary-btn");
        HBox stdBtnBox = new HBox(15, submitBtn, stdLogoutBtn);
        stdBtnBox.setAlignment(Pos.CENTER);
        
        formLayout.getChildren().addAll(new Label("시설 및 비품 민원 접수"), formCard, stdBtnBox);
        formTab.setContent(formLayout);

        // --- 탭 2: 민원 목록 및 공감(고쳐줘요) ---
        Tab listTab = new Tab("민원 목록");
        listTab.setClosable(false);
        VBox listLayout = new VBox(15);
        listLayout.setPadding(new Insets(30));
        listLayout.getStyleClass().add("bg-pane");

        TableView<ComplaintData> studentTable = createTableView();
        studentTable.setItems(dbList);

        Button viewStdDetailBtn = new Button("상세보기");
        viewStdDetailBtn.getStyleClass().add("secondary-btn");
        viewStdDetailBtn.setOnAction(e -> {
            ComplaintData selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) showDetailPopup(selected);
            else showAlert(Alert.AlertType.WARNING, "상세 내용을 볼 민원을 표에서 선택해주세요.");
        });

        Button sympathyBtn = new Button("👍 이 민원 공감하기 (고쳐줘요!)");
        sympathyBtn.getStyleClass().add("primary-btn");
        
        sympathyBtn.setOnAction(e -> {
            ComplaintData selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.isSympathized()) {
                    showAlert(Alert.AlertType.WARNING, "이미 공감한 게시물입니다.");
                } else {
                    int currentLikes = Integer.parseInt(selected.getLikes());
                    selected.setLikes(String.valueOf(currentLikes + 1));
                    selected.setSympathized(true); 
                    studentTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "해당 민원에 공감하셨습니다!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "공감할 민원을 표에서 먼저 선택해주세요.");
            }
        });

        HBox stdActionBox = new HBox(15, viewStdDetailBtn, sympathyBtn);
        listLayout.getChildren().addAll(new Label("실시간 교내 민원 현황"), studentTable, stdActionBox);
        listTab.setContent(listLayout);
        studentTabPane.getTabs().addAll(formTab, listTab);
        
        studentScene = new Scene(studentTabPane, 900, 700);
        loadCSS(studentScene);

        // ==========================================
        // 4. 관리자용 메인 화면 (대시보드)
        // ==========================================
        VBox adminLayout = new VBox(15);
        adminLayout.setPadding(new Insets(30));
        adminLayout.getStyleClass().add("bg-pane");
        
        Label adminTitle = new Label("🛠️ 관리자 대시보드 (민원 처리)");
        adminTitle.getStyleClass().add("title-label");

        TableView<ComplaintData> adminTable = createTableView();
        adminTable.setItems(dbList);

        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);
        controlBox.getStyleClass().add("card");
        controlBox.setPadding(new Insets(15));

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("접수됨", "처리 중", "완료");
        statusBox.setValue("처리 중");

        // ★ 관리자 답변 입력 필드 추가
        TextField answerInputField = new TextField();
        answerInputField.setPromptText("답변 등록 (선택 사항)");
        answerInputField.setPrefWidth(250);

        Button updateBtn = new Button("저장/업데이트");
        updateBtn.getStyleClass().add("primary-btn");

        Button viewAdmDetailBtn = new Button("상세보기");
        viewAdmDetailBtn.getStyleClass().add("secondary-btn");
        viewAdmDetailBtn.setOnAction(e -> {
            ComplaintData selected = adminTable.getSelectionModel().getSelectedItem();
            if (selected != null) showDetailPopup(selected);
            else showAlert(Alert.AlertType.WARNING, "상세 내용을 볼 민원을 표에서 선택해주세요.");
        });

        Button admLogoutBtn = new Button("로그아웃");
        admLogoutBtn.getStyleClass().add("secondary-btn");

        controlBox.getChildren().addAll(new Label("상태:"), statusBox, answerInputField, updateBtn, viewAdmDetailBtn, admLogoutBtn);
        adminLayout.getChildren().addAll(adminTitle, adminTable, controlBox);
        
        adminScene = new Scene(adminLayout, 900, 700);
        loadCSS(adminScene);

        // ==========================================
        // 5. 화면 전환 및 로직 제어
        // ==========================================
        
        loginBtn.setOnAction(e -> {
            String id = idField.getText();
            if (id.isEmpty() || pwField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "아이디와 비밀번호를 입력해주세요.");
            } else if (id.equals("admin")) {
                window.setScene(adminScene);
            } else {
                window.setScene(studentScene); 
            }
        });

        submitBtn.setOnAction(e -> {
            if (roomField.getText().isEmpty() || contentArea.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "호실과 상세 내용을 모두 입력해주세요.");
                return;
            }
            
            currentId++;
            // ★ 신규 민원 제출 시 초기 답변(Answer)은 빈 문자열("")로 저장
            dbList.add(0, new ComplaintData(
                String.valueOf(currentId), buildingBox.getValue() + " " + roomField.getText(), 
                itemBox.getValue(), reasonBox.getValue(), "접수됨", "0",
                tagField.getText(), photoLabel.getText(), contentArea.getText(), "" 
            ));
            showAlert(Alert.AlertType.INFORMATION, "민원이 성공적으로 접수되었습니다.");
            
            roomField.clear(); tagField.clear(); contentArea.clear();
            photoLabel.setText("첨부된 파일 없음");
            studentTabPane.getSelectionModel().select(listTab);
        });

        updateBtn.setOnAction(e -> {
            ComplaintData selected = adminTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus(statusBox.getValue());
                // ★ 답변 필드에 내용이 있으면 데이터 모델에 저장
                if (!answerInputField.getText().isEmpty()) {
                    selected.setAnswer(answerInputField.getText());
                }
                adminTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "해당 민원의 처리 상태 및 답변이 업데이트되었습니다.");
                answerInputField.clear();
            } else {
                showAlert(Alert.AlertType.WARNING, "업데이트할 민원을 선택해주세요.");
            }
        });

        goToSignUpBtn.setOnAction(e -> window.setScene(signUpScene));
        cancelSignUpBtn.setOnAction(e -> window.setScene(loginScene));
        submitSignUpBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "회원가입 완료!");
            window.setScene(loginScene);
        });

        stdLogoutBtn.setOnAction(e -> { idField.clear(); pwField.clear(); window.setScene(loginScene); });
        admLogoutBtn.setOnAction(e -> { idField.clear(); pwField.clear(); window.setScene(loginScene); });

        window.setTitle("동의대학교 민원 앱");
        window.setResizable(false);
        window.setScene(loginScene);
        window.show();
    }

    private void showDetailPopup(ComplaintData data) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.initOwner(window);
        detailStage.setTitle("민원 상세 보기");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.getStyleClass().add("bg-pane");

        Label title = new Label("📝 민원 상세 정보");
        title.getStyleClass().add("title-label");

        // 창 크기를 넘어가면 스크롤 가능하게 세팅
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.getStyleClass().add("card");

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        
        grid.add(new Label("• 장소:"), 0, 0); grid.add(new Label(data.getLocation()), 1, 0);
        grid.add(new Label("• 비품/원인:"), 0, 1); grid.add(new Label(data.getItem() + " / " + data.getReason()), 1, 1);
        grid.add(new Label("• 해시태그:"), 0, 2); grid.add(new Label(data.getTag().isEmpty() ? "없음" : data.getTag()), 1, 2);
        grid.add(new Label("• 첨부사진:"), 0, 3); grid.add(new Label(data.getPhoto()), 1, 3);
        
        Label contentTitle = new Label("• 상세 내용:");
        TextArea contentArea = new TextArea(data.getContent());
        contentArea.setEditable(false);
        contentArea.setWrapText(true);
        contentArea.setPrefRowCount(4);

        // ★ 관리자 답변 출력 영역 추가
        Label answerTitle = new Label("👨‍🔧 관리자 답변:");
        answerTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e;");
        
        String answerText = data.getAnswer().isEmpty() ? "현재 확인 중이거나 답변 대기 중입니다." : data.getAnswer();
        TextArea answerArea = new TextArea(answerText);
        answerArea.setEditable(false);
        answerArea.setWrapText(true);
        answerArea.setPrefRowCount(2);
        if (data.getAnswer().isEmpty()) {
            answerArea.setStyle("-fx-text-fill: gray;");
        } else {
            answerArea.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-background-color: #f0f4c3;");
        }

        card.getChildren().addAll(grid, contentTitle, contentArea, answerTitle, answerArea);

        Button closeBtn = new Button("닫기");
        closeBtn.getStyleClass().add("primary-btn");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        closeBtn.setOnAction(e -> detailStage.close());

        layout.getChildren().addAll(title, card, closeBtn);

        // 답변 영역 추가로 인해 팝업 창 길이를 살짝 늘림
        Scene scene = new Scene(layout, 480, 680);
        loadCSS(scene);
        detailStage.setScene(scene);
        detailStage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void loadCSS(Scene scene) {
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch(Exception e) { System.out.println("style.css 누락"); }
    }

    // 게시판(TableView) 생성 메서드
    private TableView<ComplaintData> createTableView() {
        TableView<ComplaintData> table = new TableView<>();
        table.setPrefHeight(450);

        TableColumn<ComplaintData, String> colId = new TableColumn<>("번호");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(55);

        TableColumn<ComplaintData, String> colLoc = new TableColumn<>("장소");
        colLoc.setCellValueFactory(new PropertyValueFactory<>("location"));
        colLoc.setPrefWidth(210);

        TableColumn<ComplaintData, String> colItem = new TableColumn<>("비품");
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colItem.setPrefWidth(90);

        TableColumn<ComplaintData, String> colReason = new TableColumn<>("고장원인");
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colReason.setPrefWidth(90);

        TableColumn<ComplaintData, String> colStatus = new TableColumn<>("상태");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(80);

        TableColumn<ComplaintData, String> colLikes = new TableColumn<>("공감 수");
        colLikes.setCellValueFactory(new PropertyValueFactory<>("likes"));
        colLikes.setPrefWidth(65);

        TableColumn<ComplaintData, String> colAnswer = new TableColumn<>("관리자 답변");
        colAnswer.setCellValueFactory(new PropertyValueFactory<>("answer"));
        colAnswer.setPrefWidth(235);

        table.getColumns().addAll(colId, colLoc, colItem, colReason, colStatus, colLikes, colAnswer);

        // 모든 열의 너비 조절 및 드래그 이동 차단
        for (TableColumn<?, ?> col : table.getColumns()) {
            col.setResizable(false);
            col.setReorderable(false);
        }

        // 이미 선택된 행을 다시 클릭하면 강조 표시 끄기
        table.setRowFactory(tv -> {
            TableRow<ComplaintData> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (!row.isEmpty() && row.isSelected() && event.isPrimaryButtonDown()) {
                    table.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;
        });

        return table;
    }

    // ★ 실시간 감지 및 관리자 답변 속성(Property) 반환 메서드가 추가된 모델
    public static class ComplaintData {
        private final SimpleStringProperty id, location, item, reason, status, likes;
        private final SimpleStringProperty tag, photo, content, answer;
        private boolean sympathized; 

        public ComplaintData(String id, String location, String item, String reason, String status, String likes, String tag, String photo, String content, String answer) {
            this.id = new SimpleStringProperty(id);
            this.location = new SimpleStringProperty(location);
            this.item = new SimpleStringProperty(item);
            this.reason = new SimpleStringProperty(reason);
            this.status = new SimpleStringProperty(status);
            this.likes = new SimpleStringProperty(likes);
            this.tag = new SimpleStringProperty(tag);
            this.photo = new SimpleStringProperty(photo);
            this.content = new SimpleStringProperty(content);
            this.answer = new SimpleStringProperty(answer);
            this.sympathized = false;
        }

        public String getId() { return id.get(); }
        public String getLocation() { return location.get(); }
        public String getItem() { return item.get(); }
        public String getReason() { return reason.get(); }
        public String getStatus() { return status.get(); }
        public String getLikes() { return likes.get(); }
        public String getTag() { return tag.get(); }
        public String getPhoto() { return photo.get(); }
        public String getContent() { return content.get(); }
        public String getAnswer() { return answer.get(); }
        
        public boolean isSympathized() { return sympathized; }

        public void setStatus(String val) { status.set(val); }
        public void setLikes(String val) { likes.set(val); }
        public void setAnswer(String val) { answer.set(val); }
        public void setSympathized(boolean val) { this.sympathized = val; }

        public SimpleStringProperty statusProperty() { return status; }
        public SimpleStringProperty likesProperty() { return likes; }
        public SimpleStringProperty answerProperty() { return answer; } // 답변 실시간 갱신용
    }

    public static void main(String[] args) {
        launch(args);
    }
}