import dayjs from 'dayjs';

export interface IOrden {
  id?: number;
  cliente?: number | null;
  accionId?: number;
  accion?: string;
  operacion?: string;
  precio?: number | null;
  cantidad?: number;
  fechaOperacion?: dayjs.Dayjs;
  modo?: string | null;
}

export const defaultValue: Readonly<IOrden> = {};
