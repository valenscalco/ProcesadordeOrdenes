package ar.edu.um.prog2.scalco.repository;

import ar.edu.um.prog2.scalco.domain.ClienteAccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClienteAccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteAccionRepository extends JpaRepository<ClienteAccion, Long> {}
