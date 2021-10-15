package dk.sunepoulsen.itdeveloper.utils;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ResourceBundle;

public class FXMLUtils {

    public static <T> void initFxmlWithNoBundle(T instance, String fxmlResourceName) {
        FXMLLoader fxmlLoader = new FXMLLoader(instance.getClass().getResource(fxmlResourceName));
        fxmlLoader.setRoot(instance);
        fxmlLoader.setController(instance);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new FXMLException(exception);
        }
    }

    public static <T> void initFxmlWithNoBundle(T instance) {
        initFxmlWithNoBundle(instance, defaultResourceName(instance));
    }

    public static <T> void initFxml(ResourceBundle bundle, T instance, String fxmlResourceName) {
        FXMLLoader fxmlLoader = new FXMLLoader(instance.getClass().getResource(fxmlResourceName));
        fxmlLoader.setRoot(instance);
        fxmlLoader.setController(instance);
        fxmlLoader.setResources(bundle);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new FXMLException(exception);
        }
    }

    public static <T> void initFxml(ResourceBundle bundle, T instance) {
        initFxml(bundle, instance, defaultResourceName(instance));
    }

    public static <T> void initFxml(Registry registry, T instance, String fxmlResourceName) {
        ResourceBundle bundle = registry.getBundle(instance.getClass());

        initFxml(bundle, instance, fxmlResourceName);
    }

    public static <T> void initFxml(Registry registry, T instance) {
        ResourceBundle bundle = registry.getBundle(instance.getClass());

        initFxml(bundle, instance, defaultResourceName(instance));
    }

    public static <T> void initFxml(T instance, String fxmlResourceName) {
        initFxml(Registry.getDefault(), instance, fxmlResourceName);
    }

    public static <T> void initFxml(T instance) {
        initFxml(Registry.getDefault(), instance);
    }

    private static <T> String defaultResourceName(T instance) {
        return instance.getClass().getSimpleName().toLowerCase() + ".fxml";
    }

}
