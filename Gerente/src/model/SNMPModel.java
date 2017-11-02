package model;

import services.SNMPGet;
import services.SnmpT2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Lucas on 27/05/2017.
 */
public class SNMPModel {
    private LinkedList<Integer> queue;    
    private String oid;

    public SNMPModel(String oid) {
        queue = new LinkedList<>();        
        queue.push(0);
        this.oid = oid;
    }

    public int getData(SnmpT2 snmpT2) throws IOException {
        int current = SNMPGet.getValue(snmpT2.getCommunityTarget(), snmpT2.getSnmp(), oid);
        int previous = queue.pop();
        queue.push(current);
        return current - previous;
    }
}
