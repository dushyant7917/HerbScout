<template>
    <v-layout row justify-center>
        <v-dialog v-model="displayModal" fullscreen transition="dialog-bottom-transition" :overlay=false>
            <v-card>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn flat icon @click="closeInfoModal" class="red--text">
                        <v-icon>close</v-icon>
                    </v-btn>
                </v-card-actions>
                <v-card-title>
                    <v-container v-if="item.herb_data !== undefined">
                        <div class="display-1 text-xs-center">
                            {{ item.herb_data.botanical_name | capitalizeName }}
                        </div>
                        <div class="center-image">
                            <v-parallax :src="hyphenateImage(item.herb_data.botanical_name)" class="image-view"></v-parallax>
                        </div>
                    </v-container>
                </v-card-title>
                <v-card-title>
                    <v-container v-if="item.herb_data !== undefined">
                        <v-layout row wrap>
                            <v-flex xs12 md6>
                                <v-container>
                                    <v-layout row wrap>
                                        <v-flex xs12>
                                            <v-card>
                                                <v-card-title>
                                                    <v-expansion-panel expand popout>
                                                        <v-expansion-panel-content ripple :value="true">
                                                            <div slot="header">
                                                                <span class="title">Used For</span>
                                                            </div>
                                                            <v-list>
                                                                <v-list-tile v-for="uses in item.herb_data.properties" :key="uses">
                                                                    <v-list-tile-content>
                                                                        <v-list-tile-title class="text-xs-center">{{ uses | capitalizeName }}</v-list-tile-title>
                                                                    </v-list-tile-content>
                                                                </v-list-tile>
                                                            </v-list>
                                                        </v-expansion-panel-content>
                                                    </v-expansion-panel>
                                                </v-card-title>
                                            </v-card>
                                        </v-flex>
                                        <v-flex xs12>
                                            <v-card>
                                                <v-card-title>
                                                    <v-expansion-panel expand popout>
                                                        <v-expansion-panel-content ripple :value="true">
                                                            <div slot="header">
                                                                <span class="title">Parts Used</span>
                                                            </div>
                                                            <v-list>
                                                                <v-list-tile v-for="part in item.herb_data.parts_used" :key="part">
                                                                    <v-list-tile-content>
                                                                        <v-list-tile-title class="text-xs-center">{{ part }}</v-list-tile-title>
                                                                    </v-list-tile-content>
                                                                </v-list-tile>
                                                            </v-list>
                                                        </v-expansion-panel-content>
                                                    </v-expansion-panel>
                                                </v-card-title>
                                            </v-card>
                                        </v-flex>
                                        <v-flex xs12>
                                            <v-card>
                                                <v-card-title>
                                                    <v-expansion-panel expand popout>
                                                        <v-expansion-panel-content ripple :value="true">
                                                            <div slot="header">
                                                                <span class="title">Other Similar Plants</span>
                                                            </div>
                                                            <v-list>
                                                                <v-list-tile v-for="plant in item.recommendations" :key="plant.botanical_name" @click.stop="getPlantInfo(plant.botanical_name)">
                                                                    <v-list-tile-content>
                                                                        <v-list-tile-title class="text-xs-center">{{ plant.botanical_name | formatName }}</v-list-tile-title>
                                                                    </v-list-tile-content>
                                                                </v-list-tile>
                                                            </v-list>
                                                        </v-expansion-panel-content>
                                                    </v-expansion-panel>
                                                </v-card-title>
                                            </v-card>
                                        </v-flex>
                                    </v-layout>
                                </v-container>
                            </v-flex>
                            <v-flex xs12 md6>
                                <v-container style="height: 100%">
                                    <v-card style="height: 100%" class="center-vertical">
                                        <v-card-text>
                                            <v-container style="height: 100%">
                                                <div style="height: 100%">
                                                    <gmap-map style="height: 100%" :center="{lat: 20.5937, lng: 78.9629}" :zoom="4" class="map-styles">
                                                        <gmap-marker v-for="pos in item.map_info" :key="pos.latitude" :position="{lat: parseFloat(pos.latitude), lng: parseFloat(pos.longitude)}"></gmap-marker>
                                                    </gmap-map>
                                                </div>
                                            </v-container>
                                        </v-card-text>
                                    </v-card>
                                </v-container>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-title>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    export default {
        props: {
            item: {
                type: Object,
                required: true
            },
            closeModal: {
                type: Function,
                required: true
            },
            showModal: {
                type: Boolean,
                required: true
            },
            getPlantInfo: {
                type: Function,
                required: true
            }
        },
        data() {
            return {
                displayModal: false
            };
        },
        watch: {
            showModal(updatedValue) {
                this.displayModal = updatedValue;
            }
        },
        methods: {
            closeInfoModal() {
                this.displayModal = false;
                this.closeModal();
            },
            hyphenateImage(value) {
                let hyphenatedValue = value.split(' ').map(element => {
                    return element.charAt(0).toLowerCase() + element.slice(1);
                }).join('-');
                return `http://localhost:3000/PlantImages/${hyphenatedValue}/0.jpg`;
            }
        }
    }

</script>
