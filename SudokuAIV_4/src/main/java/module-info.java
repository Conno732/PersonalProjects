module sudoku.sudokuaiv_4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens sudoku.sudokuaiv_4 to javafx.fxml;
    exports sudoku.sudokuaiv_4;
}