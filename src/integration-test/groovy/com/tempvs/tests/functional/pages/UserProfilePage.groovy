package com.tempvs.tests.functional.pages

import geb.Page

class UserProfilePage extends Page {
    static url = '/profile/userProfile'
    static at = { $("meta", name: "location").@content == '/profile/userProfile' }
}