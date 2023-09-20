package TestSetupHelper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;

public class CucumberTutorialUI extends Application {
    private Stage primaryStage;
    private int currentPage = 0;
    private MediaPlayer mediaPlayer = null;
    private Button playButton = null;
    private Button pauseButton = null;
    private Button restartButton = null;
    private int HeadingFontSize = 30;

    private String[] PageHeaders = {
            "Introduction to the framework",
            "Writing a feature file",
            "What is a step definition",
    };
    private String[] imageUrls = {
            "/TutorialData/garreth210_A_logo_that_showcases_a_robot_testing_development_co_faea53e2-1149-4f82-a07b-7102257f95ad.png",
            "/TutorialData/png-transparent-cucumber-behavior-driven-development-software-testing-test-automation-cartoon-cucumber-leaf-logo-rss.png",
            "/TutorialData/TestVideo.mp4",
    };
    private boolean[] isVideoPage = {
            false,
            false,
            true,
    };
    private String[] PageText = {
            "The introduction of the UI testing client framework represents a major leap forward in streamlining user interface testing procedures. This powerful tool has been specifically designed to handle all the intricacies of dependencies and setup, freeing users from the burden of these often complex and time-consuming tasks. The primary goal of the framework is to offer a simple, efficient, and user-friendly platform for writing tests. To this end, it incorporates Cucumber, a well-established tool that uses Behavior Driven Development principles for easier understanding and maintenance. Users can focus on creating meaningful, targeted, and efficient tests through writing actions for Cucumber, thereby contributing to the quality and reliability of the software under test. In essence, this framework simplifies the process of UI testing, allowing users to concentrate on what matters most - ensuring their software delivers the best user experience possible.",
            "Feature files are an integral part of Behavior Driven Development (BDD) methodology, a crucial component of the UI testing client framework. They provide a high-level description of the application's behavior from the user's perspective. Written in a simple, natural language syntax called Gherkin, feature files serve as a common language for all stakeholders, including developers, testers, and business analysts. This promotes understanding and communication across the entire team. A feature file typically contains a series of scenarios, each representing a specific functionality or behavior of the application. Each scenario is made up of steps that define the given situation, the action to be performed, and the expected outcome. By writing feature files, users can accurately capture the desired behavior of the application, setting a clear expectation for what needs to be developed and tested.",
            "Step definitions in the context of UI testing framework are the bridge between the natural language scenarios written in the feature files and the code that executes these scenarios. They are essentially methods written in code that are mapped to individual steps in the feature file. Each step defined in a feature file has a corresponding step definition that implements the functionality of that step. This allows the tests to be executed in an automated way by reading the steps from the feature file and executing the corresponding code in the step definitions. Step definitions allow for a clear, organized structure to automated testing, making it easier to write, understand, and maintain the tests.",
    };
    private String[] Questions = {
            "What is one major goal of the UI testing client framework?",
            "What does a feature file provide?",
            "What does a step definition do in the UI testing framework?",
    };
    private String[][] Answers = {
            {"Streamline UI testing procedures", "Code compiling", "Design UI", "Create databases"},
            {"High level, user perspective description of the application", "Detailed list of all system users", "List of all the errors in the system", "Historical performance stats of the system"},
            {"Acts as a bridge between natural language scenarios and the code", "Increases application security", "Stores user data", "Analyses the code coverage"},
    };

    private void askQuestion() {
        Dialog<ButtonType> dialog = setupDialog();

        RadioButton[] options = generateOptions();
        setupDialogPaneContent(dialog.getDialogPane(), options);

        Optional<ButtonType> result = dialog.showAndWait();

        // We assume here that the first option is always correct.
        RadioButton selectedRadioButton = (RadioButton) options[0].getToggleGroup().getSelectedToggle();

        // If the question was answered correctly, increment currentPage and display the next page
        if (result.isPresent() && result.get() == ButtonType.OK && selectedRadioButton == options[0]) {
            currentPage++;
            displayPage();
        } else {
            // If the question was not answered correctly, show an error and ask the question again
            showAlert("Incorrect! Please rewatch the video.", Alert.AlertType.ERROR);
            mediaPlayer.stop();
            askQuestion();
        }
    }

    private void askFinalQuestion() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Final Question");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label questionText = new Label(Questions[Questions.length - 1]);
        ToggleGroup group = new ToggleGroup();
        VBox vbox = new VBox();
        vbox.getChildren().add(questionText);
        RadioButton[] options = new RadioButton[Answers[Answers.length - 1].length];
        for (int i = 0; i < Answers[Answers.length - 1].length; i++) {
            options[i] = new RadioButton(Answers[Answers.length - 1][i]);
            options[i].setToggleGroup(group);
            vbox.getChildren().add(options[i]);
        }
        dialogPane.setContent(vbox);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            if (selectedRadioButton == options[0]) {
                showAlert("Correct! Tutorial is complete.", Alert.AlertType.INFORMATION);
                Platform.exit();
            } else {
                showAlert("Incorrect! Please rewatch the video.", Alert.AlertType.ERROR);
                mediaPlayer.stop();
                displayPage();
            }
        }
    }

    private Dialog<ButtonType> setupDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Question");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        return dialog;
    }

    private RadioButton[] generateOptions() {
        RadioButton[] options = new RadioButton[Answers[currentPage].length];
        ToggleGroup group = new ToggleGroup();
        for (int i = 0; i < Answers[currentPage].length; i++) {
            options[i] = new RadioButton(Answers[currentPage][i]);
            options[i].setToggleGroup(group);
        }
        return options;
    }

    private void setupDialogPaneContent(DialogPane dialogPane, RadioButton[] options) {
        Label questionText = new Label(Questions[currentPage]);
        VBox vbox = new VBox();
        vbox.getChildren().add(questionText);
        vbox.getChildren().addAll(options);
        dialogPane.setContent(vbox);
    }

    private void processResult(Optional<String> result, RadioButton[] options) {
        if (result.isPresent()) {
            result.get();
        }
    }

    private void processRadioButtonSelection(RadioButton selectedRadioButton, RadioButton correctAnswer) {
        if (selectedRadioButton == correctAnswer) {
            showAlert("Correct!", Alert.AlertType.INFORMATION);
            if (currentPage < PageHeaders.length - 1) {
                currentPage++;
            }
        } else {
            showAlert("Incorrect! Please rewatch the video.", Alert.AlertType.ERROR);
            mediaPlayer.stop();
        }
        displayPage();
    }

    private void showAlert(String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            showAlert("An unexpected error occurred: " + throwable.getMessage(), Alert.AlertType.ERROR);
        });
        // Set the close request handler
        primaryStage.setOnCloseRequest(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.dispose();  // This releases the media player resources
            }
        });
        primaryStage.show();
        this.primaryStage = primaryStage;
        displayPage();
    }

    private void nextPage() {
        if (currentPage < PageHeaders.length - 1) {
            askQuestion();
        } else if (currentPage == PageHeaders.length - 1) {
            askFinalQuestion();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
        displayPage();
        askQuestion();
    }

    private void displayPage() {
        Platform.runLater(() -> {
            stopMediaPlayer();
            BorderPane rootPane = new BorderPane();
            rootPane.setTop(createHeadingBox());
            rootPane.setCenter(createGrid());
            rootPane.setBottom(createButtonHbox());
            rootPane.setStyle("-fx-background-color:#2b2b2b;");
            rootPane.setPadding(new Insets(15));
            setScene(rootPane);
            primaryStage.setResizable(false);
            primaryStage.show();
        });
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private HBox createHeadingBox() {
        Label heading = new Label(PageHeaders[currentPage]);
        heading.setMaxWidth(Double.MAX_VALUE);
        heading.setAlignment(Pos.CENTER);
        heading.setStyle("-fx-font-size: " + HeadingFontSize + "; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox topHbox = new HBox();
        topHbox.setAlignment(Pos.CENTER);
        topHbox.getChildren().add(heading);
        return topHbox;
    }

    private VBox createTextAreaBox() {
        TextArea textArea = new TextArea();
        textArea.setText(PageText[currentPage]);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefRowCount(textArea.getText().split("\n").length);
        textArea.setStyle("-fx-text-fill: white; -fx-control-inner-background:#2b2b2b;");
        textArea.setPrefHeight(400);
        VBox vBoxTextArea = new VBox(textArea);
        VBox.setVgrow(textArea, Priority.NEVER);
        return vBoxTextArea;
    }

    private Node createMediaView() {
        Node view = null;
        URL mediaUrl = getClass().getResource(imageUrls[currentPage]);
        if (mediaUrl == null) {
            System.out.println("Media resource not found: " + imageUrls[currentPage]);
            return new Label("Media resource not found.");
        } else if (isVideoPage[currentPage]) {
            view = createVideoView(mediaUrl);
        } else {
            view = createImageView(mediaUrl);
        }
        view.setStyle("-fx-background-color: transparent");
        return view;
    }

    private Node createVideoView(URL mediaUrl) {
        Media media = new Media(mediaUrl.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> {
            System.out.println("Media Error: " + mediaPlayer.getError());
            showAlert("Error playing video: " + mediaPlayer.getError().getMessage(), Alert.AlertType.ERROR);
        });
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        BorderPane mediaViewWrapper = new BorderPane();
        mediaViewWrapper.setStyle("-fx-background-color: transparent");
        mediaViewWrapper.setPrefSize(600, 350);
        mediaViewWrapper.setMaxHeight(0.8 * Screen.getPrimary().getBounds().getHeight());
        mediaView.fitWidthProperty().bind(mediaViewWrapper.widthProperty());
        mediaView.fitHeightProperty().bind(mediaViewWrapper.heightProperty());
        mediaViewWrapper.setCenter(mediaView);
        mediaViewWrapper.setBottom(createVideoControlButtonHbox());
        return mediaViewWrapper;
    }

    private HBox createVideoControlButtonHbox() {
        playButton = new Button("Play");
        playButton.setOnAction(e -> mediaPlayer.play());
        pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> mediaPlayer.pause());
        restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        HBox videoControlButtonHbox = new HBox();
        if (playButton != null && pauseButton != null && restartButton != null) {
            videoControlButtonHbox.getChildren().addAll(playButton, pauseButton, restartButton);
            videoControlButtonHbox.setSpacing(10);
            videoControlButtonHbox.setAlignment(Pos.CENTER);
        }
        return videoControlButtonHbox;
    }

    private ImageView createImageView(URL mediaUrl) {
        Image image = new Image(mediaUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(0.35 * Screen.getPrimary().getBounds().getWidth());
        return imageView;
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.add(createMediaView(), 1, 0);
        grid.setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        grid.getColumnConstraints().addAll(col1, col2);
        grid.add(createTextAreaBox(), 0, 0);
        return grid;
    }

    private HBox createButtonHbox() {
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> nextPage());
        Button prevButton = new Button("Prev");
        prevButton.setOnAction(e -> previousPage());
        HBox buttonHbox = new HBox(prevButton, nextButton);
        buttonHbox.setAlignment(Pos.CENTER);
        buttonHbox.setSpacing(10);
        return buttonHbox;
    }

    private void setScene(BorderPane rootPane) {
        Scene scene = new Scene(rootPane,
                0.8 * Screen.getPrimary().getBounds().getWidth(),
                0.8 * Screen.getPrimary().getBounds().getHeight());
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
