package ms_plazoleta.ms_plazoleta.domain.model;

public class Restaurante {

    private Long id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Long propietarioId;

    public Restaurante(Long id, String nombre, String nit, String direccion,
                       String telefono, String urlLogo, Long propietarioId) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.urlLogo = urlLogo;
        this.propietarioId = propietarioId;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getNit() { return nit; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getUrlLogo() { return urlLogo; }
    public Long getPropietarioId() { return propietarioId; }
}
