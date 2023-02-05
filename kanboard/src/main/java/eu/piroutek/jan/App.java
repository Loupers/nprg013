package eu.piroutek.jan;

import eu.piroutek.jan.controller.FrameController;
import eu.piroutek.jan.database.TagDbHandler;
import eu.piroutek.jan.database.TaskDbHandler;
import eu.piroutek.jan.model.Tag;
import eu.piroutek.jan.model.Task;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        new FrameController();
    }
}