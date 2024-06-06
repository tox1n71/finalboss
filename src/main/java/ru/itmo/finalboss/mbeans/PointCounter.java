package ru.itmo.finalboss.mbeans;


import javax.management.*;

public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean, NotificationEmitter {
    private int totalPoints = 0;
    private int missedPoints = 0;
    private int consecutiveMisses = 0;
    private long sequenceNumber = 1;

    @Override
    public synchronized void incrementTotalPoints() {
        totalPoints++;
    }

    @Override
    public synchronized void incrementMissedPoints() {
        missedPoints++;
        consecutiveMisses++;
        if (consecutiveMisses == 4) {
            Notification notification = new Notification(
                    "missedPointsNotification",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "4 В ряд ХА-ХА-ХА. Ну и ЛОШАРА"
            );
            sendNotification(notification);
        }

    }

    @Override
    public synchronized void resetConsecutiveMisses() {
        consecutiveMisses = 0;
    }

    @Override
    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public int getMissedPoints() {
        return missedPoints;
    }

    @Override
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) {
        super.addNotificationListener(listener, filter, handback);
    }

    @Override
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        super.removeNotificationListener(listener);
    }
}


