package ar.edu.um.prog2.scalco.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.prog2.scalco.domain.ClienteAccion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteAccionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer cliente;

    @NotNull
    private Integer accionId;

    private String accion;

    private Integer cantidadActual;

    private String observaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getAccionId() {
        return accionId;
    }

    public void setAccionId(Integer accionId) {
        this.accionId = accionId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Integer getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteAccionDTO)) {
            return false;
        }

        ClienteAccionDTO clienteAccionDTO = (ClienteAccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteAccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteAccionDTO{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            ", accionId=" + getAccionId() +
            ", accion='" + getAccion() + "'" +
            ", cantidadActual=" + getCantidadActual() +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
