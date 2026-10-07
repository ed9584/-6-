package com.school;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private Stage window;
    private Scene loginScene;
    private Scene complaintScene;
    private Scene signUpScene;

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;

        // ==========================================
        // 1. 로그인 화면 (Login Scene)
        // ==========================================
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.TOP_CENTER);
        loginLayout.setPadding(new Insets(100, 0, 0, 0));
        loginLayout.getStyleClass().add("bg-pane");

        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView.setImage(logo);
            logoView.setFitWidth(120);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("로고 이미지를 찾을 수 없습니다.");
        }

        Label loginTitle = new Label("동의대학교 민원 시스템");
        loginTitle.getStyleClass().add("title-label");

        VBox loginCard = new VBox(15);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(35));
        loginCard.getStyleClass().add("card");
        loginCard.setMaxWidth(360);

        TextField idField = new TextField();
        idField.setPromptText("학번 또는 사번");
        
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
        
        loginScene = new Scene(loginLayout, 500, 750);
        loadCSS(loginScene);

        // ==========================================
        // 2. 회원가입 화면 (Sign Up Scene)
        // ==========================================
        VBox signUpLayout = new VBox(20);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.getStyleClass().add("bg-pane");

        Label signUpTitle = new Label("회원가입");
        signUpTitle.getStyleClass().add("title-label");

        VBox signUpCard = new VBox(15);
        signUpCard.setPadding(new Insets(35));
        signUpCard.getStyleClass().add("card");
        signUpCard.setMaxWidth(360); // 폼 너비 확대

        TextField newNameField = new TextField();
        newNameField.setPromptText("이름");

        TextField newIdField = new TextField();
        newIdField.setPromptText("학번 또는 사번");

        PasswordField newPwField = new PasswordField();
        newPwField.setPromptText("비밀번호");

        PasswordField newPwConfirmField = new PasswordField();
        newPwConfirmField.setPromptText("비밀번호 확인");

        Button submitSignUpBtn = new Button("가입완료");
        submitSignUpBtn.getStyleClass().add("primary-btn");
        submitSignUpBtn.setMaxWidth(Double.MAX_VALUE);

        Button cancelSignUpBtn = new Button("취소");
        cancelSignUpBtn.getStyleClass().add("secondary-btn");
        cancelSignUpBtn.setMaxWidth(Double.MAX_VALUE);

        signUpCard.getChildren().addAll(newNameField, newIdField, newPwField, newPwConfirmField, submitSignUpBtn, cancelSignUpBtn);
        signUpLayout.getChildren().addAll(signUpTitle, signUpCard);

        signUpScene = new Scene(signUpLayout, 500, 750);
        loadCSS(signUpScene);

        // ==========================================
        // 3. 민원 접수 화면 (Complaint Scene)
        // ==========================================
        VBox compLayout = new VBox(25);
        compLayout.setPadding(new Insets(40));
        compLayout.setAlignment(Pos.TOP_CENTER);
        compLayout.getStyleClass().add("bg-pane");

        Label compTitle = new Label("민원 신청");
        compTitle.getStyleClass().add("title-label");

        VBox compCard = new VBox(20);
        compCard.setPadding(new Insets(30));
        compCard.getStyleClass().add("card");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15); // 항목이 추가되었으므로 세로 간격을 살짝 줄임

        // 1) 건물 선택
        Label buildingLabel = new Label("건물 선택");
        buildingLabel.setMinWidth(Label.USE_PREF_SIZE); 
        
        ComboBox<String> buildingBox = new ComboBox<>();
        buildingBox.getItems().addAll(
            "1 대학본관", "2 법정관", "3 상경관", "5 국제관", "6 동의스포츠센터",
            "7 상영관(제2학생회관)", "8 수덕전(학생회관)", "9 제1인문관", "10 제2인문관", "11 효민체육관",
            "12 중앙도서관", "13 성파글로벌관", "14 제2효민생활관", "15 의료보건관", "16 생활과학관",
            "17 음악관", "18 창의관", "19 지천관", "20 산학협력관", "21 건윤관",
            "22 공학관", "23 정보공학관", "24 제1효민생활관", "25 학생군사교육단", "26 행복기숙사(미래생활관)"
        );
        buildingBox.setValue("1 대학본관");
        buildingBox.setMaxWidth(Double.MAX_VALUE);

        // 2) 강의실 호실 입력창 추가
        Label roomLabel = new Label("강의실 호실");
        roomLabel.setMinWidth(Label.USE_PREF_SIZE); 
        
        TextField roomField = new TextField();
        roomField.setPromptText("예: 112호");

        // 3) 상세 내용
        Label contentLabel = new Label("상세 내용");
        contentLabel.setMinWidth(Label.USE_PREF_SIZE); 
        
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("민원 내용을 상세히 작성해주세요.");
        contentArea.setPrefRowCount(8); 
        contentArea.setWrapText(true); 

        // Grid에 순서대로 배치 (행 번호 0, 1, 2)
        formGrid.add(buildingLabel, 0, 0);
        formGrid.add(buildingBox, 1, 0);
        formGrid.add(roomLabel, 0, 1);
        formGrid.add(roomField, 1, 1);
        formGrid.add(contentLabel, 0, 2);
        formGrid.add(contentArea, 1, 2);

        compCard.getChildren().add(formGrid);

        Button submitBtn = new Button("제출하기");
        submitBtn.getStyleClass().add("primary-btn");
        submitBtn.setPrefWidth(220); 

        Button logoutBtn = new Button("로그아웃");
        logoutBtn.getStyleClass().add("secondary-btn");

        HBox btnBox = new HBox(15, submitBtn, logoutBtn);
        btnBox.setAlignment(Pos.CENTER);

        compLayout.getChildren().addAll(compTitle, compCard, btnBox);
        
        complaintScene = new Scene(compLayout, 500, 750);
        loadCSS(complaintScene);

        // ==========================================
        // 4. 이벤트 및 창 설정
        // ==========================================
        goToSignUpBtn.setOnAction(e -> window.setScene(signUpScene));

        loginBtn.setOnAction(e -> {
            if (!idField.getText().isEmpty() && !pwField.getText().isEmpty()) {
                window.setScene(complaintScene);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "아이디와 비밀번호를 입력해주세요.");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });

        cancelSignUpBtn.setOnAction(e -> {
            newNameField.clear();
            newIdField.clear();
            newPwField.clear();
            newPwConfirmField.clear();
            window.setScene(loginScene);
        });

        submitSignUpBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "회원가입이 완료되었습니다.");
            alert.setHeaderText(null);
            alert.showAndWait();
            
            newNameField.clear();
            newIdField.clear();
            newPwField.clear();
            newPwConfirmField.clear();
            window.setScene(loginScene);
        });

       // 민원 접수 창 -> 제출 완료 처리
        submitBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "민원이 성공적으로 접수되었습니다.");
            alert.setHeaderText(null);
            alert.showAndWait();
            
            // 제출 완료 후 입력 폼 초기화
            buildingBox.setValue("1 대학본관");
            roomField.clear();
            contentArea.clear();
        });

        logoutBtn.setOnAction(e -> {
            idField.clear();
            pwField.clear();
            window.setScene(loginScene);
        });

        window.setTitle("동의대학교 민원 앱");
        window.setResizable(false); 
        window.setScene(loginScene);
        window.show();
    }

    private void loadCSS(Scene scene) {
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch(Exception e) {
            System.out.println("style.css 파일을 찾을 수 없습니다.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}