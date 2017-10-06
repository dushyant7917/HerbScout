const usernameRegex = /^[a-zA-Z0-9 ]{5,50}$/;
const fileExtensionRegex = /(\.jpg|\.jpeg|\.png)$/;
const passwordRegex = /^[a-zA-Z0-9 :;\/\-+*_\.]{8,50}$/;
const nameRegex = /^[a-zA-Z0-9 ]{0,100}$/;

export {
    usernameRegex,
    fileExtensionRegex,
    passwordRegex,
    nameRegex
};
