package cs1302.omega;

import cs1302.game.DemoGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import java.util.Random;

/**
 * This class will create a PONG game in which a ball will bounce between player 1 (the user) and
 * player 2 (the computer). If the ball sucessfully goes beyond either player 1 or player 2's
 * rectangle, then the game will grant points to whoever got the ball past the other.
 */
public class OmegaApp extends Application {

    // Creating the variables that will be used throughout this project.

    private boolean playing = false; // Will determine if the game is currently running.

    private int playerHeight = 110; //The height of the players rectangle.
    private int playerWidth =  10; //The width of the players rectangle.

    private double bRadius = 15; //Setting the size for the ball.
    private double bX = 300; //Setting the balls X position.
    private double bY = 300; //Setting the balls Y position.
    private double bXSpeed = 1.0; //Setting the balls speed.
    private double bYSpeed = 1.0; //Setting the balls speed.

    private int player1Score = 0; //Will be used to calculate player 1's score.
    private int player2Score = 0; //Will be used to calculate player 2's score.

    private int p1X = 0; //Setting the X position for player 1.
    private double p1Y = 300; //Setting the Y position for player 1
    private double p2X = 790; //Setting the X position for player 2.
    private double p2Y = 300; //Setting the Y position for player 2.

    private Canvas canvas;
    private GraphicsContext context;



    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        //setup scene
        VBox root = new VBox();
        canvas = new Canvas(800, 600);
        context = canvas.getGraphicsContext2D();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), e -> play(context));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        canvas.setOnMouseClicked(e -> playing = true);
        canvas.setOnMouseMoved(e -> p1Y = e.getY());

        stage.setTitle("Pong");
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        timeline.play();

    } // start

    /**
     * This method will draw the two rectangles on both sides of the game board.
     */
    private void drawPlayers() {

        context.fillRect(p1X, p1Y, playerWidth, playerHeight);
        context.fillRect(p2X, p2Y, playerWidth, playerHeight);

    } //drawPlayers

    /**
     * This method will draw the ball.
     */
    private void drawBall() {
        context.fillOval(bX, bY, bRadius, bRadius);
    } //drawBall

    /**
     * This method will determine if the ball goes off the board. If the ball goes off the board,
     * then the game will be stopped and give the display the score.
     */
    private void pointCounter() {
        if (bX < p1X - playerWidth) {
            player2Score++;
            playing = false;
        } //if
        if (bX > p2X + playerWidth) {
            player1Score++;
            playing = false;
        } //if
        context.fillText(player1Score + "\t\t\t\t" + player2Score, 400, 100);

    } //pointCounter

    /**
     * This method will gradually increase the speed of the ball once the ball is hit by either
     * player.
     */
    public void getBallSpeed() {

        if (((bX + bRadius > p2X) && bY >= p2Y && bY <= p2Y + playerHeight) ||
            ((bX < p1X + playerWidth) && bY >= p1Y && bY <= p1Y + playerHeight)) {
            bXSpeed += 1 * Math.signum(bXSpeed);
            bXSpeed *= -1;
            bYSpeed += 1 * Math.signum(bYSpeed);
            bYSpeed *= -1;
        } //if

    } //getBallSpeed

    /**
     * This method will do things like, set the balls movement, create a computer that will follow
     * the ball, reset the ball to the center and reset the balls speed and direction.
     */
    private void ifPlaying() {

        if (playing) {
            bX += bXSpeed;
            bY += bYSpeed;

            if (bX < 800 - 800 / 4) {
                p2Y = bY - playerHeight / 2;
            } else {
                p2Y = bY > p2Y + playerHeight / 2 ? p2Y += 1 : p2Y - 1;
            } //if

            drawBall();
//            context.fillOval(bX, bY, bRadius, bRadius);

        } else {
            context.setStroke(Color.WHITE);
            context.setTextAlign(TextAlignment.CENTER);
            context.strokeText("Click to start!", 400, 300);
            bX = 400;
            bY = 300;
            bXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            bYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
        } //if

    } //ifPlaying

    /**
     * This method will make sure that the ball stays on the screen and doesnt wonder off.
     */
    private void ballChecker() {

        if (bY > 600 || bY < 0) {
            bYSpeed *= -1;
        } //if

    } //ballChecker


    /**
     * This will start the game and create the graphics.
     * @param context The graphics context
     */
    private void play(GraphicsContext context) {

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, 800, 600);
        context.setFill(Color.WHITE);
        context.setFont(Font.font(50));

        ifPlaying();
        drawPlayers();
        ballChecker();
        getBallSpeed();
        pointCounter();

    } //play

} // OmegaApp
