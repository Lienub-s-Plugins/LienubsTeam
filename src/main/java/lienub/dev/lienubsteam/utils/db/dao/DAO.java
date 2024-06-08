package lienub.dev.lienubsteam.utils.db.dao;

import java.util.List;

public interface DAO<I,O> {
    I insert(I in);
    void insert(I in, O object);
    void update(I in, O object);
    void update(I in);
    void delete(I in);
    void delete(I in, O object);
    List<I> get();
    I get(O object);
}
