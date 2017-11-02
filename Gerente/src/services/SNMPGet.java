package services;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

/**
 * Created by Lucas on 27/05/2017.
 */
public class SNMPGet {

    public static int getValue(CommunityTarget communityTarget, Snmp snmp, String oid) throws IOException {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));
        ResponseEvent response = snmp.get(pdu, communityTarget);
        if (response != null) return response.getResponse().getVariableBindings().get(0).getVariable().toInt();
        snmp.close();
        return 0;
    }
}
