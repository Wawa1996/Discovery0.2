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
        String id = "0947751e3022ecf3016be03ec77ab0ce3c2662b4843898cb068d74f698ccc8ad75aa17564ae80a20bb044ee7a6d903e8e8df624b089c95d66a0570f051e5a05b";
        byte [] peerid= Hex.decodeHex(id);
        OptionalInt tcpport = OptionalInt.of(30000);
        Endpoint endpoint = new Endpoint("127.0.0.1",10000,tcpport);
        BytesValue bytesValue= BytesValue.wrap(peerid);
        DiscoveryPeer peer = new DiscoveryPeer(bytesValue,endpoint);
        bootnode.add(peer);

        return bootnode;
    }


}
