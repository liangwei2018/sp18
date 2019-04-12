package synthesizer;

/**
 * An abstract class which implements BoundedQueue, capturing the redundancies
 * between methods in BoundedQueue.
 *
 * @param <T> Generic data type
 *
 * @author Liang Wei
 */
public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {

    protected int fillCount;
    protected int capacity;

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }
}
