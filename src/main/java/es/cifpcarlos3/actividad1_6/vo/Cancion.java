package es.cifpcarlos3.actividad1_6.vo;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value="cancion")
public class Cancion implements Serializable {
    private int anio;
    private String titulo;
    private String autor;
    private String duracion;
    private boolean esEspanola;

}
