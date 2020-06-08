import { IConclusion } from 'app/shared/model/conclusion.model';
import { ITestResult } from 'app/shared/model/test-result.model';
import { IHospital } from 'app/shared/model/hospital.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IOncologist {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phone?: string;
  roomIn?: number;
  conclusions?: IConclusion[];
  tests?: ITestResult[];
  hospital?: IHospital;
  patients?: IPatient[];
}

export class Oncologist implements IOncologist {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public phone?: string,
    public roomIn?: number,
    public conclusions?: IConclusion[],
    public tests?: ITestResult[],
    public hospital?: IHospital,
    public patients?: IPatient[]
  ) {}
}
