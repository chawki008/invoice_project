import { IFacture } from 'app/shared/model/facture.model';

export interface ICorrection {
  id?: number;
  champ?: string;
  sasisseurLogin?: string;
  sasisseurId?: number;
  verificateurLogin?: string;
  verificateurId?: number;
  factures?: IFacture[];
}

export const defaultValue: Readonly<ICorrection> = {};
