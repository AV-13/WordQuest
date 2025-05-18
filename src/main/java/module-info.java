module org.imie.tp_vocabulaire {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.imie.tp_vocabulaire to javafx.fxml;
    exports org.imie.tp_vocabulaire;
    exports org.imie.model;
}