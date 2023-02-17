import Route from '@ember/routing/route';

export default class AdminRoute extends Route {
  async model() {
    let id;
    let arr = document.cookie.split('; ');
    arr.forEach((x) => {
      if (x.match('id')) {
        id = x.split('=')[1];
      }
      if (x.match('type')) {
        let temp = x.split('=')[1];
        if (temp != 'admin') {
          window.location.href = temp;
        }
      }
    });
    let obj = await fetch('admindata')
      .then((response) => response.json())
      .then((data) => {
        return data;
      });
    console.log(obj);
    return obj;
  }
}
