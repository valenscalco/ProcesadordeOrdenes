package ar.edu.um.prog2.scalco.service.mapper;

import ar.edu.um.prog2.scalco.domain.Orden;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orden} and its DTO {@link OrdenDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenMapper extends EntityMapper<OrdenDTO, Orden> {}
