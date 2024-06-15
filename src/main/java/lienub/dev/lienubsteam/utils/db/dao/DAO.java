package lienub.dev.lienubsteam.utils.db.dao;

import java.util.List;

/**
 * The interface for the Data Access Object.
 *
 * @param <I> object to be manipulated
 * @param <O> object to insert, update, delete
 */
public interface DAO<I,O> {
    /**
     * Insert .
     *
     * @param in the in
     * @return the object to be manipulated
     */
    I insert(I in);

    /**
     * Insert.
     *
     * @param in     the in
     * @param object the object
     */
    void insert(I in, O object);

    /**
     * Update.
     *
     * @param in     the in
     * @param object the object
     */
    void update(I in, O object);

    /**
     * Update.
     *
     * @param in the in
     */
    void update(I in);

    /**
     * Delete.
     *
     * @param in the in
     */
    void delete(I in);

    /**
     * Delete.
     *
     * @param in     the in
     * @param object the object
     */
    void delete(I in, O object);

    /**
     * Get list.
     *
     * @return the list
     */
    List<I> get();

    /**
     * Get .
     *
     * @param object the object
     * @return the object to be manipulated
     */
    I get(O object);
}
