import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cliente-accion.reducer';

export const ClienteAccionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clienteAccionEntity = useAppSelector(state => state.clienteAccion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clienteAccionDetailsHeading">
          <Translate contentKey="procesadorOrdenesApp.clienteAccion.detail.title">ClienteAccion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.id}</dd>
          <dt>
            <span id="cliente">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.cliente">Cliente</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.cliente}</dd>
          <dt>
            <span id="accionId">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.accionId">Accion Id</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.accionId}</dd>
          <dt>
            <span id="accion">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.accion">Accion</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.accion}</dd>
          <dt>
            <span id="cantidadActual">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.cantidadActual">Cantidad Actual</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.cantidadActual}</dd>
          <dt>
            <span id="observaciones">
              <Translate contentKey="procesadorOrdenesApp.clienteAccion.observaciones">Observaciones</Translate>
            </span>
          </dt>
          <dd>{clienteAccionEntity.observaciones}</dd>
        </dl>
        <Button tag={Link} to="/cliente-accion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cliente-accion/${clienteAccionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClienteAccionDetail;
