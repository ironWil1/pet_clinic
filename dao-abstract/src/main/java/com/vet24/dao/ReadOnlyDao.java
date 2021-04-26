package com.vet24.dao;

import java.io.Serializable;
import java.util.Collection;

public interface ReadOnlyDao<K extends Serializable, T> {

    T getByKey (K key);

    boolean isExistByKey (K key);

    Collection<T> getAll ();
}
