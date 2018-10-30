package redisson;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.Signal;

public class RedisDistMutexElector implements IDistMutexElector {
    @Override
    public void becomeActiveOrDie(Class var1) throws InterruptedException {

    }
//    private static final Logger log = LoggerFactory.getLogger(RedisDistMutexElector.class);
//    private static final String ELECTOR_CONFIG_NAME_FORMAT = "%s.mutex.elector";
//    private static final int HERTBEAT_SEND_INTERVAL = 1000;
//    private static final int HERTBEAT_SYNC_THRESHOLD = 5000;
//    @Autowired
//    RedisService redisService;
//    @Autowired
//    IDistLockFactory lockFactory;
//    private RedisDistMutexElector.ElectorAttribute selfAttribute = getSelfElectorAttributeOrDie();
//    private RedisDistMutexElector.HeartBeatSender heartBeatSender = new RedisDistMutexElector.HeartBeatSender();
//    private RedisDistMutexElector.ElectFailExceptionHandler electFailExceptionHandler = new RedisDistMutexElector.ElectFailExceptionHandler(Thread.currentThread());
//
//    @PostConstruct
//    public void init() {
//        this.heartBeatSender.setUncaughtExceptionHandler(this.electFailExceptionHandler);
//    }
//
//    public void becomeActiveOrDie(Class electorClazz) throws InterruptedException {
//        log.error("becomeActive with mutex elector {}", electorClazz.getName());
//        IDistLock lock = this.lockFactory.getLock(RedisDistMutexElector.class.getName());
//        if (!lock.tryLock(1L, 10L, TimeUnit.SECONDS)) {
//            throw new RedisDistMutexElector.ElectFailedException("try lock failed before becomeActiveOrDie");
//        } else {
//            try {
//                this.heartBeatSender.setElectorClazz(electorClazz);
//                this.selfAttribute.updateTimestamp();
//                String content = this.getElectorAttribute(electorClazz);
//                if (content == null) {
//                    this.updateElectorAttributeWithRetry(electorClazz, 5);
//                    this.heartBeatSender.start();
//                } else {
//                    RedisDistMutexElector.ElectorAttribute attribute = (RedisDistMutexElector.ElectorAttribute) JsonUtil.toJava(content, RedisDistMutexElector.ElectorAttribute.class);
//                    if (this.selfAttribute.isSameElector(attribute)) {
//                        if (this.selfAttribute.isElectorSynced(attribute)) {
//                            throw new RedisDistMutexElector.ElectFailedException("became actived already, repeated attempts");
//                        }
//
//                        throw new RedisDistMutexElector.ElectFailedException("unsynced elector");
//                    }
//
//                    if (this.selfAttribute.isElectorSynced(attribute)) {
//                        throw new RedisDistMutexElector.ElectFailedException("Another elector is running.");
//                    }
//
//                    this.updateElectorAttributeWithRetry(electorClazz, 5);
//                    this.heartBeatSender.start();
//                }
//            } catch (Exception var8) {
//                log.error(var8.getMessage(), var8);
//                Signal.raise(new Signal("TERM"));
//            } finally {
//                lock.forceUnlock();
//            }
//
//        }
//    }
//
//    private String getElectorAttribute(Class electorClazz) {
//        return this.redisService.getString(String.format("%s.mutex.elector", electorClazz.getSimpleName()));
//    }
//
//    private void updateElectorAttribute(Class electorClazz) {
//        this.selfAttribute.updateTimestamp();
//        this.redisService.put(String.format("%s.mutex.elector", electorClazz.getSimpleName()), JsonUtil.toJson(this.selfAttribute));
//    }
//
//    private void updateElectorAttributeWithRetry(Class electorClazz, int retyMax) {
//        try {
//            this.updateElectorAttribute(electorClazz);
//        } catch (Exception var4) {
//            if (retyMax <= 0) {
//                throw new RedisDistMutexElector.ElectFailedException(var4.getMessage(), var4);
//            }
//
//            log.error("update elector attribute failed: {}, retry...", var4.getMessage());
//            this.updateElectorAttributeWithRetry(electorClazz, retyMax - 1);
//        }
//
//    }
//
//    public static RedisDistMutexElector.ElectorAttribute getSelfElectorAttributeOrDie() {
//        RedisDistMutexElector.ElectorAttribute electorAttribute = new RedisDistMutexElector.ElectorAttribute();
//        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//        electorAttribute.setName(runtimeMXBean.getName());
//        electorAttribute.setStartTime(runtimeMXBean.getStartTime());
//        return electorAttribute;
//    }
//
//    public RedisDistMutexElector() {
//    }
//
//    public RedisService getRedisService() {
//        return this.redisService;
//    }
//
//    public IDistLockFactory getLockFactory() {
//        return this.lockFactory;
//    }
//
//    public RedisDistMutexElector.ElectorAttribute getSelfAttribute() {
//        return this.selfAttribute;
//    }
//
//    public RedisDistMutexElector.HeartBeatSender getHeartBeatSender() {
//        return this.heartBeatSender;
//    }
//
//    public RedisDistMutexElector.ElectFailExceptionHandler getElectFailExceptionHandler() {
//        return this.electFailExceptionHandler;
//    }
//
//    public void setRedisService(RedisService redisService) {
//        this.redisService = redisService;
//    }
//
//    public void setLockFactory(IDistLockFactory lockFactory) {
//        this.lockFactory = lockFactory;
//    }
//
//    public void setSelfAttribute(RedisDistMutexElector.ElectorAttribute selfAttribute) {
//        this.selfAttribute = selfAttribute;
//    }
//
//    public void setHeartBeatSender(RedisDistMutexElector.HeartBeatSender heartBeatSender) {
//        this.heartBeatSender = heartBeatSender;
//    }
//
//    public void setElectFailExceptionHandler(RedisDistMutexElector.ElectFailExceptionHandler electFailExceptionHandler) {
//        this.electFailExceptionHandler = electFailExceptionHandler;
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) {
//            return true;
//        } else if (!(o instanceof RedisDistMutexElector)) {
//            return false;
//        } else {
//            RedisDistMutexElector other = (RedisDistMutexElector)o;
//            if (!other.canEqual(this)) {
//                return false;
//            } else {
//                label71: {
//                    Object this$redisService = this.getRedisService();
//                    Object other$redisService = other.getRedisService();
//                    if (this$redisService == null) {
//                        if (other$redisService == null) {
//                            break label71;
//                        }
//                    } else if (this$redisService.equals(other$redisService)) {
//                        break label71;
//                    }
//
//                    return false;
//                }
//
//                Object this$lockFactory = this.getLockFactory();
//                Object other$lockFactory = other.getLockFactory();
//                if (this$lockFactory == null) {
//                    if (other$lockFactory != null) {
//                        return false;
//                    }
//                } else if (!this$lockFactory.equals(other$lockFactory)) {
//                    return false;
//                }
//
//                label57: {
//                    Object this$selfAttribute = this.getSelfAttribute();
//                    Object other$selfAttribute = other.getSelfAttribute();
//                    if (this$selfAttribute == null) {
//                        if (other$selfAttribute == null) {
//                            break label57;
//                        }
//                    } else if (this$selfAttribute.equals(other$selfAttribute)) {
//                        break label57;
//                    }
//
//                    return false;
//                }
//
//                Object this$heartBeatSender = this.getHeartBeatSender();
//                Object other$heartBeatSender = other.getHeartBeatSender();
//                if (this$heartBeatSender == null) {
//                    if (other$heartBeatSender != null) {
//                        return false;
//                    }
//                } else if (!this$heartBeatSender.equals(other$heartBeatSender)) {
//                    return false;
//                }
//
//                Object this$electFailExceptionHandler = this.getElectFailExceptionHandler();
//                Object other$electFailExceptionHandler = other.getElectFailExceptionHandler();
//                if (this$electFailExceptionHandler == null) {
//                    if (other$electFailExceptionHandler == null) {
//                        return true;
//                    }
//                } else if (this$electFailExceptionHandler.equals(other$electFailExceptionHandler)) {
//                    return true;
//                }
//
//                return false;
//            }
//        }
//    }
//
//    protected boolean canEqual(Object other) {
//        return other instanceof RedisDistMutexElector;
//    }
//
//
//    public String toString() {
//        return "RedisDistMutexElector(redisService=" + this.getRedisService() + ", lockFactory=" + this.getLockFactory() + ", selfAttribute=" + this.getSelfAttribute() + ", heartBeatSender=" + this.getHeartBeatSender() + ", electFailExceptionHandler=" + this.getElectFailExceptionHandler() + ")";
//    }
//
//    public class HeartBeatSender extends Thread {
//        boolean started = false;
//        Class electorClazz;
//
//        public void run() {
//            if (this.started) {
//                throw new RedisDistMutexElector.ElectFailedException("heart beat pusher scheduled repeatedly");
//            } else {
//                this.started = true;
//
//                while(true) {
//                    try {
//                        String content = RedisDistMutexElector.this.getElectorAttribute(this.electorClazz);
//                        RedisDistMutexElector.ElectorAttribute attribute = (RedisDistMutexElector.ElectorAttribute)JsonUtil.toJava(content, RedisDistMutexElector.ElectorAttribute.class);
//                        Preconditions.checkState(RedisDistMutexElector.this.selfAttribute.isSameElector(attribute), "inconsist elector for heartbeat: self: %s => target: %s", new Object[]{RedisDistMutexElector.this.selfAttribute, attribute});
//                        Preconditions.checkState(RedisDistMutexElector.this.selfAttribute.isElectorSynced(attribute), "elector unsynced for heartbeat: self: %s => target: %s", new Object[]{RedisDistMutexElector.this.selfAttribute, attribute});
//                        RedisDistMutexElector.this.updateElectorAttributeWithRetry(this.electorClazz, 5);
//                        sleep(1000L);
//                    } catch (Exception var3) {
//                        throw new RedisDistMutexElector.ElectFailedException(var3.getMessage(), var3);
//                    }
//                }
//            }
//        }
//
//        public HeartBeatSender() {
//        }
//
//        public boolean isStarted() {
//            return this.started;
//        }
//
//        public Class getElectorClazz() {
//            return this.electorClazz;
//        }
//
//        public void setStarted(boolean started) {
//            this.started = started;
//        }
//
//        public void setElectorClazz(Class electorClazz) {
//            this.electorClazz = electorClazz;
//        }
//
//        public boolean equals(Object o) {
//            if (o == this) {
//                return true;
//            } else if (!(o instanceof RedisDistMutexElector.HeartBeatSender)) {
//                return false;
//            } else {
//                RedisDistMutexElector.HeartBeatSender other = (RedisDistMutexElector.HeartBeatSender)o;
//                if (!other.canEqual(this)) {
//                    return false;
//                } else if (this.isStarted() != other.isStarted()) {
//                    return false;
//                } else {
//                    Object this$electorClazz = this.getElectorClazz();
//                    Object other$electorClazz = other.getElectorClazz();
//                    if (this$electorClazz == null) {
//                        if (other$electorClazz != null) {
//                            return false;
//                        }
//                    } else if (!this$electorClazz.equals(other$electorClazz)) {
//                        return false;
//                    }
//
//                    return true;
//                }
//            }
//        }
//
//        protected boolean canEqual(Object other) {
//            return other instanceof RedisDistMutexElector.HeartBeatSender;
//        }
//
//        public String toString() {
//            return "RedisDistMutexElector.HeartBeatSender(started=" + this.isStarted() + ", electorClazz=" + this.getElectorClazz() + ")";
//        }
//    }
//
//    public static class ElectorAttribute {
//        private String name;
//        private long startTime;
//        private Date timestamp;
//
//        public boolean isSameElector(RedisDistMutexElector.ElectorAttribute other) {
//            if (other == null) {
//                return false;
//            } else if (this.name != null && other.getName() != null) {
//                return this.name.equals(other.getName()) && other.getStartTime() == this.startTime;
//            } else {
//                return false;
//            }
//        }
//
//        public boolean isElectorSynced(RedisDistMutexElector.ElectorAttribute other) {
//            if (other == null) {
//                return false;
//            } else if (this.timestamp != null && other.getTimestamp() != null) {
//                return Math.abs(this.timestamp.getTime() - other.getTimestamp().getTime()) < 5000L;
//            } else {
//                return false;
//            }
//        }
//
//        public void updateTimestamp() {
//            this.timestamp = new Date();
//        }
//
//        public ElectorAttribute() {
//        }
//
//        public String getName() {
//            return this.name;
//        }
//
//        public long getStartTime() {
//            return this.startTime;
//        }
//
//        public Date getTimestamp() {
//            return this.timestamp;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setStartTime(long startTime) {
//            this.startTime = startTime;
//        }
//
//        public void setTimestamp(Date timestamp) {
//            this.timestamp = timestamp;
//        }
//
//        public boolean equals(Object o) {
//            if (o == this) {
//                return true;
//            } else if (!(o instanceof RedisDistMutexElector.ElectorAttribute)) {
//                return false;
//            } else {
//                RedisDistMutexElector.ElectorAttribute other = (RedisDistMutexElector.ElectorAttribute)o;
//                if (!other.canEqual(this)) {
//                    return false;
//                } else {
//                    label39: {
//                        Object this$name = this.getName();
//                        Object other$name = other.getName();
//                        if (this$name == null) {
//                            if (other$name == null) {
//                                break label39;
//                            }
//                        } else if (this$name.equals(other$name)) {
//                            break label39;
//                        }
//
//                        return false;
//                    }
//
//                    if (this.getStartTime() != other.getStartTime()) {
//                        return false;
//                    } else {
//                        Object this$timestamp = this.getTimestamp();
//                        Object other$timestamp = other.getTimestamp();
//                        if (this$timestamp == null) {
//                            if (other$timestamp != null) {
//                                return false;
//                            }
//                        } else if (!this$timestamp.equals(other$timestamp)) {
//                            return false;
//                        }
//
//                        return true;
//                    }
//                }
//            }
//        }
//
//        protected boolean canEqual(Object other) {
//            return other instanceof RedisDistMutexElector.ElectorAttribute;
//        }
//
//
//        public String toString() {
//            return "RedisDistMutexElector.ElectorAttribute(name=" + this.getName() + ", startTime=" + this.getStartTime() + ", timestamp=" + this.getTimestamp() + ")";
//        }
//    }
//
//    public static class ElectFailedException extends RuntimeException {
//        public ElectFailedException(String message) {
//            super(message);
//        }
//
//        public ElectFailedException(String message, Throwable e) {
//            super(message, e);
//        }
//    }
//
//    public static class ElectFailExceptionHandler implements UncaughtExceptionHandler {
//        Thread mainThread;
//
//        public ElectFailExceptionHandler(Thread mainThread) {
//            this.mainThread = mainThread;
//        }
//
//        public void uncaughtException(Thread t, Throwable e) {
//            RedisDistMutexElector.log.error(e.getMessage(), e);
//            Signal.raise(new Signal("TERM"));
//        }
//    }
}

