import Route from '@ember/routing/route';

export default class AddAdminRoute extends Route {
  model() {
    let arr = document.cookie.split('; ');
    arr.forEach((x) => {
      if (x.match('type')) {
        let temp = x.split('=')[1];
        if (temp != 'admin') {
          window.location.href = temp;
        }
      }
    });
  }
}
