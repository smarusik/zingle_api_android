package me.zingle.api.sdk.logger;

import java.io.PrintStream;
import java.util.Date;

import static me.zingle.api.sdk.logger.ZingleVerbosityLevel.ZINGLE_VERBOSITY_ERROR;
import static me.zingle.api.sdk.logger.ZingleVerbosityLevel.ZINGLE_VERBOSITY_INFO;
/**
 * Class for logging. Has 2 verbosity levels (see ZingleVerbosityLevel enum). By default do output to System.err, but can be redirected to any PrintStream.
 * Don't provide any synchronization, take it into account, when using in multithreaded environments.
 */
public class Log {
    private static ZingleVerbosityLevel level=ZINGLE_VERBOSITY_ERROR;
    private static PrintStream outputStream=System.err;

    /**
     * Initialization method
     * @param level - verbosity level
     * @param outputStream - required PrintStream for output
     */
    public static void init(ZingleVerbosityLevel level, PrintStream outputStream){
        Log.level = level;
        Log.outputStream = outputStream;
    }

    public static void err(String msg){
        if(level==ZINGLE_VERBOSITY_ERROR || level==ZINGLE_VERBOSITY_INFO) {
            synchronized (outputStream) {
                outputStream.println(new Date().toString());
                outputStream.println(msg);
                outputStream.println();
                outputStream.flush();
            }
        }
    }

    public static void err(String format, Object... args ){
        err(String.format(format,args));
    }

    public static void err(Class c,String funcName, String msg){
        StringBuilder b=new StringBuilder();
        b.append("In ").append(c.getSimpleName()).append("::").append(funcName);
        b.append(" error: ").append(msg);

        err(b.toString());
    }

    public static void info(String msg){
        if(level==ZINGLE_VERBOSITY_INFO) {
            synchronized (outputStream) {
                outputStream.println(new Date().toString());
                outputStream.println(msg);
                outputStream.println();
                outputStream.flush();
            }
        }
    }

    public static void info(String format, Object... args ){
        info(String.format(format, args));
    }

    public static void info(Class c,String funcName, String msg){
        StringBuilder b=new StringBuilder();
        b.append("In ").append(c.getSimpleName()).append("::").append(funcName);
        b.append(" ").append(msg);

        info(b.toString());
    }

}
