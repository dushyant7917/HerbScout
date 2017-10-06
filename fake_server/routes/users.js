var express = require('express');
var router = express.Router();

var jwt = require('jsonwebtoken');
var utility = require('./../utilities/utility');
var Model = require('./../model/model');
var config = require('./../utilities/config');

router.post('/login', (req, res) => {
    var username = req.body.username;
    var password = req.body.password;
    var usernameRegex = utility.usernameRegex;
    var passwordRegex = utility.passwordRegex;

    if (!username || !password || typeof username !== 'string' || typeof password !== 'string' ||
        !usernameRegex.test(username) || !passwordRegex.test(password))
        return res.json({
            success: false,
            message: 'Incorrect credentials format'
        });


    username = username.toLowerCase();
    Model.User.findOne({
            username: username
        }).exec()
        .then(function (user) {
            if (!user)
                res.json({
                    success: false,
                    message: 'User authentication failed'
                });
            else {
                return Model.validatePassword(password, user.password)
                    .then(function (isMatch) {
                        if (!isMatch)
                            res.json({
                                success: false,
                                message: 'User authentication failed'
                            });
                        else {
                            var token = jwt.sign({
                                    _doc: {
                                        username: user.username
                                    }
                                },
                                config.secret, {
                                    expiresIn: '7d'
                                });
                            let secureToken = utility.encrypt(token, config.secret);
                            res.json({
                                success: true,
                                token: secureToken,
                                user: {
                                    username: user.username
                                }
                            });
                        }
                    });
            }
        })
        .catch(function (err) {
            if (err !== 'Error' && err) {
                console.log(err);
                res.status(500).json({
                    success: false,
                    message: 'Something happened at our end. Check back after sometime.'
                });
            }
        });
});

router.get('/get_user', utility.checkAuthentication, (req, res) => {
    var username = req.body.username;

    if (!username || !utility.usernameRegex.test(username))
        return res.json({
            success: false,
            message: 'Invalid credentials'
        });
    else {
        username = username.toLowerCase();
        Model.User.findOne({
                username: username
            }).exec()
            .then((user) => {
                if (!user)
                    res.json({
                        success: false,
                        message: 'User authentication failed'
                    });
                else {
                    res.json({
                        success: true,
                        user: user
                    });
                }
            })
            .catch((err) => {
                if (err !== 'Error' && err) {
                    console.log(err);
                    res.status(500).json({
                        success: false,
                        message: 'Something happened at our end. Check back after sometime.'
                    });
                }
            });
    }
});

router.post('/register', (req, res) => {
    var username = req.body.username;
    var password = req.body.password;

    if (!username || !password || typeof (username) !== 'string' ||
        typeof (password) !== 'string' || !utility.usernameRegex.test(username) ||
        !utility.passwordRegex.test(password))
        return res.json({
            success: false,
            message: 'Incorrect credentials format'
        });
    else {
        username = username.toLowerCase();
        Model.User.findOne({
                username: username
            }).exec()
            .then(function (user) {
                if (user) {
                    res.json({
                        success: false,
                        message: 'User is already registered. Please select another username.'
                    });
                    return Promise.reject('Error');
                } else {
                    return Model.createHash(password);
                }
            })
            .then(function (hash) {
                return Model.User({
                    username: username,
                    password: hash
                });
            })
            .then(function (averageJoe) {
                return averageJoe.save();
            })
            .then(function () {
                res.json({
                    success: true,
                    message: 'User registration successful. Please login to continue...'
                });
            })
            .catch(function (err) {
                if (err !== 'Error' && err) {
                    console.log(err);
                    res.status(500).json({
                        success: false,
                        message: 'Something happened at our end. Check back after sometime.'
                    });
                }
            });
    }
});

module.exports = router;
