import Config.Discoveryconfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import peer.DiscoveryPeer;

import java.util.List;
import java.util.Optional;

@Slf4j
public class Main {

    public static void main(String[] args) throws DecoderException {
        boolean isboot = false;
        if (args.length!=0){
//            int port = Integer.getInteger(args[0]);
            log.info("种子节点启动");
            isboot = true;
        }
        DiscoveryController discoveryController = new DiscoveryController();
        discoveryController.start(isboot);
    }
}
