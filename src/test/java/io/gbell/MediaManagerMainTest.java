package io.gbell;

import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThat;

public class MediaManagerMainTest extends FxRobot {

    private MediaManagerMain mediaManagerMain;
    private Stage stage;

//    @Override
//    public void start(Stage stage) throws Exception {
////        mediaManagerMain.start(stage);
//
//    }

    @Before
    public void setUp() throws Exception {
        stage = FxToolkit.registerPrimaryStage();
        mediaManagerMain = (MediaManagerMain) FxToolkit.setupApplication(MediaManagerMain.class);
    }

    @Test
    public void onStart_shouldHaveCorrectTitle() {
        assertThat(stage.getTitle()).isEqualTo("Media Manager");
    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    @Test
//    public void main() throws Exception {
//
//    }
//
//    @Test
//    public void inject() throws Exception {
//
//    }
//
//    @Test
//    public void start() throws Exception {
//
//    }
}