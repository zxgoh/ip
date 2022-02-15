
package juke;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Juke extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private ArrayList<Task> itemList = new ArrayList<>();


    private Image user = new Image(this.getClass().getResourceAsStream("/images/cat.jpeg"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/woman.jpeg"));

    private static void writeToFile(String filePath, String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(textToAdd);
        fw.close();
    }


    /**
     * Iteration 2:
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        Label userText = new Label(userInput.getText());
        Label dukeText = new Label(getResponse(userInput.getText()));
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, new ImageView(user)),
                DialogBox.getDukeDialog(dukeText, new ImageView(duke))
        );
        userInput.clear();
    }

    /**
     *
     * @param input
     * @return Juke's response based on the command
     */
    private String getResponse(String input) {

        Outputs op = new Outputs();

        while(true) {
            String reply = input;
            String[] splittedString = input.split(" ");

            File file = new File("data/duke.txt");
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) { }
            if (splittedString[0].equals("tag") && splittedString.length == 3) { // check for mark tag
                assert !itemList.isEmpty() : "List is empty";
                String tag = "#" + splittedString[2];
                try {
                    int index = Integer.valueOf(reply.split(" ")[1]);
                    itemList.get(index - 1).setTag(tag);
                    return op.border +
                            "     Nice! I've tagged " + '"' + itemList.get(index - 1).getDescription() + '"' +
                            " with " + tag + "       " + "\n" + op.instructions + op.border;
                } catch (NumberFormatException n) {
                    return "Invalid input, please enter a valid index number instead";
                }

            } else if (splittedString[0].equals("mark") && splittedString.length == 2) { // check for mark tag
                assert !itemList.isEmpty() : "List is empty";
                try {
                    int index = Integer.valueOf(reply.split(" ")[1]);
                    itemList.get(index - 1).markAsDone();
                    return op.border +
                            "     Nice! I've marked this task as done: \n" +
                            "       " + itemList.get(index - 1).getDescription() + "\n" +
                            op.instructions +
                            op.border;

                } catch (NumberFormatException n) {
                    return "Invalid input, please enter a valid index number instead";
                }

                /**
                 * if command is "unmark {index no.}", mark said task as undone
                 */
            } else if (splittedString[0].equals("unmark") && splittedString.length == 2) { // check for unmark tag
                assert !itemList.isEmpty() : "List is empty";
                try {
                    int index = Integer.valueOf(reply.split(" ")[1]);
                    itemList.get(index - 1).markAsUndone();
                    return op.border +
                            "     OK, I've marked this task as not done yet: \n" +
                            "       " + itemList.get(index - 1).getDescription() + "\n" +
                            op.instructions +
                            op.border;
                } catch (NumberFormatException n) {
                    return "Invalid input, please enter a valid index number instead";
                }
            } else if (splittedString[0].equals("delete")) { //check for delete
                assert !itemList.isEmpty() : "List is empty";
                try {
                    int index = Integer.valueOf(splittedString[1]);

                    String toRemove = itemList.remove(index - 1).getDescription();
                    return op.border +
                            "     Noted. I've removed this task: \n" +
                            "       " + toRemove + "\n" +
                            "     Now you have " + itemList.size() + " tasks in the list.\n" + op.instructions +
                            op.border;
                } catch (NumberFormatException n) {
                    return "Invalid input, please enter a valid index number instead";
                }
            } else if (splittedString[0].equals("todo")) { // check for todo tag
                if (splittedString.length == 1) { //invalid todo command
                    return op.border +
                            "     ☹ OOPS!!! The description of a todo cannot be empty.\n" +
                            op.instructions +
                            op.border;
                } else { //valid todo command
                    Task todoTask = new Todo(reply, "");
                    itemList.add(todoTask);
                    return op.border +
                            "     Got it. I've added this task: \n" +
                            "       " + todoTask.getDescription() + "\n" +
                            "     Now you have " + itemList.size() + " tasks in the list.\n" +
                            op.instructions +
                            op.border;
                }
            } else if (splittedString[0].equals("deadline")) { // check for deadline tag
                if (splittedString.length == 1) { //invalid deadline command
                    return op.border +
                            "     ☹ OOPS!!! The description of a deadline cannot be empty.\n" +
                            op.instructions +
                            op.border;
                } else { //valid deadline command
                    Task deadlineTask = new Deadline(reply, "");
                    itemList.add(deadlineTask);
                    return op.border +
                            "     Got it. I've added this task: \n" +
                            "       " + deadlineTask.getDescription() + "\n" +
                            "     Now you have " + itemList.size() + " tasks in the list.\n" +
                            op.instructions +
                            op.border;
                }
            } else if (splittedString[0].equals("event")) { // check for event tag
                if (splittedString.length == 1) { //invalid event command
                    return op.border +
                            "     ☹ OOPS!!! The description of a event cannot be empty.\n" +
                            op.instructions +
                            op.border;
                } else { //valid event command
                    Task eventTask = new Event(reply, "");
                    itemList.add(eventTask);
                    return op.border +
                            "     Got it. I've added this task: \n" +
                            "       " + eventTask.getDescription() + "\n" +
                            "     Now you have " + itemList.size() + " tasks in the list.\n" +
                            op.instructions +
                            op.border;
                }
            } else if (reply.equals("bye")) { // check for bye
                System.exit(0);
            } else if (reply.equals("list")) { // check for list
                String totalString = op.border +
                        "    Here are the tasks in your list:\n";
                for (int i = 0; i < itemList.size(); i++) {
                    totalString += "    " + (i + 1) + ". " + itemList.get(i).getDescription() + " " +
                            itemList.get(i).tag + "\n";
                }
                totalString += op.instructions +
                        op.border;
                return totalString;
            } else if (splittedString[0].equals("find")) {
                String keyword = splittedString[1];
                List<Task> filteredItemList = new ArrayList<>();
                for (Task t : itemList) {
                    filteredItemList.add(t);
                }
                filteredItemList.removeIf(s -> !s.getDescription().contains(keyword));
                String relevantTasks = "";
                int count = 1;
                for (Task t : filteredItemList) {
                    relevantTasks += "     " + count + "." + t.getDescription() + " " + t.tag + "\n";
                    count++;
                }
                if (relevantTasks.equals("")) { //no tasks with relevant keywords
                    return op.border
                            + "     Sorry, there are no matching tasks\n"
                            + op.instructions + op.border;
                } else {
                    return op.border
                            + "     Here are the matching tasks in your list:\n" + relevantTasks
                            + op.instructions + op.border;
                }
            } else if (splittedString[0].equals("help")){
                return op.help;
            } else { //check non-existing commands
                return op.border +
                        "     ☹ OOPS!!! I'm sorry, but I don't know what that means :-(\n" +
                        op.instructions +
                        op.border;
            }
            String file2 = "data/duke.txt";
            try {
                for (Task t : itemList) {
                    writeToFile(file2, t.getDescription() + "\n");
                }
            } catch (IOException e) {
                System.out.println("");
            }


        }
    }

    @Override
    public void start(Stage stage) {


        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);
        userInput = new TextField();
        sendButton = new Button("Send");
        sendButton.setStyle("-fx-font-size: 20;" +
                "-fx-text-fill: #FF69B4;" +
                "-fx-background-color: #89cff0 ; " +
                "-fx-background-radius: 10");
        sendButton.setTranslateX(-5);

        BackgroundFill myBF = new BackgroundFill(Color.HONEYDEW, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));// or null for the padding
//then you set to your node or container or layout
        dialogContainer.setBackground(new Background(myBF));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);
        scene.setFill(Color.AQUAMARINE);
        stage.setScene(scene);
        stage.show();

        stage.setTitle("Juke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        // You will need to import `javafx.scene.layout.Region` for this.
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefSize(280, 50);
        userInput.setTranslateX(10);
        userInput.setStyle(
                "-fx-font-family: Times;" +
                "-fx-font-size: 20;" +
                "-fx-text-fill: #a87bab;" +
                "-fx-background-color: #89cff0 ; " +
                "-fx-background-radius: 10");
//        userInput.
        //


        sendButton.setPrefSize(100,50);
//        sendButton.setPadding(new Insets());
        sendButton.setBorder(
                new Border(
                new BorderStroke(Color.BLUEVIOLET, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput , 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        //event handler
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        userInput.setOnAction((event) -> {
            handleUserInput();
        });
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

    }


}