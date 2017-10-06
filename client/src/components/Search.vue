<template>
    <v-container class="text-xs-center">
        <v-layout row align-center wrap>
            <v-flex xs11>
                <v-text-field v-model="plantName" label="Enter a Name:" name="plantName" :rules="[rules.name]">
                </v-text-field>
            </v-flex>
            <v-flex xs1>
                <v-btn icon class="red--text">
                    <v-icon>search</v-icon>
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
        nameRegex
    } from './../utitility';

    export default {
        props: {
            name: {
                type: String
            }
        },
        data() {
            return {
                plantName: '',
                loading: false,
                rules: {
                    name: (value) => {
                        return nameRegex.test(value) || 'Invalid Characters in Data';
                    }
                }
            }
        },
        mounted() {
            console.log(this.name);
            console.log(this);
        },
        methods: {
            searchPlantInfo() {
                if (!nameRegex.test(this.plantName)) {
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
        }
    }

</script>
