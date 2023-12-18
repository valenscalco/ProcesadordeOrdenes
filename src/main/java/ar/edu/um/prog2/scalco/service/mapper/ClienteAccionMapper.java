package ar.edu.um.prog2.scalco.service.mapper;

import ar.edu.um.prog2.scalco.domain.ClienteAccion;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClienteAccion} and its DTO {@link ClienteAccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteAccionMapper extends EntityMapper<ClienteAccionDTO, ClienteAccion> {}
