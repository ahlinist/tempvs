package com.tempvs.tests.functional.pages

import geb.Page

class LoginPage extends Page {
    static url = '/auth/login'
    static at = { $("meta", name: "location").@content == '/auth/login' }
}