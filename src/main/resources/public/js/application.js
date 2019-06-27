import {library} from './library.js';
import {stash} from './stash.js';
import {messaging} from './messaging.js';

window.onload = function() {
  library.init();
  stash.init();
  messaging.init();
};
