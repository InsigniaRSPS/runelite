package net.runelite.mixins.rsps;

import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSClient;

import java.math.BigInteger;

@Mixin(RSClient.class)
public abstract class PortChangeMixin implements RSClient {

    @Copy("doCycleLoggedOut")
    public void rs$doCycleLoggedOut() {
        throw new RuntimeException();
    }

    @Replace("doCycleLoggedOut")
    public void rl$doCycleLoggedOut() {
        setPort(54900);
        rs$doCycleLoggedOut();
    }

   @Copy("doCycleJs5Connect")
    public void rsdoCycleJs5Connect() {
        throw new RuntimeException();
    }

    @Replace("doCycleJs5Connect")
    public void rl$doCycleJs5Connect() {
        int origPort = getPort();

        try {
            setPort(54901);
            rsdoCycleJs5Connect();
        } finally {
            setPort(origPort);
        }
    }


}
