package ru.natlex.task.async;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class NatlexLockManager implements LockManager {

    private final ConcurrentHashMap<String, ReentrantLock> lockMap;

    public NatlexLockManager() {
        this.lockMap = new ConcurrentHashMap<>();
    }

    @Override
    public ReentrantLock getLock(String key) {
        String lockKey = StringUtils.isEmpty(key) ? "nullKey" : key;

        ReentrantLock reentrantLock = lockMap.get(lockKey);
        if (reentrantLock == null) {
            synchronized (lockMap) {
                reentrantLock = lockMap.get(lockKey);
                if (reentrantLock == null) {
                    reentrantLock = new ReentrantLock();
                    lockMap.put(lockKey, reentrantLock);
                }
            }
        }
        return reentrantLock;
    }
}
