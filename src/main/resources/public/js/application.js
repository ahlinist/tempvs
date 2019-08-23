import {library} from './library.js';
import {stash} from './stash.js';
import {messaging} from './messaging.js';
import {profile} from './profile.js';

window.onload = function() {
  library.init();
  stash.init();
  messaging.init();
  profile.init();
};
