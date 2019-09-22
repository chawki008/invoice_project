export interface ICorrection {
  id?: number;
  champ?: string;
  oldValue?: string;
  newValue?: string;
  etat?: string;
  sasisseurLogin?: string;
  sasisseurId?: number;
  verificateurLogin?: string;
  verificateurId?: number;
  factureId?: number;
}

export const defaultValue: Readonly<ICorrection> = {};
