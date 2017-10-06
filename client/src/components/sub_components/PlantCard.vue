<template>
    <v-flex xs12 sm6 md4 lg3 xl3>
        <v-container @click.stop="getSpecificPlant(name)">
            <v-card raised hover class="text-xs-center">
                <v-card-media :src="image" height="200px" class="black--text image-zoom">
                </v-card-media>
                <v-card-text style="height: 70px;">
                    <div class="title center-vertical">
                        {{ name | formatName }}
                    </div>
                </v-card-text>
            </v-card>
        </v-container>
    </v-flex>
</template>

<script>
    export default {
        props: {
            name: {
                type: String,
                required: true
            },
            image: {
                type: String,
                required: true
            },
            getSpecificPlant: {
                type: Function,
                required: true
            }
        }
    }

</script>
