package network;

import java.util.List;

public interface INetworkConnection {
    void connect(String host, int port);
    void send(NetworkPacket packet);
    void disconnect();
    boolean isConnected();
}