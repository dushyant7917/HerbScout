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
                            {{ item.herb_data.botanical_name }}
                        </div>
                        <div class="center-image">
                            <img :src="hyphenateImage(item.herb_data.botanical_name)" :alt="item.title" class="image-view" />
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
                                            <div class="title">
                                                Used For
                                            </div>
                                            <v-list>
                                                <v-list-tile v-for="uses in item.herb_data.properties" :key="uses">
                                                    <v-list-tile-content>
                                                        <v-list-tile-title class="text-xs-center">{{ uses }}</v-list-tile-title>
                                                    </v-list-tile-content>
                                                </v-list-tile>
                                            </v-list>
                                        </v-flex>
                                        <v-flex xs12>
                                            <div class="title">Parts Used</div>
                                            <v-list>
                                                <v-list-tile v-for="part in item.herb_data.parts_used" :key="part">
                                                    <v-list-tile-content>
                                                        <v-list-tile-title class="text-xs-center">{{ part }}</v-list-tile-title>
                                                    </v-list-tile-content>
                                                </v-list-tile>
                                            </v-list>
                                        </v-flex>
                                        <v-flex xs12>
                                            <div class="title">Similar Plants</div>
                                            <v-list>
                                                <v-list-tile v-for="plant in item.recommendations" :key="plant.botanical_name" @click.stop="getPlantInfo(plant.botanical_name)">
                                                    <v-list-tile-content>
                                                        <v-list-tile-title class="text-xs-center">{{ plant.botanical_name }}</v-list-tile-title>
                                                    </v-list-tile-content>
                                                </v-list-tile>
                                            </v-list>
                                        </v-flex>
                                    </v-layout>
                                </v-container>
                            </v-flex>
                            <v-flex xs12 md6>
                                <gmap-map :center="{lat: 20.5937, lng: 78.9629}" :zoom="4" class="map-styles">
                                    <gmap-marker v-for="pos in item.map_info" :key="pos.latitude" :position="{lat: parseFloat(pos.latitude), lng: parseFloat(pos.longitude)}"></gmap-marker>
                                </gmap-map>
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
