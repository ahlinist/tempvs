package com.tempvs.tests.functional.pages

import geb.Page

class VerifyPage extends Page {
    static url = '/verify/byEmail'
    static at = { $("meta", name: "location").@content == '/verify/byEmail'}
}