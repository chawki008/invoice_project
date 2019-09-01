import { Moment } from 'moment';
import { ICorrection } from 'app/shared/model/correction.model';

export interface IFacture {
  id?: number;
  etat?: string;
  type?: string;
  createdAt?: Moment;
  lastModifiedAt?: Moment;
  date?: Moment;
  info?: string;
  numero?: string;
  montantTTC?: number;
  fournisseur?: string;
  ecoTax?: number;
  imageContentType?: string;
  image?: any;
  sasisseurId?: number;
  verificateurId?: number;
  corrections?: ICorrection[];
}

export const defaultValue: Readonly<IFacture> = {};
