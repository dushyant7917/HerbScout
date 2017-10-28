<template>
    <v-app light>
        <v-snackbar :top="true" :right="true" :color="snackBarType" v-model="displaySnackBar">
            {{ snackBarMessage }}
            <v-btn flat class="white--text" @click.stop="displaySnackBar = false">Close</v-btn>
        </v-snackbar>

        <v-toolbar light class="extra-margin-bottom">
            <!-- <v-toolbar-side-icon class="hidden-md-and-up" @click.stop="displayDrawer = true"></v-toolbar-side-icon> -->
            <v-toolbar-title class="blue--text darken-2">HerbScout</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-items>
                <v-btn flat :to="'/'" exact v-if="!userExists()">
                    Home
                </v-btn>
                <!-- <v-btn flat :to="'/login'" exact v-if="!userExists()">
                    Login
                </v-btn>
                <v-btn flat :to="'/dashboard'" exact v-else="userExists()">
                    DashBoard
                </v-btn>
                <v-btn flat @click.stop="logoutUser" exact v-if="userExists()">
                    Logout
                </v-btn> -->
            </v-toolbar-items>
        </v-toolbar>

        <!-- <v-navigation-drawer temporary clipped height="100%" v-model="displayDrawer">
            <v-list class="pt-0">
                <v-list-tile :to="'/'" exact v-if="!userExists()">
                    <v-list-tile-action>
                        <v-icon>home</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Home</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile :to="'/login'" exact v-if="!userExists()">
                    <v-list-tile-action>
                        <v-icon>account_circle</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Login</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile :to="'/dashboard'" exact v-else>
                    <v-list-tile-action>
                        <v-icon>home</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>DashBoard</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile @click.stop="logoutUser" v-if="userExists()">
                    <v-list-tile-action>
                        <v-icon>assignment_return</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Logout</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>
        </v-navigation-drawer> -->

        <main>
            <v-layout>
                <v-flex xs12>
                    <transition name="slide-x-reverse-transition">
                        <router-view @displayMessage="displayMessage">
                        </router-view>
                    </transition>
                </v-flex>
            </v-layout>
        </main>

        <!-- <v-footer absolute style="background-color: #fafafa">
            <v-spacer></v-spacer>
            <div class="title" style="padding: 7px">Desgined with 💗 by: Team Hack++</div>
        </v-footer> -->
    </v-app>
</template>

<script>
    import {
        mapMutations,
        mapGetters
    } from 'vuex';

    export default {
        data() {
            return {
                snackBarType: '',
                snackBarMessage: '',
                displaySnackBar: false,
                displayDrawer: false
            };
        },
        methods: {
            ...mapGetters([
                'userExists'
            ]),
            ...mapMutations([
                'removeUser'
            ]),
            displayMessage(messageType, message) {
                this.snackBarType = messageType;
                this.snackBarMessage = message;
                this.displaySnackBar = true;
            },
            logoutUser() {
                this.removeUser();
                this.$router.push({
                    path: '/'
                });
            }
        }
    }

</script>
