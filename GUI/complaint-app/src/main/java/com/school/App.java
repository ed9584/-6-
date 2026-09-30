package com.school;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. 화면 요소 만들기
        Label title = new Label("학교 민원 신청 시스템");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("학번 (예: 20260001)");

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("민원 내용을 상세히 적어주세요.");
        contentArea.setPrefRowCount(5);

        Button submitBtn = new Button("제출하기");
        submitBtn.setStyle("-fx-background-color: #0d6efd; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setPrefWidth(200);

        // 2. 요소들을 수직으로 배치 (VBox 레이아웃)
        VBox layout = new VBox(15); // 요소 간격 15px
        layout.setPadding(new Insets(20)); // 화면 안쪽 여백
        layout.setAlignment(Pos.CENTER); // 중앙 정렬
        layout.getChildren().addAll(title, studentIdField, contentArea, submitBtn);

        // 3. 버튼 클릭 이벤트 (기능 연결)
        submitBtn.setOnAction(e -> {
            System.out.println("학번: " + studentIdField.getText());
            System.out.println("내용: " + contentArea.getText());
            System.out.println("제출 완료!");
        });

        // 4. 화면 띄우기
        Scene scene = new Scene(layout, 350, 400);
        primaryStage.setTitle("학교 민원 앱");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}