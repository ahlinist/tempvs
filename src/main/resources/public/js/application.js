import {library} from './library.js';
import {stash} from './stash.js';
import {messaging} from './messaging.js';
import {profile} from './profile.js';
import {user} from './user.js';
import {header} from './header.js';

window.onload = function() {
  header.init();
  library.init();
  stash.init();
  messaging.init();
  profile.init();
  user.init();
};
