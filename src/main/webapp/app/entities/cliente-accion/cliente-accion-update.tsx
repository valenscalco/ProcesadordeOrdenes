import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClienteAccion } from 'app/shared/model/cliente-accion.model';
import { getEntity, updateEntity, createEntity, reset } from './cliente-accion.reducer';

export const ClienteAccionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clienteAccionEntity = useAppSelector(state => state.clienteAccion.entity);
  const loading = useAppSelector(state => state.clienteAccion.loading);
  const updating = useAppSelector(state => state.clienteAccion.updating);
  const updateSuccess = useAppSelector(state => state.clienteAccion.updateSuccess);

  const handleClose = () => {
    navigate('/cliente-accion');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.cliente !== undefined && typeof values.cliente !== 'number') {
      values.cliente = Number(values.cliente);
    }
    if (values.accionId !== undefined && typeof values.accionId !== 'number') {
      values.accionId = Number(values.accionId);
    }
    if (values.cantidadActual !== undefined && typeof values.cantidadActual !== 'number') {
      values.cantidadActual = Number(values.cantidadActual);
    }

    const entity = {
      ...clienteAccionEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...clienteAccionEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="procesadorOrdenesApp.clienteAccion.home.createOrEditLabel" data-cy="ClienteAccionCreateUpdateHeading">
            <Translate contentKey="procesadorOrdenesApp.clienteAccion.home.createOrEditLabel">Create or edit a ClienteAccion</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="cliente-accion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('procesadorOrdenesApp.clienteAccion.cliente')}
                id="cliente-accion-cliente"
                name="cliente"
                data-cy="cliente"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.clienteAccion.accionId')}
                id="cliente-accion-accionId"
                name="accionId"
                data-cy="accionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.clienteAccion.accion')}
                id="cliente-accion-accion"
                name="accion"
                data-cy="accion"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.clienteAccion.cantidadActual')}
                id="cliente-accion-cantidadActual"
                name="cantidadActual"
                data-cy="cantidadActual"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.clienteAccion.observaciones')}
                id="cliente-accion-observaciones"
                name="observaciones"
                data-cy="observaciones"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cliente-accion" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ClienteAccionUpdate;
