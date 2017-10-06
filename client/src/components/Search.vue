<template>
    <v-container class="text-xs-center">
        <v-layout row align-center wrap>
            <v-flex xs11>
                <v-text-field v-model="plantName" label="Enter a Name:" name="plantName" :rules="[rules.name]">
                </v-text-field>
            </v-flex>
            <v-flex xs1>
                <v-btn icon class="red--text" :loading="loading" :disabled="loading" @click.stop="searchPlantInfo">
                    <v-icon>search</v-icon>
                </v-btn>
            </v-flex>
        </v-layout>

        <v-layout row wrap>
            <PlantCard v-for="result in results" :name="result.botanical_name" :key="result.botanical_name" :getSpecificPlant="getSpecificPlant"></PlantCard>
        </v-layout>

    </v-container>
</template>

<script>
    import {
        searchPlantInfo,
        getSpecificPlant
    } from './../api/api';

    import {
        queryRegex,
        specificPlantNameRegex
    } from './../utitility';

    import PlantCard from './sub_components/PlantCard';

    export default {
        data() {
            return {
                plantName: '',
                loading: false,
                rules: {
                    name: (value) => {
                        return queryRegex.test(value) || 'Invalid Characters in Data';
                    }
                },

                results: [],
                plantInfo: {}
            }
        },
        components: {
            PlantCard
        },
        methods: {
            searchPlantInfo() {
                if (!queryRegex.test(this.plantName)) {
                    this.$emit('displayMessage', 'error', 'Invalid Characters in Plant Name');
                    return;
                }

                this.loading = true;

                searchPlantInfo(this.plantName)
                    .then(data => {
                        if (data.error === undefined) {
                            if (data.success) {
                                this.results = data.data.results;
                            } else {
                                this.$emit('displayMessage', 'error', data.message);
                            }
                        } else {
                            this.$emit('displayMessage', 'error', data.error);
                        }

                        this.loading = false;
                    });
            },
            getSpecificPlant(name) {
                if (specificPlantNameRegex.test(name)) {
                    getSpecificPlant(name)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success) {
                                    this.plantInfo = data.data;
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
            }
        }
    }

</script>
