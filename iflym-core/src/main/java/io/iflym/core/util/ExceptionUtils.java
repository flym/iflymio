package io.iflym.core.util;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 异常工具类
 * Created by flym on 7/10/2017.
 *
 * @author flym
 */
@Slf4j
public class ExceptionUtils {
    public interface Action0 {
        /**
         * 执行无返回操作
         *
         * @throws Throwable 在执行过程中允许throw任何异常
         */
        void run() throws Throwable;
    }

    public interface Fun0<T> {
        /**
         * 执行相应的操作
         *
         * @return 此操作有返回值, 这里返回相应的实际对象
         * @throws Throwable 在执行过程中允许throw任何异常
         */
        T call() throws Throwable;
    }

    /** 执行无返回操作,如果出现异常,则记录之(并不抛出) */
    @Deprecated
    public static void doActionLogE(Action0 action0) {
        doActionLogE(log, action0);
    }

    /** 执行无返回操作,如果出现异常,则记录之(并不抛出) */
    public static void doActionLogE(Logger logger, Action0 action0) {
        try{
            action0.run();
        } catch(Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /** 执行无返回操作,如果出现异常,则转为运行期异常 */
    public static void doActionRethrowE(Action0 action0) {
        try{
            action0.run();
        } catch(Throwable e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void doActionLogAndRethrowE(Logger logger, Action0 action0) {
        try{
            action0.run();
        } catch(Throwable e) {
            logger.error(e.getMessage(), e);
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /** 执行有返回操作,如果出现异常,则记录之(并不抛出),并返回null */
    @Deprecated
    public static <T> T doFunLogE(Fun0<T> fun0) {
        return doFunLogE(log, fun0);
    }

    /** 执行有返回操作,如果出现异常,则记录之(并不抛出),并返回null */
    public static <T> T doFunLogE(Logger logger, Fun0<T> fun0) {
        try{
            return fun0.call();
        } catch(Throwable e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /** 执行有返回操作,如果出现异常,则转换为运行期异常 */
    public static <T> T doFunRethrowE(Fun0<T> fun0) {
        try{
            return fun0.call();
        } catch(Throwable e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /** 执行有返回操作,如果出现异常,记录之,同时转换为运行期异常 */
    public static <T> T doFunLogAndRethrowE(Logger logger, Fun0<T> fun0) {
        try{
            return fun0.call();
        } catch(Throwable e) {
            logger.error(e.getMessage(), e);
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
