<template>
    <v-container class="text-xs-center">

        <input type="file" name="file" ref="fileInput" class="input-file" @change="handleFileChange" />

        <v-layout row align-center wrap>
            <v-flex xs12 sm12 md5 lg5>
                <v-text-field v-model="plantName" label="Enter a Name:" name="plantName" :rules="[rules.name]">
                </v-text-field>
            </v-flex>
            <v-flex xs12 sm12 md2 lg2>
                <h3 class="grey--text">--OR--</h3>
            </v-flex>
            <v-flex xs12 sm12 md5 lg5>
                <v-layout row wrap align-center>
                    <v-flex xs12 sm6>
                        <v-btn @click.stop="$refs.fileInput.click()" class="red white--text" fluid>
                            <v-icon>file_upload</v-icon>
                            <span>Upload Photo</span>
                        </v-btn>
                    </v-flex>
                    <v-flex xs12 sm6 @click.stop="$refs.fileInput.click()">
                        <v-text-field :disabled="true" :label="fileName">
                        </v-text-field>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
        <v-layout row align-center>
            <v-flex xs12>
                <v-btn @click.stop="searchPlantData" class="green white--text" :loading="loading" :disabled="loading">
                    <span class="main-submit-button">Submit</span>
                    <v-icon>send</v-icon>
                </v-btn>
            </v-flex>
        </v-layout>

    </v-container>
</template>


<script>
    import {
        sendPlantImage,
        searchPlantInfo
    } from './../api/api';

    import {
        queryRegex,
        fileExtensionRegex
    } from './../utitility';

    export default {
        data() {
            return {
                plantName: '',
                fileName: 'Filename will be displayed here...',
                imageFile: null,
                imageBase64: '',
                fileReader: new FileReader(),

                loading: false,

                rules: {
                    name: (value) => {
                        return queryRegex.test(value) || 'Invalid Characters in Data';
                    }
                }
            };
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
                                    console.log(data);
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

                    searchPlantInfo(this.plantName)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success) {
                                    console.log(data);
                                    this.resetFields();
                                } else {
                                    this.$emit('displayMessage', 'error', data.message);
                                }
                            } else {
                                this.$emit('displayMessage', 'error', data.error);
                            }

                            this.loading = false;
                        });
                }
            },
            resetFields() {
                this.plantName = '';
                this.imageBase64 = '';
                this.imageFile = null;
                this.fileName = 'Filename will be displayed here...';
            }
        }
    }

</script>
