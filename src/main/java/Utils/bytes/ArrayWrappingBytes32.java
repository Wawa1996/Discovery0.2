package Utils.bytes;

import java.security.MessageDigest;

import static com.google.common.base.Preconditions.checkArgument;

/** An implementation of {@link Bytes32} backed by a byte array ({@code byte[]}). */
class ArrayWrappingBytes32 extends ArrayWrappingBytesValue implements Bytes32 {

    ArrayWrappingBytes32(final byte[] bytes) {
        this(checkLength(bytes), 0);
    }

    ArrayWrappingBytes32(final byte[] bytes, final int offset) {
        super(checkLength(bytes, offset), offset, SIZE);
    }

    // Ensures a proper error message.
    private static byte[] checkLength(final byte[] bytes) {
        checkArgument(bytes.length == SIZE, "Expected %s bytes but got %s", SIZE, bytes.length);
        return bytes;
    }

    // Ensures a proper error message.
    private static byte[] checkLength(final byte[] bytes, final int offset) {
        checkArgument(
                bytes.length - offset >= SIZE,
                "Expected at least %s bytes from offset %s but got only %s",
                SIZE,
                offset,
                bytes.length - offset);
        return bytes;
    }

    @Override
    public Bytes32 copy() {
        // Because MutableArrayWrappingBytesValue overrides this, we know we are immutable. We may
        // retain more than necessary however.
        if (offset == 0 && length == bytes.length) return this;

        return new ArrayWrappingBytes32(arrayCopy());
    }

    @Override
    public MutableBytes32 mutableCopy() {
        return new MutableArrayWrappingBytes32(arrayCopy());
    }
}

