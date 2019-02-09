package bot_parameters.interfaces;

public interface Copyable<T extends Copyable<T>> {
    T createCopy();
}
