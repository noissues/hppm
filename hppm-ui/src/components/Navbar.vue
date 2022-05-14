<template>
  <nav>
    <v-app-bar app elevation="3" color="rgba(255, 255, 255, 0.85)" style="backdrop-filter: saturate(180%) blur(20px)">
      <!--当小屏幕时，菜单展示为左侧的 drawer-->
      <v-app-bar-nav-icon
              class="hidden-md-and-up"
              @click="drawer = true"
      />

      <v-container class="fill-height py-0" style="max-width: 1080px">
        <v-row align="center">
          <v-toolbar-title class="text-h4 font-weight-bold hidden-sm-and-down mr-5">
            <router-link to="/" style="text-decoration: none;">
              <span>HP</span>
              <span class="grey--text">PM</span>
            </router-link>
          </v-toolbar-title>

          <v-btn
                  v-for="(link, i) in links"
                  :key="i"
                  class="text-subtitle-1 font-weight-bold hidden-sm-and-down"
                  text
                  @click="goTo(link)"
          > {{ link.text }}
          </v-btn>

          <v-spacer/>

          <v-text-field
                  rounded
                  dense
                  append-icon="mdi-magnify"
                  flat
                  hide-details
                  solo-inverted
                  label="搜索歌名或歌手"
                  style="max-width: 300px;"
                  @click:append="searchByParam"
                  @keyup.enter="searchByParam"
                  v-model="searchParam"
          />
        </v-row>
      </v-container>
    </v-app-bar>

    <v-navigation-drawer
            v-model="drawer"
            absolute
            temporary
    >

      <v-list>
        <v-list-item>
          <v-list-item-title class="text-h4 font-weight-bold">
            <router-link to="/" style="text-decoration: none;">
              <span>HP</span>
              <span class="grey--text">PM</span>
            </router-link>
          </v-list-item-title>
        </v-list-item>
      </v-list>
      <v-list
              nav
      >
        <v-list-item-group
                v-model="group"
                active-class="text--accent-4"
                color="primary"
        >
          <v-list-item
                  v-for="(item, i) in links"
                  :key="i"
                  :to="item.route"
          >
            <v-list-item-icon>
              <v-icon v-text="item.icon"></v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title class="" v-text="item.text"></v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

  </nav>
</template>

<script>
  import Bus from '../bus.js';

  export default {
    name: "Navbar",

    components: {
    },

    data() {
      return {
        searchParam: "",
        showSnackbar: false,
        drawer: false,
        links: [
          {icon: 'mdi-home', text: '首页', route: '/'},
          {icon: 'mdi-account', text: '我的曲谱', route: '/admin'},
        ],
      }
    },

    methods: {
      searchByParam() {
        Bus.$emit('searchOnClick', this.searchParam);
      },
      goTo(link) {
        this.$router.push(link.route)
      }
    }
  }
</script>

<style scoped>

</style>