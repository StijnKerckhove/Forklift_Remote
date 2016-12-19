package stijnkerckhove.forklift_remote.exceptions;

/**
 * Created by User on 17/12/2016.
 */

public class FailedBluetoothConnectionException extends RuntimeException {
    public FailedBluetoothConnectionException(String message) {
        super(message);
    }
}
