package services;

/**
 * Created by Lucas on 22/05/2017.
 */
import java.io.IOException;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpT2 {


    private static String port = "161";    
    private static int snmpVersion = SnmpConstants.version1;
    private CommunityTarget communityTarget;
    private Snmp snmp;

    public SnmpT2(Snmp snmp, CommunityTarget communityTarget) {
        this.snmp = snmp;
        this.communityTarget = communityTarget;
    }

    public static SnmpT2 snmpFactory(String ipAddress, String community) throws IOException {
        // Create TransportMapping and Listen
        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();

        // Create Target Address object
        CommunityTarget communityT = new CommunityTarget();
        communityT.setCommunity(new OctetString(community));
        communityT.setVersion(snmpVersion);
        communityT.setAddress(new UdpAddress(ipAddress + "/" + port));
        communityT.setRetries(2);
        communityT.setTimeout(1000);
        Snmp s = new Snmp(transport);
        // Create Snmp object for sending data to Agent
        return new SnmpT2(s, communityT);
    }

    public CommunityTarget getCommunityTarget() {
        return communityTarget;
    }

    public Snmp getSnmp() {
        return snmp;
    }
}


