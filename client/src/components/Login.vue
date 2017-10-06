<template>
    <v-layout row wrap>
        <v-flex xs12 sm8 offset-sm2 md6 offset-md3>
            <v-card>
                <v-card-media src="/static/TestImage.jpg" height="300px" class="black--text image-zoom">
                    <v-container fill-height fluid>
                        <v-layout fill-height>
                            <v-flex xs12 align-end flexbox style="display: flex">
                                <span class="headline headline-text-shadow white--text text--darken-2" v-if="displayLogin">Sign in to continue</span>
                                <span class="headline headline-text-shadow white--text text--darken-2" v-else>Sign up to register</span>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-media>
                <v-card-title>
                    <v-container>
                        <v-layout row wrap>
                            <v-flex xs12>
                                <v-text-field label="Username" v-model="username" :counter="10" :rules="[rules.required, rules.username]" required></v-text-field>
                            </v-flex>
                            <v-flex xs12>
                                <v-text-field label="Password" v-model="password" :rules="[rules.required, rules.password]" type="password" required></v-text-field>
                            </v-flex>
                        </v-layout>
                    </v-container>
                    <v-container>
                        <v-layout row wrap>
                            <v-flex xs12 v-if="displayLogin">
                                <v-btn class="green darken-3 white--text" style="float: right" @click="handleUserLogin" :loading="loading" :disabled="loading">
                                    <v-icon>send</v-icon>
                                    <span style="padding: 0 7px">Sign In</span>
                                </v-btn>
                                <span class="subheading prompt-main-text" @click="displaySignUp">
                                    Not yet registered?
                                </span>
                            </v-flex>
                            <v-flex xs12 v-else>
                                <v-btn class="green darken-3 white--text" style="float: right" @click="handleUserRegistration" :loading="loading" :disabled="loading">
                                    <v-icon>send</v-icon>
                                    <span style="padding: 0 7px">Sign Up</span>
                                </v-btn>
                                <span class="subheading prompt-main-text" @click="displayLogin = true">
                                    Already Registered?
                                </span>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-title>
            </v-card>
        </v-flex>
    </v-layout>
</template>

<script>
    import {
        usernameRegex,
        passwordRegex
    } from './../utitility';
    import {
        loginUser,
        registerUser
    } from './../api/api';

    import {
        mapGetters,
        mapMutations
    } from 'vuex';

    export default {
        data() {
            return {
                username: '',
                password: '',

                displayLogin: true,
                loading: false,

                rules: {
                    required: (value) => !!value || 'Required.',
                    username: (value) => {
                        return usernameRegex.test(value) || 'Invalid Username';
                    },
                    password: (value) => {
                        return passwordRegex.test(value) || 'Invalid Password';
                    }
                }
            };
        },
        methods: {
            ...mapGetters([
                'getToken'
            ]),
            ...mapMutations([
                'setUser'
            ]),
            handleUserLogin() {
                if (this.validateAllFields()) {
                    this.loading = true;
                    loginUser(this.username, this.password)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success) {
                                    this.setUser({
                                        user: data.user,
                                        token: data.token
                                    });
                                    this.$router.push({
                                        path: '/dashboard'
                                    });
                                } else
                                    this.$emit('displayMessage', 'error', data.message);
                            } else
                                this.$emit('displayMessage', 'error', data.error);
                            this.loading = false;
                        });
                } else {
                    this.$emit('displayMessage', 'warning', 'Please fill all the fields proprerly');
                }
            },
            handleUserRegistration() {
                if (this.validateAllFields()) {
                    this.loading = true;
                    registerUser(this.username, this.password)
                        .then(data => {
                            if (data.error === undefined) {
                                if (data.success)
                                    this.$emit('displayMessage', 'success', data.message);
                                else
                                    this.$emit('displayMessage', 'error', data.message);
                            } else
                                this.$emit('displayMessage', 'error', data.error);
                            this.loading = false;
                        });
                } else
                    this.$emit('displayMessage', 'warning', 'Please fill all the fields proprerly');
            },
            validateAllFields() {
                if (!usernameRegex.test(this.username) || !passwordRegex.test(this.password))
                    return false;
                else
                    return true;
            },
            displaySignUp() {
                this.displayLogin = false;
            }
        }
    };

</script>
