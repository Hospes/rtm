package ua.hospes.nfs.marathon.core.di;

/**
 * @author Andrew Khloponin
 */
public class Injector {
    @SuppressWarnings("unchecked")
    public static <C> C getComponent(Object o, Class<C> component) {
        return component.cast(((HasComponent) o).getComponent());
    }
}