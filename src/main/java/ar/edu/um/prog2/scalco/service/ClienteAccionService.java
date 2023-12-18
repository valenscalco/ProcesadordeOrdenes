package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.prog2.scalco.domain.ClienteAccion}.
 */
public interface ClienteAccionService {
    /**
     * Save a clienteAccion.
     *
     * @param clienteAccionDTO the entity to save.
     * @return the persisted entity.
     */
    ClienteAccionDTO save(ClienteAccionDTO clienteAccionDTO);

    /**
     * Updates a clienteAccion.
     *
     * @param clienteAccionDTO the entity to update.
     * @return the persisted entity.
     */
    ClienteAccionDTO update(ClienteAccionDTO clienteAccionDTO);

    /**
     * Partially updates a clienteAccion.
     *
     * @param clienteAccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClienteAccionDTO> partialUpdate(ClienteAccionDTO clienteAccionDTO);

    /**
     * Get all the clienteAccions.
     *
     * @return the list of entities.
     */
    List<ClienteAccionDTO> findAll();

    /**
     * Get the "id" clienteAccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClienteAccionDTO> findOne(Long id);

    /**
     * Delete the "id" clienteAccion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
