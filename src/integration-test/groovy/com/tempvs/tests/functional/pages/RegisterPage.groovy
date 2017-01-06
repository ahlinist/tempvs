package com.tempvs.tests.functional.pages

import geb.Page

class RegisterPage extends Page {
    static url = '/auth/register'
    static at = { $("meta", name: "location").@content == '/auth/register' }

    static content = {
        registerForm {$('form[action="/user/register"]')}
        emailField {$('input[name=email]')}
        passwordField {$('input[name=password]')}
        repeatPasswordField {$('input[name=repeatPassword]')}
        firstNameField {$('input[name=firstName]')}
        lastNameField {$('input[name=lastName]')}
        submitButton {$('.submit-button')}
    }

    def registerUser(email, password, repeatPassword, firstName, lastName) {
        emailField.value(email)
        passwordField.value(password)
        repeatPasswordField.value(repeatPassword)
        firstNameField.value(firstName)
        lastNameField.value(lastName)
        submitButton.click()
    }
}