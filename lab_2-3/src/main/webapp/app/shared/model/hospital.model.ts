import { IOncologist } from 'app/shared/model/oncologist.model';
import { ITherapist } from 'app/shared/model/therapist.model';

export interface IHospital {
  id?: number;
  name?: string;
  phone?: string;
  address?: string;
  paidFor?: boolean;
  oncologists?: IOncologist[];
  therapists?: ITherapist[];
}

export class Hospital implements IHospital {
  constructor(
    public id?: number,
    public name?: string,
    public phone?: string,
    public address?: string,
    public paidFor?: boolean,
    public oncologists?: IOncologist[],
    public therapists?: ITherapist[]
  ) {
    this.paidFor = this.paidFor || false;
  }
}
