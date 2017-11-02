package model;

/**
 * Created by Lucas on 27/05/2017.
 */
public class UtilizacaoDeLink {

    private int ifInOctets;
    private int ifOutOctets;
    private int position;
    private UtilizacaoDeLink previous;
    private int ifSpeed;
    private int utilizacaoDeLink;

    public UtilizacaoDeLink(
            int ifInOctets,
            int ifOutOctets,
            int position,
            int ifSpeed,
            UtilizacaoDeLink previous
    ) {
        this.ifInOctets = ifInOctets;
        this.ifOutOctets = ifOutOctets;
        this.position = position;
        this.previous = previous;
        this.ifSpeed = ifSpeed;
        if (this.previous instanceof UtilizacaoDeLink)
            calculaUtilizacaoDeLink(ifSpeed);
    }

    public int getIfInOctets() {
        return ifInOctets;
    }

    public int getIfOutOctets() {
        return ifOutOctets;
    }

    public int getPosition() {
        return position;
    }

    public void calculaUtilizacaoDeLink(int ifSpeed) {
        int taxa_de_bytes = (this.getIfInOctets() - previous.getIfInOctets()) +
                (this.getIfOutOctets() - previous.getIfOutOctets());
        int total_de_bytes_por_segundo = taxa_de_bytes / this.getPosition() - previous.getPosition();
        utilizacaoDeLink = (total_de_bytes_por_segundo * 8) / ifSpeed;
        utilizacaoDeLink = utilizacaoDeLink - previous.utilizacaoDeLink;
    }

    public int getUtilizacaoDeLink() {
        return utilizacaoDeLink;
    }
}
