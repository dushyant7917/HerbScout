<template>
    <v-flex xs12 sm6 md4 lg3 xl3>
        <v-container @click.stop="getSpecificPlant(name)">
            <v-card raised hover class="text-xs-center" height="70px">
                <v-card-text>
                    <div class="title center-vertical">
                        {{ name }}
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
            getSpecificPlant: {
                type: Function,
                required: true
            }
        }
    }

</script>
