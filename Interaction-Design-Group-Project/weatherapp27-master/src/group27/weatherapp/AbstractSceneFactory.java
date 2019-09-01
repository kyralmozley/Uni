package group27.weatherapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Helper class to create a scene with specific dimensions with the root node specified by subclasses.
 */
public abstract class AbstractSceneFactory extends AbstractNodeLoader{

    public double width = -1;
    public double height = -1;

    public void setDimensions(double v, double v1){
        width = v;
        height = v1;
    }

    public void setDimensions(Scene s){
        setDimensions(s.getWidth(), s.getHeight());
    }

    public Scene loadScene() throws IOException {
        if (width < 0){
            throw new PropertyNotSetException();
        }
        if (height < 0){
            throw new PropertyNotSetException();
        }
        Parent root = loadNode();

        return new Scene(root, width, height);
    }
}

class PropertyNotSetException extends RuntimeException{
}
