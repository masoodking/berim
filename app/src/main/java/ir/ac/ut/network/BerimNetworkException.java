package ir.ac.ut.network;

/**
 * Created by Masood on 12/9/2015 AD.
 */
public class BerimNetworkException extends Exception {

    public static final String ERROR_GENERAL = "general";

    private static final long serialVersionUID = 1L;

    private int mErrorCode;

    private String mError;

    public BerimNetworkException(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mError = errorMessage;
    }

    public BerimNetworkException() {
        mErrorCode = 0;
        mError = "general error";
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    @Override
    public String getMessage() {
        return mError;
    }

    @Override
    public String toString() {
        return "BerimNetworkException :: " + mErrorCode + " errors=" + mError;
    }
}
