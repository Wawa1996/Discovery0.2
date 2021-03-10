import Config.Discoveryconfig;
import Utils.bytes.BytesValue;
import io.libp2p.core.crypto.KeyKt;
import io.libp2p.core.crypto.PrivKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.tuweni.bytes.Bytes;
import org.spongycastle.util.encoders.Hex;
import peer.DiscoveryPeer;

import java.util.List;
import java.util.Optional;

@Slf4j
public class Main {

    public static void main(String[] args) throws DecoderException {
        boolean isboot = false;
        if (args.length!=0){
            log.info("种子节点启动");
            isboot = true;
        }
        DiscoveryController discoveryController = new DiscoveryController();
        discoveryController.start(isboot);

    }
}
