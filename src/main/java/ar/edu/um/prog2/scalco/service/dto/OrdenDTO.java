package ar.edu.um.prog2.scalco.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.prog2.scalco.domain.Orden} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenDTO implements Serializable {

    private Long id;

    private Integer cliente;

    @NotNull
    private Integer accionId;

    @NotNull
    private String accion;

    @NotNull
    private String operacion;

    private Float precio;

    @NotNull
    private Integer cantidad;

    @NotNull
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime fechaOperacion;

    private String modo;

    private Boolean operacionExitosa;

    private String operacionObservaciones;

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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ZonedDateTime getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(ZonedDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public Boolean getOperacionExitosa() {
        return operacionExitosa;
    }

    public void setOperacionExitosa(Boolean operacionExitosa) {
        this.operacionExitosa = operacionExitosa;
    }

    public String getOperacionObservaciones() {
        return operacionObservaciones;
    }

    public void setOperacionObservaciones(String operacionObservaciones) {
        this.operacionObservaciones = operacionObservaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenDTO)) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenDTO{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            ", accionId=" + getAccionId() +
            ", accion='" + getAccion() + "'" +
            ", operacion='" + getOperacion() + "'" +
            ", precio=" + getPrecio() +
            ", cantidad=" + getCantidad() +
            ", fechaOperacion='" + getFechaOperacion() + "'" +
            ", modo='" + getModo() + "'" +
            ", operacionExitosa='" + getOperacionExitosa() + "'" +
            ", operacionObservaciones='" + getOperacionObservaciones() + "'" +
            "}";
    }

    public String toJSONString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // Convertir la fecha a ZonedDateTime con zona horaria UTC
        ZonedDateTime fechaConvertida = ZonedDateTime.of(getFechaOperacion().toLocalDateTime(), ZoneId.of("Z"));
        // Formatear la fecha en el nuevo formato
        String fechaEnNuevoFormato = fechaConvertida.format(formatter);

        String str =
            "{" +
            "\"cliente\":" +
            getCliente() +
            ", \"accionId\":" +
            getAccionId() +
            ", \"accion\":\"" +
            getAccion() +
            "\"" +
            ", \"operacion\":\"" +
            getOperacion() +
            "\"" +
            ", \"precio\":" +
            getPrecio() +
            ", \"cantidad\":" +
            getCantidad() +
            ", \"fechaOperacion\":\"" +
            fechaEnNuevoFormato +
            "\"" +
            ", \"modo\":\"" +
            getModo() +
            "\"" +
            ", \"operacionExitosa\":" +
            getOperacionExitosa() +
            ", \"operacionObservaciones\":\"" +
            getOperacionObservaciones() +
            "\"" +
            "}";
        return str;
    }
}
