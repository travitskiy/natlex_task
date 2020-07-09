package ru.natlex.task.async;


import java.util.concurrent.locks.ReentrantLock;

public interface LockManager {
    ReentrantLock getLock(String key);
}
