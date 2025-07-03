import lombok.Data;

@Data
public class ProgresoDTO {
    private Double valorActual;
    private String unidad;
    private String comentario;
    private Long usuarioId;
    private Long desafioId;
}