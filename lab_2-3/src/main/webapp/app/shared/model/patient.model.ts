import { IConclusion } from 'app/shared/model/conclusion.model';
import { ITestResult } from 'app/shared/model/test-result.model';
import { ITherapist } from 'app/shared/model/therapist.model';
import { IOncologist } from 'app/shared/model/oncologist.model';

export interface IPatient {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  age?: number;
  weight?: number;
  height?: number;
  phone?: string;
  address?: string;
  conclusions?: IConclusion[];
  tests?: ITestResult[];
  therapists?: ITherapist[];
  oncologists?: IOncologist[];
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public age?: number,
    public weight?: number,
    public height?: number,
    public phone?: string,
    public address?: string,
    public conclusions?: IConclusion[],
    public tests?: ITestResult[],
    public therapists?: ITherapist[],
    public oncologists?: IOncologist[]
  ) {}
}
