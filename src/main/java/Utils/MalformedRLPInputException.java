package Utils;

import Utils.bytes.uint.RLPException;

public class MalformedRLPInputException extends RLPException {
    public MalformedRLPInputException(final String message) {
        super(message);
    }
}