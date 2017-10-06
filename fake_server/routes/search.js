var express = require('express');
var router = express.Router();
var request = require('request');

var utilities = require('./../utilities/utility');

router.get('/plant_name', (req, res) => {
    var plantName = req.query.name;
    var nameRegex = utilities.nameRegex;

    if (!plantName || !nameRegex.test(plantName))
        return res.json({
            success: false,
            message: 'Invalid Data Sent'
        });

    console.log(plantName);
    return res.json({
        success: true,
        data: 'Some Data'
    });
    //TODO: Do something with request
});

router.post('/plant_image', (req, res) => {
    var imageFile = req.body.base64Content;

    if (!imageFile)
        return res.json({
            success: false,
            message: 'Invalid File Sent'
        });

    var options = {
        url: 'http://10.5.59.165/classifier/',
        method: 'POST',
        json: {
            image: imageFile
        }
    };

    request(options, (error, response, body) => {
        if (error) {
            console.log(error);
            return res.json({
                success: false,
                message: 'Unable to get the data. Please try again later'
            });
        }

        if (response.statusCode >= 200 && response.statusCode < 400) {
            console.log(body);
            return res.json({
                success: true,
                data: body
            });
        } else {
            return res.json({
                success: false,
                message: 'Something happened at our end. Sorry about that'
            });
        }
    });

    // res.json({
    //     success: true,
    //     data: 'Some Random Data'
    // });

    // return res.json({
    //     success: true,
    //     data: 'Some Data'
    // });
    //TODO: Do something with request
});


module.exports = router;
