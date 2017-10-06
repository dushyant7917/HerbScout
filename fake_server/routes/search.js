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

    // request(options, (error, response, body) => {
    //     if (error) {
    //         console.log(error);
    //         return res.json({
    //             success: false,
    //             message: 'Unable to get the data. Please try again later'
    //         });
    //     }

    //     if (response.statusCode >= 200 && response.statusCode < 400) {
    //         console.log(body);
    //         return res.json({
    //             success: true,
    //             data: body
    //         });
    //     } else {
    //         return res.json({
    //             success: false,
    //             message: 'Something happened at our end. Sorry about that...'
    //         });
    //     }
    // });

    // res.json({
    //     success: true,
    //     data:
    // });

    var results = [{
            botanical_name: 'aristolochia-bracteata'
        },
        {
            botanical_name: 'artanema-longifolia'
        },
        {
            botanical_name: 'balsamodendron-mukul'
        },
        {
            botanical_name: 'berberis-asiatica'
        },
        {
            botanical_name: 'aloe-indica'
        },
        {
            botanical_name: 'abies-webbiana'
        },
        {
            botanical_name: 'enicostemma-hyssopifolium'
        },
        {
            botanical_name: 'garcinia-morella'
        },
        {
            botanical_name: 'gloriosa-superba'
        },
        {
            botanical_name: 'habenaria-edgeworthii'
        },
        {
            botanical_name: 'kirganelia-reticulata'
        },
        {
            botanical_name: 'piper-wallichii'
        },
        {
            botanical_name: 'peganum-harmala'
        },
        {
            botanical_name: 'rhabdia-lycioides'
        },
        {
            botanical_name: 'rotula-aquatica'
        },
        {
            botanical_name: 'xyris-indica'
        },
        {
            botanical_name: 'barleria-longifolia'
        },
        {
            botanical_name: 'acacia-concinna'
        }
    ];

    results = results.map(element => {
        return {
            botanical_name: element.botanical_name,
            image: `http://localhost:3000/PlantImages/${element.botanical_name}/0.jpg`
        };
    });

    res.json({
        success: true,
        results: results
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

    // request(`${utilities.BASE_URL}/herb-info/${plantName}/`, (error, response, body) => {
    //     if (error) {
    //         console.log(error);
    //         return res.json({
    //             success: false,
    //             message: 'Unable to get the data. Please try again later'
    //         });
    //     }

    //     if (response.statusCode >= 200 && response.statusCode < 400) {
    //         console.log(body);
    //         if (!body.found) {
    //             return res.json({
    //                 success: false,
    //                 message: 'Unable to find something with that name'
    //             });
    //         } else {
    //             return res.json({
    //                 success: true,
    //                 data: body
    //             });
    //         }
    //     } else {
    //         return res.json({
    //             success: false,
    //             message: 'Something happened at our end. Sorry about that...'
    //         });
    //     }
    // });

    var data = {
        'found': true,
        'recommendations': [{
                botanical_name: 'terminalia-paniculata'
            },
            {
                botanical_name: 'terminalia-arjuna'
            },
            {
                botanical_name: 'pueraria-tuberosa'
            },
            {
                botanical_name: 'cleome-viscosa'
            },
            {
                botanical_name: 'rheum-moorcroftianum'
            }
        ],
        'map_info': [{
                'latitude': '14.7504291',
                'longitude': '78.57002559'
            },
            {
                'latitude': '27.10039878',
                'longitude': '93.61660071'
            },
            {
                'latitude': '26.7499809',
                'longitude': '94.21666744'
            }
        ],
        'herb_data': {
            'parts_used': [
                'Bark'
            ],
            'properties': [
                'Balance disorder',
                'Bleeding disorders',
                'Bipolar disorders',
                'Bob disorders',
                'Body dysmorphic disorder'
            ],
            'places': [
                'Andhra Pradesh',
                'Arunachal Pradesh',
                'Assam'
            ],
            botanical_name: 'Azadirachta indica'
        }
    };

    data.herb_data.image = `http://localhost:3000/PlantImages/` +
        `${utilities.hyphenateName(data.herb_data.botanical_name)}/0.jpg`;

    return res.json({
        success: true,
        data: data
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
        url: `${utilities.BASE_URL}/classifier/`,
        method: 'POST',
        json: {
            image: imageFile
        }
    };

    // request(options, (error, response, body) => {
    //     if (error) {
    //         console.log(error);
    //         return res.json({
    //             success: false,
    //             message: 'Unable to get the data. Please try again later'
    //         });
    //     }

    //     if (response.statusCode >= 200 && response.statusCode < 400) {
    //         console.log(body);
    //         if (!body.identified) {
    //             return res.json({
    //                 success: false,
    //                 message: 'Unable to identify the plant'
    //             });
    //         } else {
    //             return res.json({
    //                 success: true,
    //                 data: body
    //             });
    //         }
    //     } else {
    //         return res.json({
    //             success: false,
    //             message: 'Something happened at our end. Sorry about that'
    //         });
    //     }
    // });

    var data = {
        'identified': true,
        'result': {
            'found': true,
            'recommendations': [{
                    botanical_name: 'terminalia-paniculata'
                },
                {
                    botanical_name: 'terminalia-arjuna'
                },
                {
                    botanical_name: 'pueraria-tuberosa'
                },
                {
                    botanical_name: 'cleome-viscosa'
                },
                {
                    botanical_name: 'rheum-moorcroftianum'
                }
            ],
            'map_info': [{
                    'latitude': '14.7504291',
                    'longitude': '78.57002559'
                },
                {
                    'latitude': '27.10039878',
                    'longitude': '93.61660071'
                },
                {
                    'latitude': '26.7499809',
                    'longitude': '94.21666744'
                }
            ],
            'herb_data': {
                'parts_used': [
                    'Bark'
                ],
                'properties': [
                    'Balance disorder',
                    'Bleeding disorders',
                    'Bipolar disorders',
                    'Bob disorders',
                    'Body dysmorphic disorder'
                ],
                'places': [
                    'Andhra Pradesh',
                    'Arunachal Pradesh',
                    'Assam'
                ],
                botanical_name: 'Azadirachta indica'
            }
        }
    };

    data.herb_data.image = 'http://localhost:3000/PlantImages/' +
        utilities.hyphenateName(data.herb_data.botanical_name) + '/0.jpg';
    console.log(data);

    return res.json({
        success: true,
        data: data
    });
});


module.exports = router;
