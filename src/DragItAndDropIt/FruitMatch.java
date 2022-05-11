
package DragItAndDropIt;

import java.util.Arrays;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class FruitMatch extends Application {
    
    

    Circle circle1 = createCircle(50,50);
    Circle circle2 = createCircle(450,50);
    Circle circle3 = createCircle(870,50);
    Circle circle4 = createCircle(50,550);
    Circle circle5 = createCircle(450,550);
    Circle circle6 = createCircle(850,550);
    Circle[] circles = {circle1,circle2,circle3,circle4,circle5,circle6};
    Fruit[] fruitArray = {new Fruit("Apple",getClass().getResource("/FruitImg/pinkLady.png").toExternalForm()),
new Fruit("Orange",getClass().getResource("/FruitImg/orange.png").toExternalForm()),
new Fruit("Papaya",getClass().getResource("/FruitImg/papaya.png").toExternalForm()),
new Fruit("Mango",getClass().getResource("/FruitImg/mango.png").toExternalForm()),
new Fruit("Grape",getClass().getResource("/FruitImg/grapes.png").toExternalForm()),
new Fruit("Guava",getClass().getResource("/FruitImg/guava.png").toExternalForm())};
    
 
final int complete = 10;
    int scoreCount;
    long startTime;
    Double finalScore = null;
    int[] value = new Random()
        .ints(0, fruitArray.length)
        .distinct()
        .limit(fruitArray.length)
        .toArray();
    Button startGame;
    Label GameInfo;
    Fruit rndFruit = fruitArray[value[0]];
    Pane root = new Pane();
Circle targetCircle;
    @Override
    public void start(Stage primaryStage) {
        try {
            startScreen();
            Scene scene = new Scene(root,900,600);
            scene.setFill(Color.ANTIQUEWHITE);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    public void startScreen()
    {
            if(finalScore == null)
            {
                startGame = new Button("Start Game");
            GameInfo = new Label("In this game you have to match the fruit from the middle of the\n"
                    + "screen to the matching fruit on the edges of the screen."
                    + "When you\nhave matched ten pairs of fruit, your score will be shown on how fast\n"
                    + "you were able to match will be shown in seconds. Gooooood luck!!!");
            }
            else
            {
            startGame = new Button("Play again");
            GameInfo = new Label(finalScore+" seconds was how fast you were able to match ten fruit. Well done!!!");
            GameInfo.setTextFill(Color.RED);
            }
            GameInfo.setFont(new Font("Arial", 13));
            
            GameInfo.setLayoutX(270);
            GameInfo.setLayoutY(140);
            startGame.setLayoutX(400);
            startGame.setLayoutY(220);
            
            startGame.setOnAction(value ->{
                startTime = System.nanoTime();
                startGame.setVisible(false);
                GameInfo.setVisible(false);
            SetTarget(FruitSelection());
            });
            
            root.getChildren().add(GameInfo);
            root.getChildren().add(startGame);
    }
     void enableDrag(final ImageView source){
        
        source.setOnDragDetected(new EventHandler <MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               
               /* allow any transfer mode */
               Dragboard db = source.startDragAndDrop(TransferMode.COPY);
               
               /* put a image on dragboard */
               ClipboardContent content = new ClipboardContent();
               Image sourceImage = source.getImage();
               content.putImage(sourceImage);
               db.setContent(content);
               
               event.consume();
           }
       });

    }
    
     void receiveImg(final Circle target){
        
        target.setOnDragOver(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                
                if(db.hasImage()&& targetCircle.getCenterX() == target.getCenterX() && targetCircle.getCenterY() == target.getCenterY()){

                    event.acceptTransferModes(TransferMode.COPY);
                }
                
                event.consume();
            }
        });

        target.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
 
                Dragboard db = event.getDragboard();
                
                if(db.hasImage()){
                    root.getChildren().clear();
                    if(scoreCount >=complete)
                    {
                    scoreCount=0;
                    finalScore =TimeScore(startTime);
                    startScreen();
                    }
                    else
                    {
                    value = new Random()
                            .ints(0, fruitArray.length)
                            .distinct()
                            .limit(fruitArray.length)
                            .toArray();
                    SetTarget(FruitSelection());
                    event.setDropCompleted(true);
                    }
                    
                    
                }else{
                    event.setDropCompleted(false);
                }
                
                event.consume();
            }
        });
        
    }
     
     public void SetTarget(String FruitName)
     {
         
         StackPane imageContainer;
            
            int count = 0;
            
            for(Fruit f: fruitArray)
            {
            imageContainer = new StackPane();
            Image rndFruitImg = new Image(f.getImg(),100,100,false,false);
            PixelReader reader = rndFruitImg.getPixelReader();
            WritableImage newImage = new WritableImage(reader, 6,6, 90, 90);
            ImageView ImgView = new ImageView(newImage);
            imageContainer.getChildren().add(ImgView);
            imageContainer.setLayoutX(circles[value[count]].getCenterX()-50);
            imageContainer.setLayoutY(circles[value[count]].getCenterY()-50);
            if(f.getName().equals(FruitName))
            {
            targetCircle = circles[value[count]];
            receiveImg(circles[value[count]]);
            }
            root.getChildren().add(imageContainer);
            if(count<5)
            {
            count++;
            }
            
            }
            scoreCount++;
            root.getChildren().addAll(Arrays.asList(circles));
         
   
     }
     
    public String FruitSelection()
    {
        rndFruit = fruitArray[value[0]];
    Image rndFruitImg = new Image(rndFruit.getImg(),100,100,false,false);
            PixelReader reader = rndFruitImg.getPixelReader();
            WritableImage newImage = new WritableImage(reader, 6,6, 90, 90);
            ImageView imagefruitView = new ImageView(newImage);
            imagefruitView.setY(220);
            imagefruitView.setX(400);
            enableDrag(imagefruitView);
            root.getChildren().add(imagefruitView);
            return rndFruit.getName();
    }
    
 public double TimeScore(long startTime)
 {
     long elapsedTime = System.nanoTime() - this.startTime;
        double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000;
        return (double) Math.round(elapsedTimeInSeconds * 100) / 100;
       
 }
    
    private Circle createCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(35);
        circle.setFill(Color.TRANSPARENT);
        return circle;
    }
    public static void main(String[] args) {
        launch(args);
    }
}