var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');

var UserSchema = mongoose.Schema({
    username: {
        type: String,
        required: true,
        index: true,
        lowercase: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    }
});

var createHash = (password) => {
    return bcrypt.genSalt(10)
        .then(function (salt) {
            return bcrypt.hash(password, salt);
        });
};

var validatePassword = (password, hash) => {
    return bcrypt.compare(password, hash);
};

module.exports = {
    User: mongoose.model('User', UserSchema),
    createHash: createHash,
    validatePassword: validatePassword
};
