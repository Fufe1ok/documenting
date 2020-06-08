import { Moment } from 'moment';
import { IOncologist } from 'app/shared/model/oncologist.model';
import { IPatient } from 'app/shared/model/patient.model';
import { TestType } from 'app/shared/model/enumerations/test-type.model';

export interface ITestResult {
  id?: number;
  name?: string;
  type?: TestType;
  dateTime?: Moment;
  description?: string;
  url?: string;
  oncologist?: IOncologist;
  patient?: IPatient;
}

export class TestResult implements ITestResult {
  constructor(
    public id?: number,
    public name?: string,
    public type?: TestType,
    public dateTime?: Moment,
    public description?: string,
    public url?: string,
    public oncologist?: IOncologist,
    public patient?: IPatient
  ) {}
}
