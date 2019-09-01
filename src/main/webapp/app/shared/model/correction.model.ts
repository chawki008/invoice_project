import { IFacture } from 'app/shared/model/facture.model';

export interface ICorrection {
  id?: number;
  champ?: string;
  sasisseurId?: number;
  verificateurId?: number;
  factures?: IFacture[];
}

export const defaultValue: Readonly<ICorrection> = {};
