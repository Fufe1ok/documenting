import { IHospital } from 'app/shared/model/hospital.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface ITherapist {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phone?: string;
  roomIn?: number;
  hospital?: IHospital;
  patients?: IPatient[];
}

export class Therapist implements ITherapist {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public phone?: string,
    public roomIn?: number,
    public hospital?: IHospital,
    public patients?: IPatient[]
  ) {}
}
