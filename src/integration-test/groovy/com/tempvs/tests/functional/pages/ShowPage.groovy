package com.tempvs.tests.functional.pages

import geb.Page

class ShowPage extends Page {
    static url = '/user/show'
    static at = { $("meta", name: "location").@content == '/user/show' }
}