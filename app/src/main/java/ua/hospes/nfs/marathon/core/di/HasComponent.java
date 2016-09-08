package ua.hospes.nfs.marathon.core.di;

/**
 * @author Andrew Khloponin
 */
public interface HasComponent<C> {
    C getComponent();
}