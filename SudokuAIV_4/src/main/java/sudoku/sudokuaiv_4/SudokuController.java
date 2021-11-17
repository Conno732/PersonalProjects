package sudoku.sudokuaiv_4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class SudokuController {
    int SudokuGrid[][];
   @FXML
   GridPane grid = new GridPane();
   @FXML
    Button startButton = new Button();
   @FXML
    Text text = new Text();
   @FXML
    Button solveButton = new Button();
    @FXML
    Button generateButton = new Button();
    @FXML
            Button clearButton = new Button();

   TextField[][] textFields = new TextField[9][9];

   public void onStart(ActionEvent e){
       SudokuGrid = new int[9][9];
       for (int i = 0; i < 9; i++){
           for (int j = 0; j < 9; j++){
               textFields[i][j] = new TextField();
               textFields[i][j].setPrefSize(67,67);
               textFields[i][j].setFont(Font.font("Calibri", FontWeight.BOLD, 27));
               textFields[i][j].setAlignment(Pos.CENTER);
               grid.add(textFields[i][j], j, i, 1, 1);
           }
       }
       startButton.setDisable(true);
       startButton.setVisible(false);
       text.setVisible(true);
       solveButton.setDisable(false);
       generateButton.setDisable(false);
       clearButton.setDisable(false);
   }

   public void Test(){
      // System.out.println(textFields[8][8].getText().equals(""));
   }

   public void Solve(){

       if (setSudokuGrid() == 1){
           text.setText("Invalid Puzzle");
           return;
       }

       if (SudokuAnswer() == 0){
           setTextFields();
           text.setText("Solved!");
       }
       else{
           text.setText("Unsolvable");
       }
   }

   int setSudokuGrid(){
       int tmp;
       for (int i = 0; i < 9; i++){
           for (int j = 0; j < 9; j++) {
               if (textFields[i][j].getText().equals("")){
                   SudokuGrid[i][j] = 0;
               }
               else{
                   try {
                       tmp = Integer.parseInt(textFields[i][j].getText());
                       if (tmp <= 9)
                           SudokuGrid[i][j] = tmp;
                       else {
                           clearSudokuGrid();
                           return 1;
                       }
                   } catch (NumberFormatException e) {
                        clearSudokuGrid();
                        return 1;
                   }
               }
           }
       }
       return 0;
   }

   void setTextFields(){
       for (int i = 0; i < 9; i++){
           for (int j = 0; j < 9; j++) {
               if(SudokuGrid[i][j] != 0)
               textFields[i][j].setText(Integer.toString(SudokuGrid[i][j]));
               else
                   textFields[i][j].setText("");
           }
       }
   }

   public void clear(ActionEvent e){
       clearSudokuGrid();
       setTextFields();
       text.setText("Enter Puzzle:");
   }



////////////////////////////////////////////////////////////////////////////////////////////
    public void generate(ActionEvent e){
        clearSudokuGrid();
        setTextFields();
        Random random = new Random();
        int howMany = random.nextInt(20) + 5;
        int ranX;
        int ranY;
        int ranNum;
        while (howMany >0){
            ranX = random.nextInt(9);
            ranY = random.nextInt(9);
            while (true){
                ranNum = random.nextInt(9);
                ranNum++;
                if (isPossibleAnswer(ranNum, ranY + 1, ranX + 1)) break;
            }
            SudokuGrid[ranY][ranX] = ranNum;
            howMany--;
        }
        setTextFields();
        text.setText("Puzzle Generated:");

    }

    void clearSudokuGrid(){
        SudokuGrid = new int[9][9];
    }

    int SudokuAnswer(){
        if (!checkBoard())
            return -1;
        return SudokuAnswer(1,1);
    }

    boolean isPossibleAnswer(int number, int row, int column){
        if (isInRow(number, row) || isInBox(number,row,column) || isInColumn(number, column)) return false;
        return true;
    }

    public boolean checkBoard(){
        int tmp;
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                if (SudokuGrid[y][x] == 0) continue;
                tmp = SudokuGrid[y][x];
                SudokuGrid[y][x] = 0;
                if (!(isPossibleAnswer(tmp, y + 1, x + 1)))return false;
                SudokuGrid[y][x] = tmp;
            }
        }
        return true;
    }

    int SudokuAnswer(int row, int column){
        int nexCol = column + 1;
        int nextRow = row;

        if (nexCol > 9) {
            nexCol = 1;
            nextRow = row + 1;
        }

        if (row > 9)
            return 0; //returns zero is successful
        if (SudokuGrid[row-1][column-1] != 0){
            return SudokuAnswer(nextRow, nexCol);
        }

        for (int i = 1; i <= 9; i++){
            if (isPossibleAnswer(i, row,column)){
                SudokuGrid[row - 1][column - 1] = i;
                int check = SudokuAnswer(nextRow, nexCol);
                if (check == -1){SudokuGrid[row - 1][column - 1] = 0;}
                else if (check == 0){
                    return 0;
                }
            }
        }
        //no solutions, go back
        return -1;
    }

    public int[][] getBox(int row, int column){
        int boxY = Math.floorDiv(row-1, 3);
        int boxX = Math.floorDiv(column-1, 3);
        int[][] res = new int[3][3];
        for (int y = 0; y < 3; y++){
            for (int x = 0; x < 3; x++){
                res[y][x] = SudokuGrid[y + (boxY*3)][x+ (boxX * 3)];
            }
        }
        return res;
    }

    public boolean isInRow(int number, int row){
        row--;
        if (row < 0 || row >= 9) return false;
        for (int x = 0; x < 9; x++){
            if (SudokuGrid[row][x] == number) return true;
        }
        return false;
    }

    public boolean isInBox(int number, int row, int column){
        int[][] check = getBox(row, column);
        for (int y = 0; y < 3; y++){
            for (int x = 0; x < 3; x++){
                if (check[y][x] == number) return true;
            }
        }
        return false;
    }

    public boolean isInColumn(int number, int column){
        column--;
        if (column < 0 || column >= 9) return false;
        for (int y = 0; y < 9; y++){
            if (SudokuGrid[y][column] == number) return true;
        }
        return false;
    }

}