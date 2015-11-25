package se.tv4.chronometro.internal;

/**
 * Created by dimitris.lachanas on 26/10/15.
 */

import android.util.Log;

/**
 * Wrapper around {@link Log}
 */
public class DebugLog {

    private DebugLog() {}

    /**
     * Send a debug log message
     *
     * @param tag Source of a log message. It usually identifies the class or activity where the log
     * call occurs.
     * @param message The message you would like logged.
     */
    public static void log(String tag, String message) {
        Log.d(tag, message);
    }
}