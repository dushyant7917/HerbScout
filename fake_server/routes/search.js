var express = require('express');
var router = express.Router();
var request = require('request');

var utilities = require('./../utilities/utility');

router.get('/plant_info', (req, res) => {
    var queryContent = req.query.query;
    var queryRegex = utilities.queryRegex;

    if (!queryContent || !queryRegex.test(queryContent))
        return res.json({
            success: false,
            message: 'Invalid Data Sent'
        });

    console.log(queryContent);
    var options = {
        url: `${utilities.BASE_URL}/search/`,
        method: 'POST',
        json: {
            query: queryContent
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
            // console.log(body);
            let results = body.results.map(element => {
                return {
                    botanical_name: element.botanical_name,
                    image: `http://localhost:3000/PlantImages/${element.botanical_name}/0.jpg`
                };
            });
            return res.json({
                success: true,
                results: results
            });
        } else {
            return res.json({
                success: false,
                message: 'Something happened at our end. Sorry about that...'
            });
        }
    });
});
2

router.get('/auto_complete', (req, res) => {
    var query = req.query.query;
    console.log(query);

    request(`${utilities.BASE_URL}/auto_complete/${query}/`, (error, response, body) => {
        if (error) {
            console.log(error);
            return res.json({
                success: false,
                message: 'Unable to get the data. Please try again later'
            });
        }
        console.log(response.statusCode);

        if (response.statusCode >= 200 && response.statusCode < 400) {
            body = JSON.parse(body);
            return res.json({
                success: true,
                results: body.result
            });
        } else {
            return res.json({
                success: false,
                message: 'Something happened at our end. Sorry about that...'
            });
        }
    });
});

router.get('/get_specific_plant', (req, res) => {
    var plantName = req.query.name;
    var nameRegex = utilities.specificPlantName;

    if (!plantName || !nameRegex.test(plantName))
        return res.json({
            success: false,
            message: 'Invalid Data Sent'
        });

    console.log(plantName);

    request(`${utilities.BASE_URL}/herb-info/${plantName}/`, (error, response, body) => {
        if (error) {
            console.log(error);
            return res.json({
                success: false,
                message: 'Unable to get the data. Please try again later'
            });
        }

        if (response.statusCode >= 200 && response.statusCode < 400) {
            // console.log(typeof body);
            // console.log(body['found']);
            body = JSON.parse(body);
            if (body.found === false || body.identified === false) {
                return res.json({
                    success: false,
                    message: 'Sorry, Unable to find something matching that name'
                });
            } else {
                return res.json({
                    success: true,
                    data: body
                });
            }
        } else {
            console.log(body);
            return res.json({
                success: false,
                message: 'Something happened at our end. Sorry about that'
            });
        }
    });
});

router.post('/plant_image', (req, res) => {
    var imageFile = req.body.base64Content;

    if (!imageFile)
        return res.json({
            success: false,
            message: 'Invalid File Sent'
        });

    var options = {
        url: `${utilities.BASE_URL}/classifier/`,
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
            // body = JSON.parse(body);
            console.log(typeof body);
            console.log(body);
            if (body.identified === false || body.found === false) {
                return res.json({
                    success: false,
                    message: 'Unable to identify the plant'
                });
            } else {
                console.log('Proper Response');
                return res.json({
                    success: true,
                    data: body
                });
            }
        } else {
            console.log('Error Occurred');
            return res.json({
                success: false,
                message: 'Something happened at our end. Sorry about that'
            });
        }
    });
});


module.exports = router;
