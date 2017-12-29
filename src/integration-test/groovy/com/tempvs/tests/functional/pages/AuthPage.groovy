package com.tempvs.tests.functional.pages

import geb.Page

class AuthPage extends Page {
    static url = '/auth/index'
    static at = { $("meta", name: "location").@content == '/auth/index' }
}