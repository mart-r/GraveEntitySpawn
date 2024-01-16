package ges.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class WrongLocationFixer implements Filter
{
    public Filter.Result checkMessage(String msg)
    {
        if (msg.contains("Wrong location! (") && msg.contains(") should be ("))
            return Filter.Result.DENY;
        return Filter.Result.NEUTRAL;
    }

    public LifeCycle.State getState()
    {
        return LifeCycle.State.STARTED;
    }

    public void initialize()
    {
    }

    public boolean isStarted()
    {
        return false;
    }

    public boolean isStopped()
    {
        return false;
    }

    public void start()
    {
    }

    public void stop()
    {
    }

    public Filter.Result filter(LogEvent e)
    {
        return checkMessage(e.getMessage().getFormattedMessage());
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object... arg4)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object msg, Throwable arg4)
    {
        return checkMessage(msg.toString());
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message msg, Throwable arg4)
    {
        return checkMessage(msg.getFormattedMessage());
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12)
    {
        return checkMessage(msg);
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String msg, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13)
    {
        return checkMessage(msg);
    }

    public Filter.Result getOnMatch()
    {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result getOnMismatch()
    {
        return Filter.Result.NEUTRAL;
    }
}
