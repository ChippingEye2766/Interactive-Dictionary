package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Controller;

import java.io.*;
import java.net.URL;
import java.util.Vector;
class node implements Serializable
{
    boolean isLeaf;
    node children[]=new node[26];
    StringBuffer defination;
}

public class Main extends Application {
    Vector<Button> buttonlist=new Vector<>();
    TextField word = new TextField("");
    HBox root = new HBox(10);
    VBox first = new VBox(10);
    Label didyoumean = new Label("        Did you mean");
    FlowPane layout = new FlowPane();
    Label search=new Label("        Search");
    static node dictionary;
        //trie dict ;
    Controller obj = new Controller();
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
          ProgressIndicator progressIndicator=new ProgressIndicator(-1);
        progressIndicator.setMinWidth(100);

        Service<Void> background=new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                       InputStream file=(getClass().getResourceAsStream( "obj/objfile"));
                        BufferedInputStream bis=new BufferedInputStream(file);
                        ObjectInputStream ous = new ObjectInputStream(bis);
                        dictionary = (node) ous.readObject();
                        return null;
                    }
                };
            }
        };

        background.setOnRunning(e->{
            primaryStage.setTitle("Dictionary");
           // progressIndicator.setProgress();
            //progressIndicator.setProgress(-1);
            FlowPane templayout=new FlowPane();
            Label label=new Label("   Loading up the Dictionary ");
            label.setTextFill(Color.WHITE);
            label.setId("loading");
           // label.setFont();
            templayout.getChildren().addAll(label,progressIndicator);
            templayout.setPadding(new Insets(50,20,20,20));
            Scene scene=new Scene(templayout,600,200);
            primaryStage.setScene(scene);
            scene.getStylesheets().addAll("style.css");
            primaryStage.show();
        });
        background.setOnSucceeded(e->{
            progressIndicator.setProgress(1);
            //first.setPadding(new Insets(10, 10, 0, 10));
               search.setId("search");
            didyoumean.setId("search");
            //didyoumean.setAlignment(Pos.CENTER);
            first.getChildren().addAll(search,word);
            word.setMinWidth(200);
            root.getChildren().addAll(first);
             root.setPadding(new Insets(20,20,20,20));
            layout.setPadding(new Insets(20,20,20,20));
            root.getChildren().addAll(layout);
            word.setOnAction(t -> {
                action(word.getText());
            });
            System.out.println("object loaded");
            Scene scene = new Scene(root, 600, 575);
            primaryStage.setScene(scene);
            scene.getStylesheets().addAll("style.css");
        });
        background.restart();


    }

    public void action(String text) {
        root.getChildren().removeAll(layout);
        layout.getChildren().clear();

        String define = search(dictionary, text.toLowerCase());
        if (define.equalsIgnoreCase("Sorry no meaning found")) {
            Label alert = new Label("          Sorry no meaning found");
            alert.setId("earch");
            layout.getChildren().addAll(alert);
            root.getChildren().addAll(layout);
            first.getChildren().clear();
            first.getChildren().addAll(search,word, didyoumean);
            try {
                buttonlist=obj.auto(text.toLowerCase());
               // first.getChildren().addAll(obj.auto(text.toLowerCase()));
                for (Button b:buttonlist
                     ) {
                    b.setOnAction(p->{
                        action(b.getText());
                    });

                }
                first.getChildren().addAll(buttonlist);
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        } else {
            first.getChildren().clear();
            word.setText(text);
            first.getChildren().addAll(search,word);
            int length = define.length();
            int count = 0;
            int x = 2;

            Label numbering = new Label("1->");
            numbering.setTextFill(Color.GREEN);
            layout.getChildren().addAll(numbering);


            while (count != length && x < 15) {
                StringBuffer temp1 = new StringBuffer();

                while (count < length && define.charAt(count) >= 97 && define.charAt(count) <= 122) {
                    temp1.append(define.charAt(count++));
                }
                Label b1 = new Label(temp1.toString());
                b1.setTextFill(Color.WHITE);
                b1.setOnMouseEntered(t -> {
                    b1.setTextFill(Color.BLUE);
                });
                b1.setOnMouseExited(t -> {
                    b1.setTextFill(Color.WHITE);
                });
                b1.setOnMouseClicked(t -> {
                    action(b1.getText());
                });
                // b1.setId("fancy");
                //b1.getStyleClass().addAll(".label:hover tot");
                layout.getChildren().addAll(b1);

                if (count < length && define.charAt(count) == 46) {
                    count++;
                    Label dot = new Label(".   ");
                    dot.setTextFill(Color.WHITE);
                    layout.getChildren().addAll(dot);
                    if (count < length) {
                        Label label = new Label(x++ + " ->");
                        label.setTextFill(Color.GREEN);
                        layout.getChildren().addAll(label);
                    }
                } else if (count < length && !(define.charAt(count) >= 97 && define.charAt(count) <= 122)) {
                    Label b2 = new Label(String.valueOf(define.charAt(count++)));
                    b2.setTextFill(Color.WHITE);
                    layout.getChildren().add(b2);


                }

            }


            root.getChildren().addAll(layout);

        }
    }
    public String search(node obj,String word)
    {
        int length=word.length();
        int index=0;
        for (int i = 0; i <length ; i++) {
            index=word.charAt(i)-97;
            if(index<0||index>=26)
                return "Sorry no meaning found";
            if(obj.children[index]==null)
            {
                return  "Sorry no meaning found";
            }
            obj=obj.children[index];

        }
        if(obj.isLeaf) {
            return String.valueOf(obj.defination);
        }
        return "Sorry no meaning found";
    }
}
