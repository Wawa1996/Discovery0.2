//import Message.DisMessage;
//import Message.MessageFa;
//import io.vertx.core.Handler;
//import io.vertx.core.Vertx;
//import org.apache.commons.codec.DecoderException;
//import org.junit.After;
//import org.junit.jupiter.api.Test;
//import peer.DiscoveryPeer;
//import peer.Endpoint;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class DiscoveryControllerTest {
//    io.vertx.core.datagram.DatagramSocket socket;
//    Vertx vertx = Vertx.vertx();
//    DiscoveryControllerTest() throws UnknownHostException {
//    }
//
//    @After
//    public void closeVertx() {
//        vertx.close();
//    }
//
//// 直接运行DiscoveryController报错
//
//    @Test
//    void pingmessage() throws DecoderException, IOException {
//        InetAddress address = InetAddress.getLocalHost();
//        int port = 10000;
//        vertx.createDatagramSocket().listen(port,address.toString(),ar->{
//            socket = ar.result();
////            DiscoveryPeer discoveryController = new DiscoveryController();
//            socket.handler(handlePacket());
//        });
//
//        DiscoveryPeer peer= new DiscoveryPeer(address.toString(),port);
//        Endpoint to = new Endpoint(address.toString(),port);
//        DisMessage message = MessageFa.create((byte) 1,null,to);
//        byte[] sendData = message.getEncoded();
//        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
//        DatagramSocket istemciSocket = new DatagramSocket();
//        istemciSocket.setSoTimeout(10000);
//        byte[] receiveMessage = new byte[65];
//        DatagramPacket gelenPaket = new DatagramPacket(receiveMessage, receiveMessage.length);
//        istemciSocket.send(sendPacket);
//        istemciSocket.receive(gelenPaket);
//        assertEquals((byte) 1, gelenPaket.getData()[0]);
//    }
//
//    @Test
//    Handler<io.vertx.core.datagram.DatagramPacket> handlePacket() {
//        return null;
//    }
//
//    @Test
//    void onMessage() {
//    }
//
//    @Test
//    void refreshTableIfRequired() {
//    }
//
//    @Test
//    void bond() {
//    }
//
//    @Test
//    void refreshTable() {
//    }
//
//    @Test
//    void addToPeerTable() {
//    }
//}