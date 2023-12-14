package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.prog2.scalco.domain.Orden}.
 */
public interface OrdenService {
    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    OrdenDTO save(OrdenDTO ordenDTO);

    /**
     * Updates a orden.
     *
     * @param ordenDTO the entity to update.
     * @return the persisted entity.
     */
    OrdenDTO update(OrdenDTO ordenDTO);

    /**
     * Partially updates a orden.
     *
     * @param ordenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO);

    /**
     * Get all the ordens.
     *
     * @return the list of entities.
     */
    List<OrdenDTO> findAll();

    /**
     * Get the "id" orden.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdenDTO> findOne(Long id);

    /**
     * Delete the "id" orden.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
