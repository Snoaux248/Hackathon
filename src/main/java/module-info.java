module com.project.hackathon.hackathon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.project.hackathon.hackathon to javafx.fxml;
    exports com.project.hackathon.hackathon;
}