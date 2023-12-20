import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrden } from 'app/shared/model/orden.model';
import { getEntity, updateEntity, createEntity, reset } from './orden.reducer';

export const OrdenUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ordenEntity = useAppSelector(state => state.orden.entity);
  const loading = useAppSelector(state => state.orden.loading);
  const updating = useAppSelector(state => state.orden.updating);
  const updateSuccess = useAppSelector(state => state.orden.updateSuccess);

  const handleClose = () => {
    navigate('/orden');
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
    if (values.precio !== undefined && typeof values.precio !== 'number') {
      values.precio = Number(values.precio);
    }
    if (values.cantidad !== undefined && typeof values.cantidad !== 'number') {
      values.cantidad = Number(values.cantidad);
    }
    values.fechaOperacion = convertDateTimeToServer(values.fechaOperacion);

    const entity = {
      ...ordenEntity,
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
      ? {
          fechaOperacion: displayDefaultDateTime(),
        }
      : {
          ...ordenEntity,
          fechaOperacion: convertDateTimeFromServer(ordenEntity.fechaOperacion),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="procesadorOrdenesApp.orden.home.createOrEditLabel" data-cy="OrdenCreateUpdateHeading">
            <Translate contentKey="procesadorOrdenesApp.orden.home.createOrEditLabel">Create or edit a Orden</Translate>
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
                  id="orden-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.cliente')}
                id="orden-cliente"
                name="cliente"
                data-cy="cliente"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.accionId')}
                id="orden-accionId"
                name="accionId"
                data-cy="accionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.accion')}
                id="orden-accion"
                name="accion"
                data-cy="accion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.operacion')}
                id="orden-operacion"
                name="operacion"
                data-cy="operacion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.precio')}
                id="orden-precio"
                name="precio"
                data-cy="precio"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.cantidad')}
                id="orden-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.fechaOperacion')}
                id="orden-fechaOperacion"
                name="fechaOperacion"
                data-cy="fechaOperacion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('procesadorOrdenesApp.orden.modo')} id="orden-modo" name="modo" data-cy="modo" type="text" />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.operacionExitosa')}
                id="orden-operacionExitosa"
                name="operacionExitosa"
                data-cy="operacionExitosa"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('procesadorOrdenesApp.orden.operacionObservaciones')}
                id="orden-operacionObservaciones"
                name="operacionObservaciones"
                data-cy="operacionObservaciones"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/orden" replace color="info">
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

export default OrdenUpdate;
