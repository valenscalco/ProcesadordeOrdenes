import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClienteAccion from './cliente-accion';
import ClienteAccionDetail from './cliente-accion-detail';
import ClienteAccionUpdate from './cliente-accion-update';
import ClienteAccionDeleteDialog from './cliente-accion-delete-dialog';

const ClienteAccionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ClienteAccion />} />
    <Route path="new" element={<ClienteAccionUpdate />} />
    <Route path=":id">
      <Route index element={<ClienteAccionDetail />} />
      <Route path="edit" element={<ClienteAccionUpdate />} />
      <Route path="delete" element={<ClienteAccionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClienteAccionRoutes;
