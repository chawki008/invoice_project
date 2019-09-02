import { Moment } from 'moment';

export interface ITempsTravail {
  id?: number;
  startDate?: Moment;
  endDate?: Moment;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<ITempsTravail> = {};
