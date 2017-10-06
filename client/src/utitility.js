const usernameRegex = /^[a-zA-Z0-9 ]{5,50}$/;
const fileExtensionRegex = /(\.jpg|\.jpeg|\.png)$/;
const passwordRegex = /^[a-zA-Z0-9 :;\/\-+*_\.]{8,50}$/;
const queryRegex = /^[a-zA-Z0-9 ]{3,100}$/;
const specificPlantNameRegex = /^[a-z]+\-[a-z]+$/;

export {
    usernameRegex,
    fileExtensionRegex,
    passwordRegex,
    queryRegex,
    specificPlantNameRegex
};
