package Utils;


import Utils.bytes.BytesValue;
import Utils.bytes.BytesValueRLPOutput;
import Utils.bytes.MutableBytesValue;
import Utils.bytes.RLP;
import io.libp2p.core.crypto.PrivKey;
import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.Optional;
import static cryto.Hash.keccak256;
@Slf4j
public class Packet {
    // index          len
    // 0     hash     len = 32
    // 32  LenthIndex len = 1
    // 33   packtype  len = 1
    // 34   pubkey    len = 37
    // 71   Signatrue len = Len
    // 71+Len  data   len = size

    private static final int LENGTH_INDEX = 32;
    private static final int TYPE_INDEX = 33;
    private static final int PUBKEY_INDEX = 71;
    private static byte Signature_Len;
    private final PacketType type;
    private final PacketData data;
    private final BytesValue hash;
    private BytesValue signature;
    private BytesValue publicKey;

    private Packet(final PacketType type, final PacketData data ,PrivKey pri) {

        this.type = type;
        this.data = data;
        final BytesValue typeBytes = BytesValue.of(this.type.getValue());
        final BytesValue dataBytes = RLP.encode(this.data::writeTo);
        this.signature = BytesValue.wrap(pri.sign(BytesValue.wrap(typeBytes, dataBytes).extractArray()));
        Signature_Len = (byte) (signature.size());
        this.publicKey = BytesValue.wrap(pri.publicKey().bytes());
        this.hash = keccak256(BytesValue.wrap(BytesValue.wrap(BytesValue.wrap(
                BytesValue.wrap(BytesValue.of(Signature_Len),typeBytes),publicKey),signature),dataBytes));
    }

    private Packet(
            final PacketType packetType, final PacketData packetData, final BytesValue message) {
        this.type = packetType;
        System.out.println("decode type = "+ type);
        this.data = packetData;
        System.out.println("packetData  = "+ packetData.toString());
        this.hash  = message.slice(0, 32);
        System.out.println("hash  = "+ hash.toString());
        Signature_Len = message.get(LENGTH_INDEX);
        this.publicKey = message.slice(TYPE_INDEX + 1, 37);
        System.out.println("publicKey = "+ publicKey);
        this.signature= message.slice(PUBKEY_INDEX , Signature_Len);
        System.out.println("signature = "+signature.toString());
        final BytesValue rest = message.slice(LENGTH_INDEX, message.size() - LENGTH_INDEX);
        if (!Arrays.equals(keccak256(rest).extractArray(), hash.extractArray())) {
            throw new PeerDiscoveryPacketDecodingException(
                    "Integrity check failed: non-matching hashes.");
        }
    }


    public static Packet create(
            final PacketType packetType, final PacketData packetData ,PrivKey privKey) {
        return new Packet(packetType, packetData, privKey);
    }

    public static Packet decode(final Buffer message) {
        final byte type = message.getByte(TYPE_INDEX);
        Signature_Len = message.getByte(LENGTH_INDEX);
        final PacketType packetType =
                PacketType.forByte(type)
                        .orElseThrow(
                                () ->
                                        new PeerDiscoveryPacketDecodingException("Unrecognized packet type: " + type));

        final PacketType.Deserializer<?> deserializer = packetType.getDeserializer();
        final PacketData packetData;
        try {
            packetData = deserializer.deserialize(RLP.input(message, Signature_Len + 71));
        } catch (final Exception e) {
            throw new PeerDiscoveryPacketDecodingException("Malformed packet of type: " + packetType, e);
        }

        return new Packet(packetType, packetData, BytesValue.wrapBuffer(message));
    }

    public Buffer encode() {
        final BytesValue signature = this.signature;
        final BytesValueRLPOutput encodedData = new BytesValueRLPOutput();
        data.writeTo(encodedData);
        final Buffer buffer =
                Buffer.buffer(hash.size() + signature.size() + 2 + encodedData.encodedSize());
        hash.appendTo(buffer);
        Signature_Len = (byte) (signature.size());
        buffer.appendByte(Signature_Len);
        buffer.appendByte(type.getValue());
        buffer.appendBytes(publicKey.extractArray());
        signature.appendTo(buffer);
        appendEncoded(encodedData, buffer);
        return buffer;
    }

    protected void appendEncoded(final BytesValueRLPOutput encoded, final Buffer buffer) {
        final int size = encoded.encodedSize();
        if (size == 0) {
            return;
        }
        // We want to append to the buffer, and Buffer always grows to accommodate anything writing,
        // so we write the last byte we know we'll need to make it resize accordingly.
        final int start = buffer.length();
        buffer.setByte(start + size - 1, (byte) 0);
        encoded.writeEncoded(MutableBytesValue.wrapBuffer(buffer, start, size));
    }

    @SuppressWarnings("unchecked")
    public <T extends PacketData> Optional<T> getPacketData(final Class<T> expectedPacketType) {
        if (data == null || !data.getClass().equals(expectedPacketType)) {
            return Optional.empty();
        }
        return Optional.of((T) data);
    }

    public BytesValue getNodeId() {
        return publicKey;
    }

    public PacketType getType() {
        return type;
    }

    public BytesValue getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Packet{"
                + "type="
                + type
                + ", data="
                + data
                + ", hash="
                + hash
                + ", signature="
                + signature
                + ", publicKey="
                + publicKey
                + '}';
    }

}
