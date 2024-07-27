package pe.gob.sucamec.rma1369.data;

/**
 *
 * @author rfernandezv
 */
public class SitArma {
    private Long sitArma;
    private String desSit;

    public SitArma() {
    }
    
    public SitArma(Long sitArma, String desSit) {
        this.sitArma = sitArma;
        this.desSit = desSit;
    }    
    
    public Long getSitArma() {
        return sitArma;
    }

    public void setSitArma(Long sitArma) {
        this.sitArma = sitArma;
    }

    public String getDesSit() {
        return desSit;
    }

    public void setDesSit(String desSit) {
        this.desSit = desSit;
    }
    
    
}
