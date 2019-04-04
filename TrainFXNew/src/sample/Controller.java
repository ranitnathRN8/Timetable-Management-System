package sample;

import Ranit.Datasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;


public class Controller {
    @FXML
    public Button createButton;
    @FXML
    public Label datasheetLabel;
    @FXML
    public CheckBox checkBox;
    @FXML
    public Label labelCheckTrain;
    @FXML
    public Label labelNewTrain;
//    @FXML
//    public TextField checkTrainText;
    @FXML
    public TextField newTrainText;
    @FXML
    public Button updateButton;
    @FXML
    public Button clear;
    @FXML
    public void initialize(){
        updateButton.setDisable(true);
        newTrainText.setDisable(true);
       // checkTrainText.setDisable(true);
        clear.setDisable(true);
    }
    @FXML
    private void onButtonClicked1(ActionEvent e){
        String sb;
        if (e.getSource().equals(createButton)){
            if(Datasource.getInstance().open()){
                sb = Datasource.getInstance().createTableForPlatform();
                datasheetLabel.setText(sb);
                Datasource.getInstance().close();
            }

        }
        /*else if(e.getSource().equals(checkBox))
        {
            datasheetLabel.setText("Platform datasheet already created");
        }*/
    }
    @FXML
    private void onButtonClicked2(ActionEvent e){
        if (e.getSource().equals(checkBox)){
            updateButton.setDisable(false);
            newTrainText.setDisable(false);
            //checkTrainText.setDisable(false);
            createButton.setDisable(true);
            clear.setDisable(false);
            datasheetLabel.setText("Platform table already created");
        }
    }
//    @FXML
//    private int handleKeyreleased(){
//        //if (e.getSource().equals(updateButton)) {
//            String checktrainId = checkTrainText.getText();
//            //String nextTrainId = newTrainText.getText();
//            int checkid = Integer.parseInt(checktrainId);
//            return checkid;
//            //int nextid = Integer.parseInt(nextTrainId);
//            /*if (Datasource.getInstance().open()) {
//                Datasource.getInstance().checkIfStatement(checkid, nextid);
//                datasheetLabel.setText("Platform table updated");
//                Datasource.getInstance().close();
//            }*/
//        //}
//    }
    @FXML
    private int handleKeyreleased2(){
        String nextTrainId = newTrainText.getText();
        int nextid = Integer.parseInt(nextTrainId);
        return nextid;
    }
    @FXML
    private void onButtonClicked3(ActionEvent e){
        int checkid = 1;
        int nextid = handleKeyreleased2();
        String stat;
        if (Datasource.getInstance().open()) {
            stat = Datasource.getInstance().checkIfStatement(checkid, nextid);
            datasheetLabel.setText(stat);
            Datasource.getInstance().close();
        }
    }
    @FXML
    private void onButtonClicked4(ActionEvent e){
        if (e.getSource().equals(clear)){
            //checkTrainText.clear();
           newTrainText.clear();
        }
    }
}
