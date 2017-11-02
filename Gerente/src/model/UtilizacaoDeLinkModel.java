package model;

import services.SNMPGet;
import services.SnmpT2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Lucas on 28/05/2017.
 */
public class UtilizacaoDeLinkModel {

    private static LinkedList<UtilizacaoDeLink> queue;
    private static String IF_OUT_OCTETS = ".1.3.6.1.2.1.2.2.1.16";
    private static String IF_IN_OCTETS = ".1.3.6.1.2.1.2.2.1.10";
    private SnmpT2 snmpT2;
    private int ifSpeed;

    public UtilizacaoDeLinkModel(int ifSpeed, SnmpT2 snmpT2) throws IOException {
        queue = new LinkedList<>();
        this.ifSpeed = ifSpeed;
        this.snmpT2 = snmpT2;
        queue.push(new UtilizacaoDeLink(ifSpeed, 0, 0, 0, null));
    }

    public int getUtilizacaoDeLink(int position) throws IOException {

        int ifInOctets = SNMPGet.getValue(snmpT2.getCommunityTarget(), snmpT2.getSnmp(), IF_IN_OCTETS);
        int ifOutOctets = SNMPGet.getValue(snmpT2.getCommunityTarget(), snmpT2.getSnmp(), IF_OUT_OCTETS);

        UtilizacaoDeLink previous = queue.pop();
        UtilizacaoDeLink current = new UtilizacaoDeLink(ifInOctets, ifOutOctets, position, ifSpeed, previous);
        queue.push(current);
        return current.getUtilizacaoDeLink();
    }
}
