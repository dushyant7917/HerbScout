<template>
    <v-layout row justify-center>
        <v-dialog v-model="displayModal" fullscreen transition="dialog-bottom-transition" :overlay=false>
            <v-card>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn flat icon @click="closeInfoModal" class="red--text">
                        <v-icon>fa-times</v-icon>
                    </v-btn>
                </v-card-actions>
                <v-card-title>
                    <v-container v-if="item.askljdjfb !== undefined">
                        <div class="display-1">
                            {{ item.title | decodeHTML}}
                        </div>
                        <img :src="item.image" :alt="item.title" class="image-view" />
                        <div class="title html-description-content" v-html="filterIFrames(item.description)">
                        </div>
                    </v-container>
                </v-card-title>
                <v-bottom-nav value="true" class="white">
                    <slot name="slot_1"></slot>
                    <v-btn flat class="deep-orange--text" :value="true" target="_blank" :href="item.URL">
                        <v-icon>fa-external-link</v-icon>
                    </v-btn>
                    <slot name="slot_2"></slot>
                    <slot name="slot_3"></slot>
                </v-bottom-nav>
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
            }
        },
        data() {
            return {
                displyModal: false
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
            }
        }
    }

</script>
