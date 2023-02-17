import Route from '@ember/routing/route';

export default class PatientRoute extends Route {
  async model() {
    let obj = await fetch('patientdata?')
      .then((response) => response.json())
      .then((data) => {
        return data;
      });
    console.log(obj);
    return obj;
  }
}
