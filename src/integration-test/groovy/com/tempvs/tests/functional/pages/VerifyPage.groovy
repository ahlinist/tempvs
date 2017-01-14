package com.tempvs.tests.functional.pages

import geb.Page

class VerifyPage extends Page {
    static url = '/user/verify'
    static at = { $("meta", name: "location").@content == '/user/verify' }
}