import {library} from './library.js';
import {stash} from './stash.js';

window.onload = function() {
  library.init();
  stash.init();
};
