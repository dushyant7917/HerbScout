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
            <PlantCard v-for="result in results" :image="result.image" :name="result.botanical_name" :key="result.botanical_name" :getSpecificPlant="getSpecificPlant"></PlantCard>
        </v-layout>

        <PlantView :item="plantInfo" :closeModal="closeModal" :showModal="displayModal" :getPlantInfo="getPlantInfo"></PlantView>

        <v-container v-if="results.length === 0 && !loading">
            <v-card raised>
                <v-card-text>
                    <v-layout row wrap>
                        <v-flex xs12 style="padding: 7px 0">
                            <v-icon class="red--text">info</v-icon>
                        </v-flex>
                        <v-flex xs12>
                            <span class="title">No Results Found. Search for Something Else</span>
                        </v-flex>
                    </v-layout>
                </v-card-text>
            </v-card>
        </v-container>

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
    import PlantView from './sub_components/PlantView';

    export default {
        props: {
            name: {
                type: String
            }
        },
        data() {
            return {
                plantName: this.name,
                loading: false,
                rules: {
                    name: (value) => {
                        return queryRegex.test(value) || 'Invalid Characters in Data';
                    }
                },

                results: [],
                plantInfo: {},
                displayModal: false
            }
        },
        components: {
            PlantCard,
            PlantView
        },
        mounted() {
            this.searchPlantInfo();
        },
        methods: {
            hyphenatePlantName(name) {
                return name.split(' ').map(element => {
                    return element.charAt(0).toLowerCase() + element.slice(1);
                }).join('-');
            },
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
                                this.results = data.results;
                                if (data.results.length === 0)
                                    this.$emit('displayMessage', 'error', 'No results found');
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
            }
        }
    }

</script>
