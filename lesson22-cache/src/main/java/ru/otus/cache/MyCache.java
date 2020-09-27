package ru.otus.cache;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import static java.util.Objects.nonNull;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    public static final String REMOVE_ACTION = "REMOVE";
    public static final String ADD_ACTION = "ADD";
    private final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final WeakHashMap<K, V> hashMap = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        hashMap.put(key, value);
        notifyListeners(key, value, ADD_ACTION);
    }

    @Override
    public void remove(K key) {
        val obj = hashMap.remove(key);
        if (nonNull(obj))
            notifyListeners(key, obj, REMOVE_ACTION);
    }

    @Override
    public V get(K key) {
        return hashMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String operation) {
        val hwListeners = new HashSet<>(listeners);
        hwListeners.forEach(listener -> notifyListener(listener, key, value, operation));
    }

    private void notifyListener(HwListener<K, V> listener, K key, V value, String operation) {
        try {
            listener.notify(key, value, operation);
        } catch (Exception e) {
            logger.error("Listener notify error", e);
        }
    }
}
