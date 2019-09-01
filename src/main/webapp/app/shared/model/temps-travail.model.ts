import { Moment } from 'moment';

export interface ITempsTravail {
  id?: number;
  startDate?: Moment;
  endDate?: Moment;
  userId?: number;
}

export const defaultValue: Readonly<ITempsTravail> = {};
