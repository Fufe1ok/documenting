import { Moment } from 'moment';
import { IOncologist } from 'app/shared/model/oncologist.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IConclusion {
  id?: number;
  date?: Moment;
  resultDescription?: string;
  url?: string;
  signedBy?: IOncologist;
  forPatient?: IPatient;
}

export class Conclusion implements IConclusion {
  constructor(
    public id?: number,
    public date?: Moment,
    public resultDescription?: string,
    public url?: string,
    public signedBy?: IOncologist,
    public forPatient?: IPatient
  ) {}
}
