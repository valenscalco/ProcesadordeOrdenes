import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './cliente-accion.reducer';

export const ClienteAccion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const clienteAccionList = useAppSelector(state => state.clienteAccion.entities);
  const loading = useAppSelector(state => state.clienteAccion.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="cliente-accion-heading" data-cy="ClienteAccionHeading">
        <Translate contentKey="procesadorOrdenesApp.clienteAccion.home.title">Cliente Accions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="procesadorOrdenesApp.clienteAccion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cliente-accion/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="procesadorOrdenesApp.clienteAccion.home.createLabel">Create new Cliente Accion</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {clienteAccionList && clienteAccionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('cliente')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.cliente">Cliente</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cliente')} />
                </th>
                <th className="hand" onClick={sort('accionId')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.accionId">Accion Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accionId')} />
                </th>
                <th className="hand" onClick={sort('accion')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.accion">Accion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accion')} />
                </th>
                <th className="hand" onClick={sort('cantidadActual')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.cantidadActual">Cantidad Actual</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cantidadActual')} />
                </th>
                <th className="hand" onClick={sort('observaciones')}>
                  <Translate contentKey="procesadorOrdenesApp.clienteAccion.observaciones">Observaciones</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('observaciones')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {clienteAccionList.map((clienteAccion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cliente-accion/${clienteAccion.id}`} color="link" size="sm">
                      {clienteAccion.id}
                    </Button>
                  </td>
                  <td>{clienteAccion.cliente}</td>
                  <td>{clienteAccion.accionId}</td>
                  <td>{clienteAccion.accion}</td>
                  <td>{clienteAccion.cantidadActual}</td>
                  <td>{clienteAccion.observaciones}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cliente-accion/${clienteAccion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/cliente-accion/${clienteAccion.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/cliente-accion/${clienteAccion.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.home.notFound">No Cliente Accions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ClienteAccion;
