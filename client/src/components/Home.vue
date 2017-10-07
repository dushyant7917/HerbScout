<template>
    <v-container class="text-xs-center">

        <input type="file" name="file" ref="fileInput" class="input-file" @change="handleFileChange" />

        <div class="justify-center align-center" style="display: flex">
            <v-card style="max-width: 800px;">
                <v-card-text style="height: 200px;">
                    <!-- <img src="/static/logo.png" alt="Image" style="height: 100%;" class="image-zoom"> -->
                    <div class="background-main-image image-zoom-helper"></div>
                </v-card-text>
                <v-card-title>
                    <v-container>
                        <form @submit.prevent="searchPlantData">
                            <v-layout row align-center wrap>
                                <v-flex xs12>
                                    <v-container style="max-width: 600px;">
                                        <v-text-field v-model="plantName" label="Search Something. Will Ya..." name="plantName" :rules="[rules.name]">
                                        </v-text-field>
                                    </v-container>
                                </v-flex>
                                <v-flex xs12>
                                    <v-container>
                                        <v-layout row wrap align-center>
                                            <v-flex xs12>
                                                <v-btn @click.stop="$refs.fileInput.click()" class="red white--text" fluid>
                                                    <span>Upload </span>
                                                    <v-icon>file_upload</v-icon>
                                                </v-btn>
                                            </v-flex>
                                            <v-flex xs12 @click.stop="$refs.fileInput.click()" class="text-xs-center" style="display: flex; justify-content: center; align-items: center;">
                                                <v-text-field :disabled="true" :label="fileName" style="max-width: 300px;">
                                                </v-text-field>
                                            </v-flex>
                                        </v-layout>
                                    </v-container>
                                </v-flex>
                            </v-layout>
                        </form>
                        <v-layout row align-center>
                            <v-flex xs12>
                                <v-btn @click.stop="searchPlantData" class="green white--text" :loading="loading" :disabled="loading">
                                    <span class="main-submit-button">Submit</span>
                                    <v-icon>send</v-icon>
                                </v-btn>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-title>
            </v-card>
        </div>

        <PlantView :item="plantInfo" :closeModal="closeModal" :showModal="displayModal" :getPlantInfo="getPlantInfo"></PlantView>

    </v-container>
</template>


<script>
    import {
        sendPlantImage,
        getSpecificPlant
    } from './../api/api';

    import {
        queryRegex,
        fileExtensionRegex,
        specificPlantNameRegex
    } from './../utitility';

    import PlantView from './sub_components/PlantView';

    export default {
        data() {
            return {
                plantName: '',
                fileName: '',
                imageFile: null,
                imageBase64: '',
                fileReader: new FileReader(),

                plantInfo: {},
                displayModal: false,

                loading: false,

                rules: {
                    name: (value) => {
                        return queryRegex.test(value) || 'Invalid Characters in Data';
                    }
                }
            };
        },
        components: {
            PlantView
        },
        mounted() {
            this.fileReader.addEventListener('load', this.convertToBase64);
        },
        methods: {
            handleFileChange(event) {
                var imageFile = event.target.files[0];
                if (!fileExtensionRegex.test(imageFile.name)) {
                    this.$emit('displayMessage', 'error', 'Invalid File Type Selected');
                    return;
                }

                this.imageFile = imageFile;
                this.fileName = this.imageFile.name;
                this.fileReader.readAsDataURL(imageFile);
            },
            convertToBase64() {
                let base64Result = this.fileReader.result.split(',')[1];
                this.imageBase64 = base64Result;
            },
            searchPlantData() {
                if (this.plantName === '') {
                    if (this.imageFile === null) {
                        this.$emit('displayMessage', 'warning', 'No File Selected or Search Entered');
                        return;
                    }
                    this.loading = true;

                    sendPlantImage(this.imageBase64)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success) {
                                    this.plantInfo = data.data.result;
                                    this.displayModal = true;
                                    this.resetFields();

                                } else {
                                    console.log('Error from server');
                                    this.$emit('displayMessage', 'error', data.message);
                                }
                            } else {
                                console.log('Error from client');
                                this.$emit('displayMessage', 'error', data.error);
                            }

                            this.loading = false;
                        });

                } else {
                    if (!queryRegex.test(this.plantName)) {
                        this.$emit('displayMessage', 'error', 'Invalid Characters in Plant Name');
                        return;
                    }

                    this.loading = true;

                    this.$router.push({
                        path: `/search/${this.plantName}`
                    });
                }
            },
            getSpecificPlant(name) {
                if (specificPlantNameRegex.test(name)) {
                    getSpecificPlant(name)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success) {
                                    this.plantInfo = data.data;
                                    this.displayModal = true;
                                } else {
                                    this.$emit('displayMessage', 'error', data.message);
                                }
                            } else {
                                this.$emit('displayMessage', 'error', data.error);
                            }

                            this.loading = false;
                        });
                } else {
                    this.$emit('displayMessage', 'error',
                        'We are sorry this a problem on over end. We\'ll resolve it shortly');
                }
            },
            getPlantInfo(name) {
                this.getSpecificPlant(name);
            },
            closeModal() {
                this.displayModal = false;
                this.plantInfo = {};
            },
            resetFields() {
                this.plantName = '';
                this.imageBase64 = '';
                this.imageFile = null;
                this.fileName = '';
            }
        }
    }

</script>
