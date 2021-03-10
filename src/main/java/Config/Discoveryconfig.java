package Config;

import Utils.bytes.BytesValue;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import peer.DiscoveryPeer;
import peer.Endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class Discoveryconfig {

    static List<DiscoveryPeer> bootnode = new ArrayList<>();

    public static List<DiscoveryPeer> getBootnode() throws DecoderException {

        //逻辑是先连接config里面节点再进行发现
        String id  = "08021221027611680ca65e8fb7214a31b6ce6fcd8e6fe6a5f4d784dc6601dfe2bb9f8c96c2";
        byte [] peerid= Hex.decodeHex(id);
        OptionalInt tcpport = OptionalInt.of(30000);
        Endpoint endpoint = new Endpoint("127.0.0.1",10000,tcpport);
        BytesValue bytesValue= BytesValue.wrap(peerid);
        DiscoveryPeer peer = new DiscoveryPeer(bytesValue,endpoint);
        bootnode.add(peer);

        return bootnode;
    }


}
