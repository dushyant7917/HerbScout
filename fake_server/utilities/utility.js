var config = require('./config');
var jwt = require('jsonwebtoken');
var crypto = require('crypto');

function encrypt(dataToEncrypt, key) {
    var cipher = crypto.createCipher('aes192', key);
    var encryptedData = cipher.update(dataToEncrypt, 'utf8', 'hex');
    encryptedData += cipher.final('hex');
    return encryptedData;
}

function decrypt(dataToDecrypt, key) {
    var deCipher = crypto.createDecipher('aes192', key);
    var decryptedData = deCipher.update(dataToDecrypt, 'hex', 'utf8');
    decryptedData += deCipher.final('utf8');
    return decryptedData;
}

function checkAuthentication(req, res, next) {
    var secureToken = req.body.token || req.query.token || req.headers['x-access-token'];
    if (secureToken) {
        let token = decrypt(secureToken, config.secret);
        jwt.verify(token, config.secret, function (err, decodedToken) {
            if (err)
                return res.status(403).json({
                    success: false,
                    message: 'Invalid token provided. Please login to continue'
                });
            else {
                req.decoded = decodedToken;
                next();
            }
        });
    } else {
        return res.status(403).json({
            success: false,
            message: 'No token provided'
        });
    }
}


const usernameRegex = /^[a-zA-Z0-9 ]{5,50}$/;
const fileTypeRegex = /^(\.jpg|\.jpeg|\.png)$/;
const passwordRegex = /^[a-zA-Z0-9 :;\/\-+*_\.]{8,50}$/;
const queryRegex = /^[a-zA-Z0-9 ]{3,100}$/;
const BASE_URL = 'http://localhost:8000';
const specificPlantName = /^[a-z]+\-?[a-z]+?$/;

module.exports = {
    encrypt: encrypt,
    decrypt: decrypt,
    checkAuthentication: checkAuthentication,
    usernameRegex: usernameRegex,
    fileTypeRegex: fileTypeRegex,
    passwordRegex: passwordRegex,
    queryRegex: queryRegex,
    BASE_URL: BASE_URL,
    specificPlantName: specificPlantName
};
