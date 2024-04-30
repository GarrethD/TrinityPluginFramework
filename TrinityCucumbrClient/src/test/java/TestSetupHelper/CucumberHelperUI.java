package TestSetupHelper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;


public class CucumberHelperUI extends Application {

    private String featureDir;
    private String stepDefDir;
    private String pageObjectDir;
    private String pageObjectPackage;
    private String stepDefPackage;

    private DirectoryChooser directoryChooser;
    private Stage primaryStage;

    private TextField featureField;
    private TextField stepDefField;
    private TextField pageObjectField;
    private GridPane grid = new GridPane();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Test Framework Setup");

        setupDirectoryChooser();
        setupUI();
        primaryStage.show();
    }

    private void setupDirectoryChooser() {
        // Get the root directory of the project
        String projectRootDir = System.getProperty("user.dir");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(projectRootDir));
    }

    private void setupUI() {
        Button selectFeatureDirButton = new Button("Select Feature Directory");
        selectFeatureDirButton.setOnAction(e -> selectDirectory("src/test/resources/features", selectedDirectory -> featureDir = selectedDirectory.getAbsolutePath()));

        Button selectStepDefDirButton = new Button("Select Step Definitions Directory");
        selectStepDefDirButton.setOnAction(e -> selectDirectory("src/test/java/en/Core/stepdefinition", selectedDirectory -> {
            stepDefDir = selectedDirectory.getAbsolutePath();
            stepDefPackage = getPackageFromDirectory(stepDefDir);
        }));

        Button selectPageObjectDirButton = new Button("Select Page Object Directory");
        selectPageObjectDirButton.setOnAction(e -> selectDirectory("src/test/java/en/Core/pageobjects", selectedDirectory -> {
            pageObjectDir = selectedDirectory.getAbsolutePath();
            pageObjectPackage = getPackageFromDirectory(pageObjectDir);
        }));


        featureField = createTextFieldWithValidation(".feature file name");
        stepDefField = createTextFieldWithValidation("Step Definition file name");
        pageObjectField = createTextFieldWithValidation("Page Object file name");

        Button createFeatureButton = new Button("Create Feature");
        createFeatureButton.setOnAction(e -> createFeatureFile());
        grid.add(createFeatureButton, 2, 1); // Change row from 0 to 1

        Button createStepDefButton = new Button("Create Step Definition");
        createStepDefButton.setOnAction(e -> createStepDefFile());
        grid.add(createStepDefButton, 2, 2); // Change row from 1 to 2

        Button createPageObjectButton = new Button("Create Page Object");
        createPageObjectButton.setOnAction(e -> createPageObjectFile());
        grid.add(createPageObjectButton, 2, 3); // Change row from 2 to 3

        Button createAllButton = new Button("Create All");
        createAllButton.setOnAction(e -> createAllFiles());
        grid.add(createAllButton, 1, 4); // Change row from 3 to 4

        addUIComponents(selectFeatureDirButton, selectStepDefDirButton, selectPageObjectDirButton);
    }

    private void selectDirectory(String initialDirectory, Consumer<File> onDirectorySelected) {
        try {
            directoryChooser.setInitialDirectory(new File(initialDirectory));
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                onDirectorySelected.accept(selectedDirectory);
            }
        } catch (NullPointerException exception) {
            // Handle the cancellation gracefully (e.g., display a message)
            System.out.println("Directory selection canceled.");
        }
    }

    private void addUIComponents(Button... buttons) {
        // Add directory selection buttons
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Add directory selection buttons
        for (int i = 0; i < buttons.length; i++) {
            grid.add(buttons[i], i, 0);
        }

        // Add labels and text fields
        addLabelAndField("Feature File Name:", featureField, 1);
        addLabelAndField("Step Definition File Name:", stepDefField, 2);
        addLabelAndField("Page Object File Name:", pageObjectField, 3);

        // Configure grid

        Scene scene = new Scene(grid, 600, 200);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(200);
        primaryStage.setScene(scene);
    }

    private void addLabelAndField(String labelText, TextField textField, int row) {
        grid.add(new Label(labelText), 0, row);
        grid.add(textField, 1, row);
    }
    private Button createButtonForField(int row) {
        Button createButton = new Button();
        switch (row) {
            case 1:
                createButton.setText("Create Feature");
                createButton.setOnAction(e -> createFeatureFile());
                break;
            case 2:
                createButton.setText("Create Step Definition");
                createButton.setOnAction(e -> createStepDefFile());
                break;
            case 3:
                createButton.setText("Create Page Object");
                createButton.setOnAction(e -> createPageObjectFile());
                break;
        }
        return createButton;
    }
    private void createAllFiles() {
        String featureFileName = featureField.getText();
        String stepDefFileName = stepDefField.getText();
        String pageObjectFileName = pageObjectField.getText();

        if (!isValidFileName(featureFileName) || !isValidFileName(stepDefFileName) || !isValidFileName(pageObjectFileName)) {
            System.out.println("Invalid file name. Please fix the input.");
            return;
        }

        String packageName = pageObjectDir.contains("src") ?
                pageObjectDir.substring(pageObjectDir.indexOf("src") + 4, pageObjectDir.lastIndexOf(File.separator)).replace(File.separator, ".") : "";

        String featureFilePath = featureDir + "/" + featureFileName + ".feature";
        String stepDefFilePath = stepDefDir + "/" + stepDefFileName + ".java";
        String pageObjectFilePath = pageObjectDir + "/" + pageObjectFileName + ".java";

        createFile(featureFilePath, createFeatureContent(featureFileName));
        createFile(stepDefFilePath, createStepDefContent(stepDefFileName, stepDefPackage));
        createFile(pageObjectFilePath, createPageObjectContent(pageObjectFileName, packageName));
    }


    private void createFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                try (PrintWriter writer = new PrintWriter(filePath, "UTF-8")) {
                    writer.println(content);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getPackageFromDirectory(String dir) {
        String[] directories = dir.split("\\\\|/");
        StringBuilder packageName = new StringBuilder();
        boolean packageFound = false;
        for (String directory : directories) {
            if (packageFound) {
                packageName.append(directory).append(".");
            }
            if (directory.equals("java")) {
                packageFound = true;
            }
        }
        return packageName.toString().substring(0, packageName.length() - 1);
    }

    private TextField createTextFieldWithValidation(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-border-color: black;");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.matches("[a-zA-Z]*")) {
                    throw new Exception("Invalid characters");
                }
                textField.setStyle("-fx-border-color: black;");
            } catch (Exception exception) {
                textField.setText(oldValue);
                textField.setStyle("-fx-border-color: red;");
                System.out.println("Error: " + exception.getMessage());
            }
        });
        return textField;
    }
    private void createFeatureFile() {
        String featureFileName = featureField.getText();
        if (isValidFileName(featureFileName)) {
            String featureFilePath = featureDir + "/" + featureFileName + ".feature";
            createFile(featureFilePath, createFeatureContent(featureFileName));
        }
    }

    private void createStepDefFile() {
        String stepDefFileName = stepDefField.getText();
        if (isValidFileName(stepDefFileName)) {
            String stepDefFilePath = stepDefDir + "/" + stepDefFileName + ".java";
            createFile(stepDefFilePath, createStepDefContent(stepDefFileName, stepDefPackage));
        }
    }

    private void createPageObjectFile() {
        String pageObjectFileName = pageObjectField.getText();
        if (isValidFileName(pageObjectFileName)) {
            String packageName = getPackageFromDirectory(pageObjectDir);
            String pageObjectFilePath = pageObjectDir + "/" + pageObjectFileName + ".java";
            createFile(pageObjectFilePath, createPageObjectContent(pageObjectFileName, packageName));
        }
    }

    private boolean isValidFileName(String fileName) {
        if (!fileName.matches("[A-Z][a-zA-Z]*")) {
            System.out.println("Invalid file name. File name should start with an uppercase letter and contain only letters.");
            return false;
        }

        return true;
    }

    private String createStepDefContent(String className, String packageName) {
        return "package " + packageName + ";\n\n" +
                "import base.Driver;\n" +
                "import org.openqa.selenium.WebElement;\n" +
                "import org.openqa.selenium.support.FindBy;\n\n" +
                "public class " + className + " extends WebBasePage\n" +
                "{\n" +
                "    @FindBy(xpath =\"//img[@id='logo']\")\n" +
                "    private WebElement LoginHeader;\n\n" +
                "    public " + className + "(Driver driver) {\n" +
                "        super(driver);\n" +
                "    }\n" +
                "}\n";
    }

    private String createFeatureContent(String featureName) {
        return "@Web\n" +
                "Feature: " + featureName + ";\n\n" +
                "  As a user\n" +
                "  I calculate a games score\n" +
                "  So that others can know how good the game is\n\n" +
                "  @TestngScenario\n" +
                "  Scenario: Calculate game score\n" +
                "    Given The game is fun\n" +
                "    When the story is Fun and Engaging\n" +
                "    Then the score should be high\n";
    }

    // Create PageObject file content with package
    // Create PageObject file content with package
    private String createPageObjectContent(String className, String packageName) {
        return "package " + packageName + ";\n\n" +
                "import base.Driver;\n" +

                "import org.openqa.selenium.WebElement;\n" +
                "import org.openqa.selenium.support.FindBy;\n\n" +
                "public class " + className + " extends WebBasePage\n" +
                "{\n" +
                "    @FindBy(xpath =\"//button[@id='grantPermissionButton']\")\n" +
                "    private WebElement CookiesAkkoordButton;\n\n" +
                "    @FindBy(xpath =\"//h1[@id='header']\")\n" +
                "    private WebElement CookieLandingPage;\n\n" +
                "    public " + className + "(Driver driver) {\n" +
                "        super(driver);\n" +
                "    }\n" +
                "}\n";
    }
    // Rest of the code, including the main method, , , , , , and createTextFieldWithValidation remain unchanged.

    public static void main(String[] args) {
        launch(args);
    }
}

