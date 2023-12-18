export interface IClienteAccion {
  id?: number;
  cliente?: number;
  accionId?: number;
  accion?: string | null;
  cantidadActual?: number | null;
  observaciones?: string | null;
}

export const defaultValue: Readonly<IClienteAccion> = {};
