import Route from '@ember/routing/route';

export default class ManageAccountRoute extends Route {
  async model() {
    let obj = await fetch('account')
      .then((response) => response.json())
      .then((data) => {
        return data;
      });
    console.log(obj);
    return obj;
  }
}
